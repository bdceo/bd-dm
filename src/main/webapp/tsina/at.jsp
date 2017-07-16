<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>自动转发并@用户</title>
</head>
<%--
	IWeiboUserDao iwu = LogicFactory.getWeiboUserDao();

	AccessToken token = (AccessToken) session.getAttribute("token");
	User user = (User) session.getAttribute("tuser");
	List<User> hus = (List<User>) session.getAttribute("hus");
	weibo4j.model.Trend[] hotts = (weibo4j.model.Trend[]) session.getAttribute("hotts"); 
	// 关系
	Friendships fm = new Friendships();
	fm.client.setToken(token.getAccessToken());
	
	// 微博
	Timeline tm = new Timeline();
	tm.client.setToken(token.getAccessToken());
	
	// 微博精选推荐
	Suggestion suggestion = new Suggestion();
	suggestion.client.setToken(token.getAccessToken());
	//微博精选分类，1：娱乐、2：搞笑、3：美女、4：视频、5：星座、6：各种萌、7：时尚、8：名车、9：美食、10：音乐。
	//是否返回图片精选微博，0：全部、1：图片微博。
	StatusWapper sugs = suggestion.suggestionsStatusesHot(1, 1);
	for(Status s : sugs.getStatuses()){
		//System.out.println(s);
		//System.out.println(s.getUser().getScreenName()+" 说：\n内容："+s.getText() +"\n转发："
		//+s.getRepostsCount()+"评论："+s.getCommentsCount());
	}
	
	// 热门收藏
	JSONArray ja = suggestion.suggestionsFavoritesHot();
	int size = ja.length();
	int index = new Random().nextInt(size);
	// 随机获取转发热门收藏
	JSONObject jo = ja.getJSONObject(index);
	String mid = jo.getString("mid");
	
	// 获取我的关注列表
	size = hus.size();
	if(size > 10){
		size = 10;
	}
	StringBuffer sb = new StringBuffer("#猎头助手# @bdceo ");
	sb.append("#" + hotts[index].getName() + "#");
	for(int i=0; i<size; i++){
		index = new Random().nextInt(hus.size());
		String n = hus.get(index).getScreenName();
		if(sb.toString().indexOf("@"+n) == -1){
			sb.append(" @" + n);
		}
	}
	index = new Random().nextInt(hotts.length);
	//tm.Repost(mid);
	tm.Repost(mid, sb.toString(), 0);
	System.out.println("热门话题转发完毕");
	Thread.sleep(1000 * 30);
	
	// 我的首页100条微博自动转
	StatusWapper status = tm.getFriendsTimeline();
	for(Status s : status.getStatuses()){
		User u = s.getUser();
		if(u.getVerifiedReason().contains("猎头")){
			String con = "@" + u.getScreenName() +" "+ s.getText();
			try{
				tm.Repost(s.getId(), con, 0);
				//tm.Repost(s.getId());
			}catch(Exception e){
				System.out.println(e.getMessage());
				continue;
			}
			Thread.sleep(1000 * 60);
		}
	}
	
	out.println("OK！" + new Date().toLocaleString());
--%>
<body>

</body>
</html>