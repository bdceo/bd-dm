package com.bdsoft.datamin.entity;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.bdsoft.datamin.fetch.weibo.feed.UserFeed;
import com.bdsoft.datamin.fetch.www.FetchFilter;
 
@TableName(value = "weibo_user")
public class WeiboUser implements Serializable {

	@TableField(exist = false)
	private static final long serialVersionUID = 1L;

	@TableId
	private int id;

	private Date addTime;

	private String userComp;// 个人名片

	private String userHome;

	private String userMail;

	private String userPhone;

	private String userQq;

	private int userType;

	private String userName;

	private String userId;// 微博用户id
	private String userAddr;// 地址

	private String userInfo;// 简介
	private String userLabel;// 标签
	private String userSchool;// 教育
	private String userJob;// 职业

	private int fs;
	private int gz;
	private int wb;

	public WeiboUser() {
	}

	public WeiboUser(WeiboFetch fetch) {
		this.userName = fetch.getWeiboUser();
		this.userHome = fetch.getUserUrl();
		this.userMail = fetch.getFetchMail();
		this.addTime = new Date();
		this.userType = FetchFilter.getInst().parseUserType(
				fetch.getWeiboUser() + fetch.getWeibo());
	}

	public WeiboUser(UserFeed user) {
		this.userName = user.getUser();
		this.userHome = user.getUserUrl();
		this.userId = user.getUid();

		this.userMail = FetchFilter.pickEmail(user.toString());
		this.addTime = new Date();
		this.userType = FetchFilter.USER_HRD;

		this.userAddr = user.getAddr();
		this.userInfo = user.getInfo();
		this.userLabel = user.getLabel();
		this.userSchool = user.getSchool();
		this.userJob = user.getJob();
		this.fs = user.getFs();
		this.gz = user.getGz();
		this.wb = user.getWb();
	}

	public WeiboUser(String mail) {
		this.userMail = mail;
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserAddr() {
		return userAddr;
	}

	public void setUserAddr(String userAddr) {
		this.userAddr = userAddr;
	}

	public String getUserInfo() {
		return userInfo;
	}

	public int getFs() {
		return fs;
	}

	public void setFs(int fs) {
		this.fs = fs;
	}

	public int getGz() {
		return gz;
	}

	public void setGz(int gz) {
		this.gz = gz;
	}

	public int getWb() {
		return wb;
	}

	public void setWb(int wb) {
		this.wb = wb;
	}

	public void setUserInfo(String userInfo) {
		this.userInfo = userInfo;
	}

	public String getUserLabel() {
		return userLabel;
	}

	public void setUserLabel(String userLabel) {
		this.userLabel = userLabel;
	}

	public String getUserSchool() {
		return userSchool;
	}

	public void setUserSchool(String userSchool) {
		this.userSchool = userSchool;
	}

	public String getUserJob() {
		return userJob;
	}

	public void setUserJob(String userJob) {
		this.userJob = userJob;
	}

	public Date getAddTime() {
		return this.addTime;
	}

	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}

	public String getUserComp() {
		return this.userComp;
	}

	public void setUserComp(String userComp) {
		this.userComp = userComp;
	}

	public String getUserHome() {
		return this.userHome;
	}

	public void setUserHome(String userHome) {
		this.userHome = userHome;
	}

	public String getUserMail() {
		return this.userMail;
	}

	public void setUserMail(String userMail) {
		this.userMail = userMail;
	}

	public String getUserPhone() {
		return this.userPhone;
	}

	public void setUserPhone(String userPhone) {
		this.userPhone = userPhone;
	}

	public String getUserQq() {
		return this.userQq;
	}

	public void setUserQq(String userQq) {
		this.userQq = userQq;
	}

	public int getUserType() {
		return this.userType;
	}

	public void setUserType(int userType) {
		this.userType = userType;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

}