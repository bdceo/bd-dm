package com.bdsoft.datamin.fetch.jd.feed;

/**
 * 从抓取的评论json中提取user有用信息
 * 
 * @author 丁辰叶
 * @date 2015-11-4
 */
public class JDUserFeed {

	private String uid;
	private String nickname;
	private String province;
	private String ip;
	private String levelId;
	private String levelName;
	private String regTime;

	@Override
	public String toString() {
		return "用户 [" + nickname + "," + province + "," + levelName + "]";
	}

	public JDUserFeed(JDJsonComments json) {
		this.uid = json.getGuid();
		this.nickname = json.getNickname();
		this.ip = json.getUserIp();
		this.province = json.getUserProvince();
		this.levelId = json.getUserLevelId();
		this.levelName = json.getUserLevelName();
		this.regTime = json.getUserRegisterTime();
	}

	public JDUserFeed() {
		super();
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getLevelId() {
		return levelId;
	}

	public void setLevelId(String levelId) {
		this.levelId = levelId;
	}

	public String getLevelName() {
		return levelName;
	}

	public void setLevelName(String levelName) {
		this.levelName = levelName;
	}

	public String getRegTime() {
		return regTime;
	}

	public void setRegTime(String regTime) {
		this.regTime = regTime;
	}

}
