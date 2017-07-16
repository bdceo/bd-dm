<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>自动取消关注</title>
</head>
<%--
	IWeiboUserDao iwu = LogicFactory.getWeiboUserDao();

	AccessToken token = (AccessToken) session.getAttribute("token");
	User user = (User) session.getAttribute("tuser");
	// 关系接口
	Friendships fm = new Friendships();
	fm.client.setToken(token.getAccessToken());
	int count = 30;
	int cursor = 0;
	
	// 粉丝
	UserWapper users = fm.getFollowersById(user.getId(), count, cursor);
	//UserWapper users = fm.getFollowersById(user.getId());
	
	// 关注
	//UserWapper users = fm.getFriendsByID(user.getId());

	// 分页遍历
	int i = 0;
	while(users.getUsers().size() > 0){ 
		System.out.println(users.getNextCursor());
		System.out.println(users.getPreviousCursor());
		System.out.println(users.getTotalNumber());
		for(User u : users.getUsers()){
			System.out.println("第" + (i+1) + "页 用户 " + u.getScreenName()+ " " + u.getUrl());
			try{
				// 取消关注微博数小于100的用户
				if(u.getStatusesCount()<100){
					//fm.destroyFriendshipsDestroyById(u.getId());
					//System.out.println("取消关注");
				}
			}catch(Exception e){
				System.out.println(e.getMessage());
				continue;
			}
		}
		users.getUsers().clear();
		if(users.getNextCursor()>0){
			users = fm.getFollowersById(user.getId(), count, (int)users.getNextCursor());
			i++;
		}
	}
	out.println("OK！" + new Date().toLocaleString());
--%>
<body>

</body>
</html>