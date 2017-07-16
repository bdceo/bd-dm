<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>手动关注</title>
</head>
<%--
	IWeiboUserDao iwu = LogicFactory.getWeiboUserDao(); 
	
	String sql = "select * from weibo_user where user_type=4 and date(add_time)='2012-10-29'"
		+" and wb>100 and gz>10 and id>=13597 order by id asc";  // 关注id>12589
	List<WeiboUser> wus = iwu.findBySQL(sql, new WeiboUser()); 
--%>
<body>
	<%--   for(WeiboUser u : wus){%>
		 	<%=u.getId()%>：&nbsp;&nbsp;
		 	<a href="<%=u.getUserHome()%>" target="_blank"><%=u.getUserName()%></a>
			&nbsp;&nbsp;粉丝=<%=u.getFs()%>,关注=<%=u.getGz()%>,微博=<%=u.getWb()%><br/><br/>
		<%}
	--%>
</body>
</html>