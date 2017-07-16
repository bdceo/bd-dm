<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>新浪微博 抓取用户</title>
</head>
<%--
	DefaultHttpClient client = (DefaultHttpClient) session.getAttribute("sinaWeiboHttp");
	if(client == null){
		response.sendRedirect("sina_login.jsp");
		return;
	}
	String msg = "";
	request.setCharacterEncoding("utf-8");
	String op = request.getParameter("op");
	String key = request.getParameter("key"); 
	request.setAttribute("key", key); 
	if(op != null){
		if("ser".equals(op) && key != null ){
			//SinaUser.fetch(key, 1, 0);
			SinaUser.fetchWithLogin(key, client);
			msg = "关键词【"+key+"】抓取完毕 " + new Date().toLocaleString();
			out.println(Assert.AlertMessage(msg, "sina_user.jsp"));
		}  
	}
--%>
<body>
	<h3>用户搜索抓取</h3>
	<form action="sina_user.jsp" method="post">
		<input type="hidden" name="op" value="ser">
		关键词：<input type="text" name="key" value="${key}">&nbsp;&nbsp;
		<input type="submit" value="开 始 吧">
	</form>
	<hr/>
</body>
</html>