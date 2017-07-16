<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%><html>
<!DOCTYPE html>
<head>
<meta charset="utf-8"/>
<script type="text/javascript" src="js/jquery.min1.8.2.js"></script>
<title>新浪微博 抓取邮箱</title>
</head>
<%--
	String msg = "";
	request.setCharacterEncoding("utf-8");
	String op = request.getParameter("op");
	String key = request.getParameter("key"); 
	request.setAttribute("key", key);
	//String pg = request.getParameter("page"); 
	//request.setAttribute("pg", pg);
	if(op != null){
		if("ser".equals(op) && key != null ){
			SinaWeibo.fetch(key, 0, 0);
			msg = "关键词【"+key+"】抓取完毕 " + new Date().toLocaleString();
			out.println(Assert.AlertMessage(msg, "sina_weibo.jsp"));
		}else if("export".equals(op)){
			SinaWeibo.exportDB();
			response.sendRedirect("index.jsp");
			return;
		}else if("import".equals(op)){
			SinaWeibo.importAll();
		}else if("parseuser".equals(op)){
			SinaWeibo.parseUser();
		}else if("update".equals(op)){
			SinaWeibo.updateRN(); 
		}
	}
	// 当日搜索关键词统计
	IWeiboFetchDao imw = LogicFactory.getWeiboFetchDao();
	String sql = "select fetch_key,count(*) from weibo_fetch where date(fetch_date)=curdate() group by fetch_key";
	List<Object> objs = imw.findBySQL(sql);
	
--%>
<body>
	<h3>抓取 新浪微博 招聘邮箱</h3>
	<form action="sina_weibo.jsp" method="post">
		<input type="hidden" name="op" value="ser">
		关键词：<input type="text" name="key" value="${key}"/>
		<!-- <br>总页数：<input type="text" name="page" size="1" value="${pg}" id="pg"/>
		<input type="range" min="0" max="50" step="1" value="${pg}" onchange="$('#pg').val(this.value);"/> -->
		<br/><input type="submit" value="开 始 吧">
	</form><hr/>
	<%-- Integer sum = 0;
	for(Object o : objs){
		Object[] oo = (Object[])o;
		String t = StringUtil.nullToEmpty(oo[0]);
		Integer dc = ParseUtil.getIntValue(oo[1]);
		sum += dc;%>
		关键词：<%=t%>，新邮箱：<%=dc%><br/>
	<%}%><br/>
	<b>今天共抓取新邮箱：<%=sum--%>个</b><br/>
	<hr/> 
	 <hr/> 
	<!-- <form action="sina_weibo.jsp" method="post">
		<input type="hidden" name="op" value="import">
		<input type="submit" value="导 入 所 有">
	</form>
	<hr/> 
	<form action="sina_weibo.jsp" method="post">
		<input type="hidden" name="op" value="parseuser">
		<input type="submit" value="解 析 用 户">
	</form> 
	<hr/> 
	<form action="sina_weibo.jsp" method="post">
		<input type="hidden" name="op" value="update">
		<input type="submit" value="更新">
	</form> 
	 -->
</body>
</html>