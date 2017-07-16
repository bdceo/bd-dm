<%@page import="com.bdsoft.datamin.entity.DouBook"%>
<%@page import="java.util.List"%>
<%@page import="com.bdsoft.datamin.entity.DouFetchQueue"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="java.math.BigInteger"%>
<%@page import="java.util.Date"%>
<%@page import="com.bdsoft.datamin.util.StringUtil"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>   
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>豆瓣抓取</title>
</head>
<body>
	<%
	/*	DoubanController action = DoubanController.getInstance();

			String url = request.getParameter("url");
			if (StringUtil.isEmpty(url)) {
		url = "http://book.douban.com/subject/5375620/";
			}

			// 操作
			action.fetchBook(url);   
			//action.fetchBuyLinks();
			//action.fetchReviews();
			//action.fetchReview(); 
			//action.fetchBooks();
			//action.fetchTags();
			//action.fetchTagBooks();

			String op = request.getParameter("op");
			if (StringUtil.isNotEmpty(op)) {
		if (op.equals("new")) {
			action.fetchLatestBooks();
		} else if (op.equals("chart")) {
			action.fetchChartBooks();
		}
			}
			
			// 测试数据库null
			// IDouBookDao dao = LogicFactory.getDouBookDao();
			// DouBook book = dao.findById(444);
			// out.println(book.toString());

			String date = new Date().toLocaleString();  */
	%>
	<hr />
	时间:&nbsp;<%=/*date*/%><br />
	<form action="book.jsp">
		地址：<input type="text" name="url" size="50" />&nbsp;&nbsp; <input
			type="submit" value="走 着 ...">
	</form>

</body>
</html>