package com.bdsoft.datamin.util.mail;

import java.util.Date;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class BDMailer {

	private final static String PORT = "25";
	private final static String SMTP = "smtp.163.com";
	private final static String ACCOUNT = "youknowceo@163.com";
	private final static String PWD = "dingchenye0517";

	private static String MAIL_SP = ";";

	private static String SENDER_NAME = "BD-Fetcher";

	// bd报警邮箱，自动发邮件
	private static String MAIL_BD_ALARM = "13426479431@qq.com";

	public static void main(String[] args) throws Exception {
		String to = MAIL_BD_ALARM;
		String subject = "【通知】bd-dm：京东抓取异常";
		String content = "异常报警，立马解决。邮件内容：" + new Date().toLocaleString();
		sendMail(subject, content, to);

		String title = "京东抓取评论3604页";
		String msg = "详情，内容个，了几格式统计。。 ";
		alarm(title, msg);
	}

	// 京东抓取统计邮件通知
	public static void jdFetchCount(String title, String content) {
		String subject = "京东抓取" + title;
		sendMail(subject, content, MAIL_BD_ALARM);
	}

	// 系统报警邮件通知
	public static void alarm(String title, String msg) {
		String sub = "报警>>" + title;
		msg += "\t" + new Date().toLocaleString();
		sendMail(sub, msg, MAIL_BD_ALARM);
	}

	// 系统恢复邮件通知
	public static void regain(String title, String msg) {
		String sub = "恢复>>" + title;
		msg += "\t" + new Date().toLocaleString();
		sendMail(sub, msg, MAIL_BD_ALARM);
	}

	private static void sendMail(String subject, String content, String to) {
		try {
			// 账户设置
			Properties pps = System.getProperties();
			pps.put("mail.smtp.host", SMTP);
			pps.put("mail.smtp.auth", "true");
			pps.put("mail.transport.protocol", "smtp");
			pps.put("mail.smtp.port", PORT);

			// Session
			final String username = ACCOUNT;
			final String password = PWD;
			Session session = Session.getDefaultInstance(pps, new Authenticator() {
				@Override
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(username, password);
				}
			});
			session.setDebug(false);

			// 邮件内容(发件人，收件人，内容)
			Message msg = new MimeMessage(session);
			// 设置发件人
			InternetAddress from = new InternetAddress(ACCOUNT, SENDER_NAME);
			// from.setPersonal("bd-admin");
			msg.setFrom(from);

			// 设置收件人
			String[] tos = to.split(MAIL_SP);
			int len = tos.length;
			InternetAddress[] toAddr = new InternetAddress[len];
			for (int i = 0; i < len; i++) {
				toAddr[i] = new InternetAddress(tos[i]);
			}
			msg.setRecipients(Message.RecipientType.TO, toAddr);// 主收件人

			// 设置邮件内容和主题
			msg.setSubject(subject);// 主题
			msg.setText(content);// 内容
			msg.setSentDate(new Date());// 发信时间
			msg.saveChanges();

			// 准备发送
			Transport trans = session.getTransport("smtp");
			trans.connect((String) pps.get("mail.smtp.host"), ACCOUNT, password);// 连接服务器
			trans.sendMessage(msg, msg.getAllRecipients());// 发送
			trans.close();
			// Transport.send(msg);
			for (Address a : msg.getAllRecipients()) {
				InternetAddress ia = (InternetAddress) a;
				System.out.println("邮件已发至 >> " + ia.getAddress());
			}
			System.out.println("发送成功。");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}