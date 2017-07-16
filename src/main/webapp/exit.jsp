<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	session.removeAttribute("tuser");
	session.removeAttribute("token");
	
	session.removeAttribute("sinaWeiboHttp");
	session.removeAttribute("qqWeiboHttp");
	
	response.sendRedirect("index.jsp");
%>