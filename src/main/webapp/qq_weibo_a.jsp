<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%><html>
<!DOCTYPE html>
<head>
<meta charset="utf-8" />
<script type="text/javascript" src="js/jquery.min1.8.2.js"></script>
<title>新浪微博 自动抓取邮箱</title>
</head>
<%--
	DefaultHttpClient client = (DefaultHttpClient) session
			.getAttribute("qqWeiboHttp");
	if (client == null) {
		response.sendRedirect("qq_login.jsp");
		return;
	}
	Map<String, List<String>> keyCount = (Map<String, List<String>>) session
			.getAttribute("keyCount");
	Map<String, Long> keyMap = (Map<String, Long>) session
			.getAttribute("keyMap");
	if (keyCount == null || keyMap == null) {
		response.sendRedirect("index.jsp");
		return;
	}

	List<String> keys = keyCount.get(QQWeibo.FETCH_SRC);
	if(keys == null){
		keys = new ArrayList<String>();
	}
	int i = 0;
	System.out.println("关键词总数：" + keyMap.size());
	for (Map.Entry<String, Long> en : keyMap.entrySet()) {
		i++;
		String key = en.getKey();
		System.out.println("抓取第" + i + "个关键词： \t" + key);

		if (keys.contains(key)) {
			System.out.println("今天抓过了");
			continue;
		} else {
			keys.add(key);
			try {
				QQWeibo.fetch(key, client);
				System.out.println("关键词【" + key + "】抓取完毕 "
						+ new Date().toLocaleString());
				System.out.print("换关键词倒计时-->");
				for (int j = 15; j > 0; j--) {
					System.out.print(j + " ");
					Thread.sleep(1000);
				}
				System.out.println("");
			} catch (Exception e) {
				if (e.getMessage().equals("ip")) {
					System.out.println("ip 被封，暂停抓取！"
							+ new Date().toLocaleString());
					out.println("ip 被封，暂停抓取！"
							+ new Date().toLocaleString());
					break;
				}
			}
		}
	}
	keyCount.put(QQWeibo.FETCH_SRC, keys);
	session.setAttribute("keyCount", keyCount);
--%>
<body>
<h3>自动抓取 新浪微博 招聘邮箱</h3>
</body>
</html>