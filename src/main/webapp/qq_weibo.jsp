<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%><html>
<!DOCTYPE html>
<head>
<meta charset="utf-8" />
<script type="text/javascript" src="js/jquery.min1.8.2.js"></script>
<title>腾讯微博 抓取邮箱</title>
</head>
<%--
	String msg = "";
	DefaultHttpClient client = (DefaultHttpClient) session
			.getAttribute("qqWeiboHttp");
	if (client == null) {
		response.sendRedirect("qq_login.jsp");
		return;
	}
	request.setCharacterEncoding("utf-8");
	String op = request.getParameter("op");
	String key = request.getParameter("key");
	request.setAttribute("key", key);
	if (op != null) {
		if ("ser".equals(op) && key != null) {
			QQWeibo.fetch(key, client);
			msg = "关键词【" + key + "】抓取完毕 " + new Date().toLocaleString();
			out.println(Assert.AlertMessage(msg, "qq_weibo.jsp"));
		}
	}
	// 当日搜索关键词统计
	IWeiboFetchDao imw = LogicFactory.getWeiboFetchDao();
	String sql = "select fetch_key,count(*) from weibo_fetch where date(fetch_date)=curdate()" 
		+" and fetch_src='"+QQWeibo.FETCH_SRC+"' group by fetch_key";
	List<Object> objs = imw.findBySQL(sql);
--%>
<body>
<h3>抓取 腾讯微博 招聘邮箱</h3>
<form action="qq_weibo.jsp" method="post"><input type="hidden"
	name="op" value="ser"> 关键词：<input type="text" name="key"
	value="${key}" /> <br />
<input type="submit" value="开 始 吧"></form>
<hr />
<%--
	Integer sum = 0;
	for (Object o : objs) {
		Object[] oo = (Object[]) o;
		String t = StringUtil.nullToEmpty(oo[0]);
		Integer dc = ParseUtil.getIntValue(oo[1]);
		sum += dc;
%>
关键词：<%=t%>，新邮箱：<%=dc%><br />
<%
	}
%><br />
<b>今天共抓取新邮箱：<%=sum--%>个</b> 
</br></br>
<a href="qq_weibo_a.jsp">自动抓取邮箱</a>
</body>
</html>