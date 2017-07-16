<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%--
	Oauth oauth = new Oauth();
	String code = request.getParameter("code");
 	System.out.println("Code [" + code + "]");
 	if (code != null) { 
		session.setAttribute("tcode", code);		
	 	AccessToken token = oauth.getAccessTokenByCode(code);
		session.setAttribute("token",token); 
	 	System.out.println(token.toString());
		
		if (token != null) {
		 	String acc = token.getAccessToken();
		 	String uid = token.getUid();
		 	
	byte[] img = NetTool.getImage("http://www.wlun.cn/img/tlogo.jpg");
	ImageItem wii = new ImageItem(img);
	
	Users um = new Users();
	um.client.setToken(acc);
	User user = um.showUserById(uid);
	session.setAttribute("tuser",user); 
		    System.out.println(user.toString());
		      
	Timeline tl = new Timeline(acc);
	String w = "我刚登陆了新浪微博应用：#猎头助手#V2.0 猎头顾问的好帮手. 推荐大家关注和体验一下. http://wlun.cn/tsina " 
	+ new Date().toLocaleString();
	//Status st = tl.UpdateStatus(w);	
	w = URLEncoder.encode(w, "UTF-8");
	Status st = tl.UploadStatus(w, wii);
	System.out.println("发布微博" + st.toString());
	
	// 获取我的关注列表所有粉丝超过500的用户
	List<User> hus = new ArrayList<User>();
	// 关系接口
	Friendships fm = new Friendships();
	fm.client.setToken(token.getAccessToken());

	int count = 200;
	int cursor = 0;
	int i = 0;
	UserWapper users = fm.getFriendsByID(uid, count, cursor);
	while(users.getUsers().size() > 0){
		//System.out.println(users.getNextCursor()+"_"+users.getPreviousCursor()+"_"+users.getTotalNumber());
		for(User u : users.getUsers()){
	//System.out.println("第" + (i+1) + "页 用户 " + u.getScreenName()+ " " + u.getUrl());
	// 获取我的关注人中粉丝超过500的用户
	if(u.getFollowersCount() > 100 && u.getStatusesCount()>100){
		hus.add(u);
	}
		}
		users.getUsers().clear();
		if(users.getNextCursor()>0){
	users = fm.getFollowersById(user.getId(), count, (int)users.getNextCursor());
	i++;
		}
	}
	session.setAttribute("hus", hus);
	System.out.println("我的关注人中粉丝超过500的已存储" + hus.size());

	// 获取当日热门话题存储 
	Trend td = new Trend(); 
	td.client.setToken(token.getAccessToken());
	//List<Trends> trends = td.getTrendsHourly();
	List<Trends> trends = td.getTrendsDaily();
	Trends ts = trends.get(0); 
	System.out.println("热门话题" + ts.getAsOf().toLocaleString());
	for(weibo4j.model.Trend t : ts.getTrends()){
	//	System.out.println(t.getName());
	}
	session.setAttribute("hotts", ts.getTrends());
	System.out.println("今日热门话题" + ts.getTrends().length);
	
	response.sendRedirect("api.jsp");
		}else{
	System.out.println("授权失败");
	response.sendRedirect("../index.jsp");
		}
 	}
--%>