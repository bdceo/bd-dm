<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Quartz-调度中心</title>
<style type="text/css">
h2 {
	display: inline;
}
</style>
</head>
<body>
	<%--
		out.println("执行时间：" + new Date().toLocaleString() + "<br/>");

		String op = request.getParameter("op");// 操作
		op = StringUtil.isEmpty(op) ? "" : op;
		String trg = request.getParameter("trg");// job表示
		int tg = StringUtil.isEmpty(trg) ? 0 : new Integer(trg);

		// 获取调度管理器
		Scheduler scheduler = JobUtil.getScheduler();
		out.println("总调度状态：" + scheduler.isStarted());
		out.println("<br/><h3>触发器状态：未定义=-1,正常=0,暂停=1,完成=2,异常=3,阻塞=4</h3>");

		// 执调度
		if (!StringUtil.isEmpty(op)) {
			if (op.equals("pause")) {
				if (StringUtil.isEmpty(trg)) {
					scheduler.pauseAll();
					out.println("<br/>全部Job已暂停");
				} else {
					String trgName = JobUtil.getTriggerName(tg);
					scheduler.pauseTrigger(trgName, JobUtil.QUARTZ_GROUP);
					out.println("<br/>job-" + trgName + " 已暂停");
				}
			} else if (op.equals("resume")) {
				if (StringUtil.isEmpty(trg)) {
					scheduler.resumeAll();
					out.println("<br/>全部Job已恢复");
				} else {
					String trgName = JobUtil.getTriggerName(tg);
					scheduler.resumeTrigger(trgName, JobUtil.QUARTZ_GROUP);
					out.println("<br/>job-" + trgName + " 已恢复");
				}
			} else {// 测试
				long delay = new Date().getTime() + 1000 * 5;

				JobUtil.pauseTrigger(delay, 6);

				out.println("测试结束");
			}
		}
	--%>

	<hr>
	<h2>全部Job&nbsp;&nbsp;</h2>
	<a href="job.jsp?op=pause">全暂停</a>
	<a href="job.jsp?op=resume">全启动</a>
	<a href="job.jsp?op=test">测试</a>
	<a href="job.jsp">刷新</a>
	<br />
	<br />
	<h2>
		抓图书Job#<%--=scheduler.getTriggerState("douBookTrigger",
					JobUtil.QUARTZ_GROUP)%>&nbsp;&nbsp;
	</h2>
	<a href="job.jsp?op=pause&trg=1">暂停</a>
	<a href="job.jsp?op=resume&trg=1">启动</a>
	<br />
	<br />
	<h2>
		抓导购Job#<%=scheduler.getTriggerState("douBuyLinkTrigger",
					JobUtil.QUARTZ_GROUP)%>&nbsp;&nbsp;
	</h2>
	<a href="job.jsp?op=pause&trg=2">暂停</a>
	<a href="job.jsp?op=resume&trg=2">启动</a>
	<br />
	<br />
	<h2>
		抓评列Job#<%=scheduler.getTriggerState("douReviewsTrigger",
					JobUtil.QUARTZ_GROUP)%>&nbsp;&nbsp;
	</h2>
	<a href="job.jsp?op=pause&trg=3">暂停</a>
	<a href="job.jsp?op=resume&trg=3">启动</a>
	<br />
	<br />
	<h2>
		抓评论Job#<%=scheduler.getTriggerState("douReviewdTrigger",
					JobUtil.QUARTZ_GROUP)%>&nbsp;&nbsp;
	</h2>
	<a href="job.jsp?op=pause&trg=4">暂停</a>
	<a href="job.jsp?op=resume&trg=4">启动</a>
	<br />
	<br />
	<h2>
		抓新书Job#<%=scheduler.getTriggerState("douLatestBookTrigger",
					JobUtil.QUARTZ_GROUP)%>&nbsp;&nbsp;
	</h2>
	<a href="job.jsp?op=pause&trg=5">暂停</a>
	<a href="job.jsp?op=resume&trg=5">启动</a>
	<br />
	<br />
	<h2>
		排行榜Job#<%=scheduler.getTriggerState("douChartBookTrigger",
					JobUtil.QUARTZ_GROUP)%>&nbsp;&nbsp;
	</h2>
	<a href="job.jsp?op=pause&trg=6">暂停</a>
	<a href="job.jsp?op=resume&trg=6">启动</a>
	<br />
	<br />
	<h2>
		抓标签Job#<%=scheduler.getTriggerState("douTagBookTrigger",
					JobUtil.QUARTZ_GROUP)--%>&nbsp;&nbsp;
	</h2>
	<a href="job.jsp?op=pause&trg=7">暂停</a>
	<a href="job.jsp?op=resume&trg=7">启动</a>
	<br />
	<br />
	<hr>
</body>
</html>