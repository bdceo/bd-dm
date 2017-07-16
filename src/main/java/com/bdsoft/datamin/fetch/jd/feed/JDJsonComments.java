package com.bdsoft.datamin.fetch.jd.feed;

/**
 * 原始抓取的评论列表json内容
 * 
 * @author	丁辰叶
 * @date	2015-11-4
 */
public class JDJsonComments {

	private Long id;// 评论ID
	private String score;// 评分
	private String content; // 心得
	private String creationTime; // 评论日期
	private String referenceTime;// 购买日期

	private String uid;// 用户ID
	private String guid;// 用户ID-加密后
	private String nickname; // 用户名
	private String userIp; // 用户IP
	private String userLevelId;// 用户等级
	private String userLevelName; // 等级描述
	private String userProvince;// 用户所在地
	private String userRegisterTime;// 用户注册时间

	public JDJsonComments() {
	}

	@Override
	public String toString() {
		return "JDJsonComments [id=" + id + ", score=" + score + ", content=" + content + ", creationTime="
				+ creationTime + ", referenceTime=" + referenceTime + ", nickname=" + nickname + ", uid=" + uid
				+ ", userIp=" + userIp + ", userLevelId=" + userLevelId + ", userLevelName=" + userLevelName
				+ ", userProvince=" + userProvince + "]";
	}

	public String getScore() {
		return score;
	}

	public void setScore(String score) {
		this.score = score;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(String creationTime) {
		this.creationTime = creationTime;
	}

	public String getReferenceTime() {
		return referenceTime;
	}

	public void setReferenceTime(String referenceTime) {
		this.referenceTime = referenceTime;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getGuid() {
		return guid;
	}

	public void setGuid(String guid) {
		this.guid = guid;
	}

	public String getUserIp() {
		return userIp;
	}

	public void setUserIp(String userIp) {
		this.userIp = userIp;
	}

	public String getUserLevelId() {
		return userLevelId;
	}

	public void setUserLevelId(String userLevelId) {
		this.userLevelId = userLevelId;
	}

	public String getUserRegisterTime() {
		return userRegisterTime;
	}

	public void setUserRegisterTime(String userRegisterTime) {
		this.userRegisterTime = userRegisterTime;
	}

	public String getUserLevelName() {
		return userLevelName;
	}

	public void setUserLevelName(String userLevelName) {
		this.userLevelName = userLevelName;
	}

	public String getUserProvince() {
		return userProvince;
	}

	public void setUserProvince(String userProvince) {
		this.userProvince = userProvince;
	}
}