<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%--
	String USER = "";
	String PWD = "";
	String LOGIN_URL = "http://login.sina.com.cn/sso/login.php?client=ssologin.js(v1.3.19)";
	String LOGIN_WEIBO = "http://weibo.com/ajaxlogin.php?framelogin=1&callback=parent.sinaSSOController.feedBackUrlCallBack";

	DefaultHttpClient client = new DefaultHttpClient();
	client.getParams().setParameter("http.protocol.cookie-policy",
			CookiePolicy.BROWSER_COMPATIBILITY);
	client.getParams().setParameter(
			HttpConnectionParams.CONNECTION_TIMEOUT, 5000);
	try {
		System.out.println("准备登陆...");
		String data = SinaLogin.getServerTime();
		String nonce = SinaLogin.makeNonce(6);
		String su = SinaLogin.encodeAccount(USER);
		String sp = new SinaSSOEncoder().encode(PWD, data, nonce);

		HttpPost httpPost = new HttpPost(LOGIN_URL);
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("entry", "weibo"));
		nvps.add(new BasicNameValuePair("gateway", "1"));
		nvps.add(new BasicNameValuePair("from", ""));
		nvps.add(new BasicNameValuePair("savestate", "7"));
		nvps.add(new BasicNameValuePair("useticket", "1"));
		nvps.add(new BasicNameValuePair("ssosimplelogin", "1"));
		nvps.add(new BasicNameValuePair("su", su));
		nvps.add(new BasicNameValuePair("service", "miniblog"));
		nvps.add(new BasicNameValuePair("servertime", data));
		nvps.add(new BasicNameValuePair("nonce", nonce));
		nvps.add(new BasicNameValuePair("pwencode", "wsse"));
		nvps.add(new BasicNameValuePair("sp", sp));
		nvps.add(new BasicNameValuePair("url", LOGIN_WEIBO));
		nvps.add(new BasicNameValuePair("returntype", "META"));
		nvps.add(new BasicNameValuePair("encoding", "UTF-8"));
		nvps.add(new BasicNameValuePair("vsnval", ""));

		httpPost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));

		HttpResponse resp = client.execute(httpPost);
		String entity = EntityUtils.toString(resp.getEntity());
		if (entity.indexOf("retcode=0") == -1) {
			System.out.println("登陆失败！");
			session.removeAttribute("sinaWeiboHttp");
			return;
		}

		// 获取到实际url进行连接
		String url = entity.substring(entity
				.indexOf("http://weibo.com/ajaxlogin.php?"), entity
				.indexOf("code=0") + 6);
		//System.out.println("url=" + url);
		HttpGet httpGet = new HttpGet(url);
		resp = client.execute(httpGet);
		// 释放response
		entity = EntityUtils.toString(resp.getEntity());
		System.out.println("新浪微博，模拟登陆成功");

		session.setAttribute("sinaWeiboHttp", client);

		response.sendRedirect("sina_user.jsp");
	} catch (Exception e) {
		e.printStackTrace();
	}
--%>