 <%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%> 
<%--
	// 新浪微博接口测试
	// bdceo:1599959304  bdcoo:1194869670
	AccessToken token = (AccessToken) session.getAttribute("token");
	User user = (User) session.getAttribute("tuser");
	
	if(token == null || user == null){
		response.sendRedirect("../login.jsp");
	}
	String access_token = token.getAccessToken();
	
	// 获取指定用户最近发的微博
	Timeline tm = new Timeline();
	tm.client.setToken(access_token);
	//StatusWapper status = tm.getUserTimeline();
	//StatusWapper status = tm.getUserTimelineByUid("1599959304");
	StatusWapper status = tm.getUserTimelineByName("bdceo");
	for(Status s : status.getStatuses()){
		//System.out.println(s.toString());
		//System.out.println(s.getId()+"_"+s.getText());
	}
	/*System.out.println(status.getNextCursor());
	System.out.println(status.getPreviousCursor());
	System.out.println(status.getTotalNumber());
	System.out.println(status.getHasvisible());*/

	String id = status.getStatuses().get(1).getId();
	// 自动评论某条微博
	System.out.println(id);
	Comments cm = new Comments();
	cm.client.setToken(access_token); 
	//Comment comment = cm.createComment("方向很重要，执行更重要", id);
	//System.out.println(comment.toString()); 

	// 自动转发某条微博
	//Status st = tm.Repost(id);
	//System.out.println(st.toString());
	
	// 获取用户详情
	Users um = new Users();
	um.client.setToken(access_token);
	User u1 = um.showUserById("1599959304");
	//System.out.println(u1.getName()+"_"+u1.getDescription());
	//System.out.println("粉丝"+u1.getFollowersCount()+",关注"+u1.getFriendsCount()+",微博"+u1.getStatusesCount());

	// 微博精选推荐
	Suggestion suggestion = new Suggestion();
	suggestion.client.setToken(token.getAccessToken());
	//微博精选分类，1：娱乐、2：搞笑、3：美女、4：视频、5：星座、6：各种萌、7：时尚、8：名车、9：美食、10：音乐。
	//是否返回图片精选微博，0：全部、1：图片微博。
	StatusWapper sugs = suggestion.suggestionsStatusesHot(5, 1);
	for(Status s : sugs.getStatuses()){
		//System.out.println(s);
		//System.out.println(s.getUser().getScreenName()+" 说：\n内容："+s.getText() +"\n转发："
		//+s.getRepostsCount()+"评论："+s.getCommentsCount());
	}
	
	// 我的首页100条微博
	status = tm.getFriendsTimeline();
	System.out.println("返回微博："+status.getStatuses().size());
	for(Status s : status.getStatuses()){
		User u = s.getUser();
		//System.out.println(u.getScreenName()+"_" + u.getVerifiedReason());
		//System.out.println("\t"+s.getText());
	}
	
	out.println("新浪微博接口测试");
	out.println("OK！" + new Date().toLocaleString());
--%> <br>
<a href="#">接口测试</a><br><br>
<a href="gz.jsp">自动关注</a>&nbsp;&nbsp;<a href="gz2.jsp">手动关注</a><br><br>
<a href="qxgz.jsp">取消关注</a><br><br>
<a href="at.jsp">自动[AT]</a><br><br>
