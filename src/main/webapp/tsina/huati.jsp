<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>话题</title>
</head>
<%--
	IWeiboUserDao iwu = LogicFactory.getWeiboUserDao();

	AccessToken token = (AccessToken) session.getAttribute("token");
	User user = (User) session.getAttribute("tuser");
	// 关系
	Friendships fm = new Friendships();
	fm.client.setToken(token.getAccessToken());
	
	// 话题
	Trend tm = new Trend();
	tm.client.setToken(token.getAccessToken());
	
	List<Trends> trends = tm.getTrendsHourly();
	for(Trends ts : trends){
		//System.out.println(ts.toString());
		System.out.println(ts.getAsOf().toLocaleString());
		for(weibo4j.model.Trend t : ts.getTrends()){
			System.out.println(t.getName()+"_"+t.getAmount());
		}
	}
	 
--%>
<body>

</body>
</html>