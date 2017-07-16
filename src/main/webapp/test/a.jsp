<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<%--
		IWebsiteDao iwd = LogicFactory.getWebsiteDao();

		String op = request.getParameter("op");
		String id = request.getParameter("id");
		if (!StringUtil.isEmpty(op)) {
			if ("mgscl".equals(op)) {
				WebFrommmgscl123.parseA();
			} else if ("del".equals(op)) {
				Website tmp = iwd.findById(Integer.parseInt(id));
				tmp.setStat("0");
				iwd.update(tmp);
			} else if ("v".equals(op)) {
				VideoFromxxx880.PAGE = 1;
				VideoFromxxx880.TOTAL = 49;
				VideoFromxxx880.FETCH_URL = VideoFromxxx880.V_LUANLUN;
				VideoFromxxx880.FETCH_CAT = VideoUtil.CAT_LUANLUN;

				VideoFromxxx880.fetchBase(); 
				VideoFromxxx880.fetchIntro();
				VideoFromxxx880.fetchVideo();
			}
		}

		List<Website> ws = iwd.findByProperty("stat", "1");
		int size = ws.size();
	--%>
	<a href="a.jsp?op=mgscl">抓mgscl</a>
	<a href="a.jsp?op=v">抓Video</a>
	<hr />
	<table border="1px">
		<%--
			for (int i = 0; i < size - 7;) {
				Website w1 = ws.get(i);
				Website w2 = ws.get(i + 1);
				Website w3 = ws.get(i + 2);
				Website w4 = ws.get(i + 3);
				Website w5 = ws.get(i + 4);
				Website w6 = ws.get(i + 5);
				Website w7 = ws.get(i + 6);
				i += 7;
		%>
		<tr>
			<td><a href="<%=w1.getWebsite()%>" target="_blank"><%=w1.getName()%></a>
				&nbsp; <a href="a.jsp?op=del&id=<%=w1.getId()%>">DEL</a></td>
			<td><a href="<%=w2.getWebsite()%>" target="_blank"><%=w2.getName()%></a>
				&nbsp; <a href="a.jsp?op=del&id=<%=w2.getId()%>">DEL</a></td>
			<td><a href="<%=w3.getWebsite()%>" target="_blank"><%=w3.getName()%></a>
				&nbsp; <a href="a.jsp?op=del&id=<%=w3.getId()%>">DEL</a></td>
			<td><a href="<%=w4.getWebsite()%>" target="_blank"><%=w4.getName()%></a>
				&nbsp; <a href="a.jsp?op=del&id=<%=w4.getId()%>">DEL</a></td>
			<td><a href="<%=w5.getWebsite()%>" target="_blank"><%=w5.getName()%></a>
				&nbsp; <a href="a.jsp?op=del&id=<%=w5.getId()%>">DEL</a></td>
			<td><a href="<%=w6.getWebsite()%>" target="_blank"><%=w6.getName()%></a>
				&nbsp; <a href="a.jsp?op=del&id=<%=w6.getId()%>">DEL</a></td>
			<td><a href="<%=w7.getWebsite()%>" target="_blank"><%=w7.getName()%></a>
				&nbsp; <a href="a.jsp?op=del&id=<%=w7.getId()%>">DEL</a></td>
		</tr>
		<%
			}
		--%>
	</table>
</body>
</html>