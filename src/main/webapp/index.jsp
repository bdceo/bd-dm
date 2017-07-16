<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>控制台</title>
</head>
<%
/*
	Map<String, List<String>> keyMap = new HashMap<String, List<String>>();
	IWeiboFetchDao imw = LogicFactory.getWeiboFetchDao();
	String sql = "select fetch_key,count(*),fetch_src from weibo_fetch where date(fetch_date)=curdate()"
			+ " group by fetch_key,fetch_src ";
	List<Object> objs = imw.findBySQL(sql);
	System.out.println("今日累计抓取关键词：" + objs.size());
	for (Object o : objs) {
		//String t = StringUtil.nullToEmpty(o);
		//keys.add(t);

		Object[] oo = (Object[]) o;
		String t = StringUtil.nullToEmpty(oo[0]);
		Integer dc = ParseUtil.getIntValue(oo[1]);
		String s = StringUtil.nullToEmpty(oo[2]);
		List<String> tmp = keyMap.get(s);
		if (tmp == null) {
			tmp = new ArrayList<String>();
		}
		tmp.add(t);
		keyMap.put(s, tmp);
	}
	for (Map.Entry en : keyMap.entrySet()) {
		System.out.println(en.getKey() + ":"
				+ ((List<String>) en.getValue()).size());
	}
	session.setAttribute("keyCount", keyMap);

	String[] k1 = new String[] { "急招", "招聘", "高薪", "招人" };
	int s1 = k1.length;

	String[] k2 = new String[] { " ", "技术", "总监", "经理", "工程师", "架构师",
			"主管", "程序员", "前端", ".net", "python", "硬件", "java", "php",
			"C#", "C++", "DBA", "运维", "产品经理", "设计师", "北京", "上海", "深圳",
			"广州", "杭州", "成都", "武汉", "南京", "长沙" };
	int s2 = k2.length;

	String[] k3 = new String[] { " ", "简历", "邮箱", "mail" };
	int s3 = k3.length;

	Map<String, Long> allKey = null;
	Object obj = session.getAttribute("keyMap");
	if (obj == null) {
		allKey = new HashMap<String, Long>();
		for (int a = 0; a < s1; a++) {
			for (int b = 0; b < s2; b++) {
				for (int c = 0; c < s3; c++) {
					StringBuffer sb = new StringBuffer();
					sb.append(k1[a]);
					sb.append(" " + k2[b]);
					sb.append(" " + k3[c]);
					allKey.put(sb.toString(), new Long(0));
				}
			}
		}
		System.out.println("关键词初始化完毕" + allKey.size());
		session.setAttribute("keyMap", allKey);
	} else {
		allKey = (Map<String, Long>) obj;
	}
	System.out.println("关键词总数：" + allKey.size());
	*/
%>
<body>
	<img src="img/sina_logo.png" border="0" />
	<br />
	<br />
	<a href="sina_weibo.jsp">抓取邮箱</a> &nbsp;&nbsp;
	<a href="sina_weibo_a.jsp">自动抓取邮箱</a> &nbsp;&nbsp;
	<a href="sina_user.jsp">抓取用户</a> &nbsp;&nbsp;
	<a href="sina_login.jsp">登录微博</a>
	<br />
	<br />
	<a href="sina_login.jsp">授权登录</a> &nbsp;&nbsp;
	<a href="tsina/api.jsp">接口测试</a>
	<hr />
	<img src="img/qq_logo.png" border="0"></img>
	<br />
	<br />
	<a href="qq_weibo.jsp">抓取邮箱</a> &nbsp;&nbsp;
	<a href="qq_weibo_a.jsp">自动抓取邮箱</a> &nbsp;&nbsp;
	<a href="qq_login.jsp">登录微博</a>
	<hr />
	<img src="img/dou_logo.png" border="0"></img>
	<br />
	<br />
	<a href="book/book.jsp">抓取图书</a> &nbsp;&nbsp;
	<a href="book/book.jsp?op=new">新书推荐</a> &nbsp;&nbsp;
	<a href="book/book.jsp?op=chart">上榜图书</a> &nbsp;&nbsp;
	<a href="book/count.jsp">抓取统计</a> &nbsp;&nbsp;
	<a href="book/lucene.jsp">建索引</a> &nbsp;&nbsp;
	<a href="book/search.jsp">搜索</a> &nbsp;&nbsp;
	<hr />
	<h3>辅助</h3>
	<a href="stat/count.jsp">邮箱统计</a> &nbsp;&nbsp;
	<a href="sina_weibo.jsp?op=export">导出当天邮箱</a> &nbsp;&nbsp;
	<a href="mail.jsp?op=export">fetchTOmail</a> &nbsp;&nbsp;
	<a href="mail.jsp?op=send">sendMail</a> &nbsp;&nbsp;
	<a href="fetch.jsp?op=fetch_tmp">FetchWWWTmp</a>&nbsp;&nbsp;
	<a href="fetch.jsp?op=fetch_admen">FetchAdmen</a>
	<hr />
	<h3>Quartz-控制</h3>
	<a href="job.jsp?op=pause">暂停-所有</a> &nbsp;&nbsp;
	<a href="job.jsp?op=resume">恢复-所有</a> &nbsp;&nbsp;
	<a href="job.jsp?op=ceshi">测试</a> &nbsp;&nbsp;
</body>
</html>