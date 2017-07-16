<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%--
	String USER = "";
	String PWD = "";
	DefaultHttpClient client = new DefaultHttpClient();
	client.getParams().setParameter("http.protocol.cookie-policy",
			CookiePolicy.BROWSER_COMPATIBILITY);
	client.getParams().setParameter(
			HttpConnectionParams.CONNECTION_TIMEOUT, 5000);
	try { 
		System.out.println("腾讯微博模拟登陆...");
		HttpGet get = new HttpGet(
				"http://check.ptlogin2.qq.com/check?uin=" + USER
						+ "&appid=46000101&ptlang=2052&r="
						+ Math.random());
		get.setHeader("Host", "check.ptlogin2.qq.com");
		get.setHeader("Referer", "http://t.qq.com/?from=11");

		get
				.setHeader("User-Agent",
						"Mozilla/5.0 (Windows NT 5.1; rv:13.0) Gecko/20100101 Firefox/13.0");
		HttpResponse resp = client.execute(get);

		String entity = EntityUtils.toString(resp.getEntity());

		String[] checkNum = entity.substring(entity.indexOf("(") + 1,
				entity.lastIndexOf(")")).replace("'", "").split(",");
		String pass = ""; 

		pass = QQmd5.GetPassword(checkNum[2].trim(), PWD, checkNum[1]
				.trim());
		/************************* 登录 ****************************/
		get = new HttpGet(
				"http://ptlogin2.qq.com/login?ptlang=2052&u="
						+ USER
						+ "&p="
						+ pass
						+ "&verifycode="
						+ checkNum[1]
						+ "&low_login_enable=1&low_login_hour=720&aid=46000101&u1=http%3A%2F%2Ft.qq.com&ptredirect=1&h=1&from_ui=1&dumy=&fp=loginerroralert&action=1-2-11125&g=1&t=1&dummy=");
		get.setHeader("Connection", "keep-alive");
		get.setHeader("Host", "ptlogin2.qq.com");
		get.setHeader("Referer", "http://t.qq.com/?from=11");
		get
				.setHeader("User-Agent",
						"Mozilla/5.0 (Windows NT 5.1; rv:13.0) Gecko/20100101 Firefox/13.0");
		resp = client.execute(get);
		entity = EntityUtils.toString(resp.getEntity());
		System.out.println("登陆返回：" + entity);
		if (entity.indexOf("登录成功") > -1) {
			get = new HttpGet("http://t.qq.com");
			resp = client.execute(get);
			entity = EntityUtils.toString(resp.getEntity());
			Document doc = Jsoup.parse(entity);
			Element es = doc.getElementById("topNav1");
			String displayName = "";
			if (es != null) {
				Elements e = es.getElementsByTag("u");
				if (e != null && e.size() > 0)
					displayName = e.get(0).text();
			}

			session.setAttribute("qqWeiboHttp", client);
			response.sendRedirect("qq_weibo.jsp");
		}
	} catch (Exception e) {
		e.printStackTrace();
	} 
--%>