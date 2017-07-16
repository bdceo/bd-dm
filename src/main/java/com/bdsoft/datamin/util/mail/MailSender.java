package com.bdsoft.datamin.util.mail;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import com.bdsoft.datamin.util.BDFileUtil;
import com.bdsoft.datamin.util.StringUtil;
import com.bdsoft.datamin.util.http.NetUtil;

public class MailSender {

	public static Random rd = new Random();
	public static String CHARSET = "UTF-8";// 统一编码
	public static String SMTP_PORT = "25";// 默认smtp端口
	public static String MULT_SPT = ";";// 多邮箱分隔符

	public static Integer USER_SUM = 20;// 总发件邮箱账户
	public static String USER_SMTP = "192.168.0.12";// 默认SMTP地址
	public static String USER_PWD = "jjjjjj";// 默认邮箱账户密码
	public static String USER_PRE = "bd";// 邮箱名前缀
	public static String USER_NICK = "中网银科猎头";// 发件时邮箱提示别名

	public static String REPLY = "dingchenye@zhongwangyinke.cn";// 设置邮件回复地址

	// 邮件内容
	public static Map<Integer, MailContent> mailCmap = new HashMap<Integer, MailContent>();
	public static List<String> hostList = new ArrayList<String>();
	public static List<MailAccount> accList = new ArrayList<MailAccount>();
	static {
		// 邮件标题
		Map<Integer, String> subMap = new HashMap<Integer, String>();
		subMap.put(0, "【内部周刊】本周推荐：人才招聘与猎头公司的合作关系！");
		subMap.put(1, "虔诚合作，服务高端，助力企业人才服务！");
		subMap.put(2, "【重要邮件】为提高人才招聘质量，推荐猎头公司合作！");
		subMap.put(3, "人才招聘技巧，3分钟让你解决招聘难题");
		subMap.put(4, "助力企业人才招聘服务");
		// 邮件内容
		for (Entry<Integer, String> en : subMap.entrySet()) {
			try {
				String name = BDFileUtil.getClassPath() + "tmp" + File.separator
						+ "h" + en.getKey() + ".html";
				File file = new File(name);
				FileInputStream fi = new FileInputStream(file);
				String src = NetUtil.stream2string(fi, CHARSET);

				MailContent mc = new MailContent();
				mc.setSubject(en.getValue());
				mc.setContent(src);

				mailCmap.put(en.getKey(), mc);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		// 邮箱域名
		hostList.add("mail.zwyk.cn");
		hostList.add("edm.zwyk.cn");
		hostList.add("service.zwyk.cn");
		for (int i = 1; i <= USER_SUM; i++) {
			for (String host : hostList) {
				MailAccount ma = new MailAccount(USER_PRE + i + "@" + host,
						USER_PWD, USER_SMTP);
				accList.add(ma);
			}
		}
		// 测试账号
//		importXinge();
		System.out.println("发信账户总数：" + accList.size());
	}

	public static void importXinge() {
		try {
			File f = new File(BDFileUtil.getClassPath() + "tmp" + File.separator
					+ "sender.txt");
			FileReader fr = new FileReader(f);
			BufferedReader br = new BufferedReader(fr);
			String line;
			while ((line = br.readLine()) != null) {
				// System.out.println(line);
				XEO xeo = new XEO(line);
				if (xeo.toString().contains("sogou.com")) {
					continue;
				}
				if (xeo.getType().equals("2")) {
					MailAccount ma = new MailAccount(xeo.getSender(),
							xeo.getPwd(), xeo.getDomain());
					accList.add(ma);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static MailSender sender = new MailSender();

	private MailSender() {
	}

	public static MailSender getInst() {
		if (sender == null) {
			sender = new MailSender();
		}
		return sender;
	}

	public static void main(String[] args) {
		MailContent mc = getRdCon();
		MailAccount ma = getRdAcc();

		MailInst mail = new MailInst(ma, mc);

		// String host = "smtp.126.com";
		// String account = "l6j903bnd9b@126.com";
		// String password = "0i5w3i9xkz";
		// MailInst mail = new MailInst(host, account, password, mc);

		mail.setTo("dingchenye@zhongwangyinke.cn");
		mail.setCc("bdcoo@qq.com");
		mail.setBcc("youknowceo@gmail.com");
		mail.setReply(REPLY);
		mail.setNickname(USER_NICK);
		System.out.println("发信人：" + ma.getEmail());

		// MailSender.getInst().sendTextMail(mail);
		MailSender.getInst().sendHtmlMail(mail);
	}

	// 随机获取发信账户
	public static MailAccount getRdAcc() {
		return accList.get(rd.nextInt(accList.size()));
	}

	// 随机获取邮件内容
	public static MailContent getRdCon() {
		return mailCmap.get(rd.nextInt(mailCmap.size()));
	}

	public boolean sendHtmlMail(MailInst mail) {
		try {
			// 构建Session
			Session session = mail.getSession();
			session.setDebug(false);

			// 设置内容
			Message msg = new MimeMessage(session);
			msg.setSubject(mail.getSubject());
			msg.setSentDate(new Date());
			msg.setFrom(mail.getMailFrom());
			msg.setRecipients(Message.RecipientType.TO, formAdd(mail.getTo()));
			if (!StringUtil.isEmpty(mail.getCc())) {
				msg.setRecipients(Message.RecipientType.CC,
						formAdd(mail.getCc()));
			}
			if (!StringUtil.isEmpty(mail.getBcc())) {
				msg.setRecipients(Message.RecipientType.BCC,
						formAdd(mail.getBcc()));
			}
			if (!StringUtil.isEmpty(mail.getReply())) {
				msg.setReplyTo(formAdd(mail.getReply()));
			}
			Multipart part = new MimeMultipart();
			BodyPart body = new MimeBodyPart();
			body.setContent(mail.getContent(), "text/html;charset=utf-8");
			part.addBodyPart(body);
			msg.setContent(part);

			msg.saveChanges();

			// 准备发送
			Transport.send(msg);
			for (Address a : msg.getAllRecipients()) {
				InternetAddress ia = (InternetAddress) a;
				System.out.println("邮件已发至 >> " + ia.getAddress());
			}
			
			msg = null;
			session = null;
		} catch (MessagingException me) {
			System.out.println("发信失败" + me.getMessage());
			// String next = me.getNextException().getMessage();
			// System.out.println(next);
			// if (next.contains("timed out")) {
			// 超时统计
			// }
			// me.printStackTrace();
			return false;
		} catch (Exception e) {
			System.out.println("发信失败：" + e.getMessage());
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public boolean sendTextMail(MailInst mail) {
		try {
			// 构建Session
			Session session = mail.getSession();
			session.setDebug(false);

			// 设置内容
			Message msg = new MimeMessage(session);
			msg.setSubject(mail.getSubject());
			msg.setText(mail.getContent());
			msg.setSentDate(new Date());
			msg.setFrom(mail.getMailFrom());
			msg.setRecipients(Message.RecipientType.TO, formAdd(mail.getTo()));
			if (!StringUtil.isEmpty(mail.getCc())) {
				msg.setRecipients(Message.RecipientType.CC,
						formAdd(mail.getCc()));
			}
			if (!StringUtil.isEmpty(mail.getBcc())) {
				msg.setRecipients(Message.RecipientType.BCC,
						formAdd(mail.getBcc()));
			}
			if (!StringUtil.isEmpty(mail.getReply())) {
				msg.setReplyTo(formAdd(mail.getReply()));
			}
			msg.saveChanges();

			// 准备发送
			Transport.send(msg);
			for (Address a : msg.getAllRecipients()) {
				InternetAddress ia = (InternetAddress) a;
				System.out.println("邮件已发至 >> " + ia.getAddress());
			}
		} catch (Exception e) {
			System.out.println("邮件发送失败：" + e.getMessage());
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public static Address[] formAdd(String mails) throws Exception {
		String[] ms = mails.split(MULT_SPT);
		InternetAddress[] adds = new InternetAddress[ms.length];
		int i = 0;
		for (String m : ms) {
			adds[i] = new InternetAddress(m);
			i++;
		}
		return adds;
	}

}
