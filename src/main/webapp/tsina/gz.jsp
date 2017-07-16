<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>自动关注</title>
</head>
<%--
	IWeiboUserDao iwu = LogicFactory.getWeiboUserDao();

	AccessToken token = (AccessToken) session.getAttribute("token");
	User user = (User) session.getAttribute("tuser");
	// 关系接口
	Friendships fm = new Friendships();
	fm.client.setToken(token.getAccessToken());
	
	String sql = "select * from weibo_user where user_type=4 and date(add_time)=curdate()"
		+" and wb>100 and gz>10 order by id asc";
	List<WeiboUser> wus = iwu.findBySQL(sql, new WeiboUser());

	StringBuffer sb = new StringBuffer();
	// 自动关注
	int n = 0, d = 0;
	for(WeiboUser u : wus){
		try{
			fm.createFriendshipsById(u.getUserId());
			System.out.println("关注了：" + u.getUserName());
			n++;
			Thread.sleep(1000 * 60);
		}catch(Exception e){
			System.out.println("已关注：" + u.getUserName());
			d++;
			e.printStackTrace();
			continue;
		}
	}

	out.println("OK！新关注：" + n + "个" + new Date().toLocaleString());
--%>
<body>

</body>
</html>