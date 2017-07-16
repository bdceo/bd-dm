<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%--
	IWeiboFetchDao wfd = LogicFactory.getWeiboFetchDao();
	IWeiboMailDao wmd = LogicFactory.getWeiboMailDao();
	IWwwFetchDao wfd2 = LogicFactory.getWwwFetchDao();

	String op = request.getParameter("op");
	if (!StringUtil.isEmpty(op)) {
		// 从weibo_fetch导出到weibo_mail
		if (op.equals("export")) {
			String sql = "";
			// www
			//List<WwwFetch> data = wfd2.findAll();
			/*sql = "select * from www_fetch where fetch_date>'2012-11-25 18:00:00'";
			List<WwwFetch> data = wfd2.findBySQL(sql, new WwwFetch());
			int size = data.size();
			for (int i = 0; i < size; i++) {
				WwwFetch wf = data.get(i);
				System.out.println((i + 1) + ":" + wf.getFetchMail());
				String[] mails = wf.getFetchMail().split(";");*/
			
			// 微博
			//List<WeiboFetch> data = wfd.findAll();
			sql = "select * from weibo_fetch where fetch_date>'2012-12-03 18:00:00'";
			List<WeiboFetch> data = wfd.findBySQL(sql, new WeiboFetch());
			int size = data.size();
			for (int i = 0; i < size; i++) {
				WeiboFetch wf = data.get(i);
				System.out.println((i + 1) + ":" + wf.getFetchMail());
				String[] mails = wf.getFetchMail().split(";");
				
				for (String mail : mails) {
					WeiboMail wm = new WeiboMail();
					wm = wmd.findByPropertyOne("mail", mail);
					if (wm == null) {
						wm = new WeiboMail(mail, wf.getFetchDate(), wf
								.getFetchSrc());
						wmd.save(wm);
						System.out.println(mail + " >>入库");
					} else {
						System.out.println(mail + " >>已存在");
					}
				}
			}
		} else if (op.equals("send")) {
			int tt = 0;
			int tc = 5;
			long sleep = 1000 * 5;
			// 单条读取发送
			String sql = "select * from weibo_mail where status=0 order by id asc limit 1";
			List<WeiboMail> data = wmd.findBySQL(sql, new WeiboMail());
			while ((data != null) && (data.size() > 0)) {
				WeiboMail wm = data.get(0);
				System.out.println("准备发邮件给：" + wm.getMail());
				// 发邮件
				MailInst mail = new MailInst(MailSender.getRdAcc(),
						MailSender.getRdCon());
				mail.setTo(wm.getMail());
				mail.setReply(MailSender.REPLY);
				System.out.println("发件人：" + mail.getEmail());
				boolean flag = MailSender.getInst().sendHtmlMail(mail);
				if (flag) {// 成功更新状态
					wm.setStatus(1);
				} else {// 失败，尝试重发
					if (tt < tc) {
						tt++;
						Thread.sleep(sleep);
						System.out.println("第" + tt + "次尝试重发....");
						continue;
					} else {
						wm.setStatus(2);
					}
				}
				wm.setLastSend(new Date());
				wmd.update(wm);

				/**/System.out.print("下一个邮箱发送倒计时-->");
				for (int j = 3; j > 0; j--) {
					System.out.print(j + " ");
					Thread.sleep(1000);
				}
				System.out.println("\n");

				// 加载下一邮箱准备发送
				tt = 0;
				data.clear();
				data = wmd.findBySQL(sql, new WeiboMail());
			}
		}
	}
	out.println("操作Over！" + new Date().toLocaleString());
--%>