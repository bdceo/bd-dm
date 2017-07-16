package com.bdsoft.datamin.entity;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.bdsoft.datamin.fetch.weibo.feed.WeiboFeed;
 
@TableName(value = "weibo_fetch")
public class WeiboFetch implements Serializable {

	@TableField(exist = false)
	private static final long serialVersionUID = 1L;

	@TableId
	private int id;

	private Date fetchDate;

	private String fetchKey;

	private String fetchMail;

	private String fetchSrc;

	private String userUrl;

	private String weibo;

	private int weiboCom;

	private Date weiboDate;

	private int weiboFav;

	private int weiboShare;

	private String weiboUser;

	public WeiboFetch() {
	}

	public WeiboFetch(String mail, String src) {
		this.fetchMail = mail;
		this.fetchSrc = src;
		this.fetchDate = new Date();
		this.fetchKey = "招聘 简历";
	}

	public WeiboFetch(String fetchKey, String fetchSrc, WeiboFeed feed) {
		super();
		this.fetchKey = fetchKey;
		this.fetchSrc = fetchSrc;
		this.fetchDate = new Date();

		this.fetchMail = feed.get_mail();

		this.weibo = feed.getEm();
		this.weiboDate = feed.getDate();
		this.weiboUser = feed.getUserName();
		this.userUrl = feed.getUserHome();
		this.weiboShare = feed.getShare();
		this.weiboFav = feed.getFav();
		this.weiboCom = feed.getComm();
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getFetchDate() {
		return this.fetchDate;
	}

	public void setFetchDate(Date fetchDate) {
		this.fetchDate = fetchDate;
	}

	public String getFetchKey() {
		return this.fetchKey;
	}

	public void setFetchKey(String fetchKey) {
		this.fetchKey = fetchKey;
	}

	public String getFetchMail() {
		return this.fetchMail;
	}

	public void setFetchMail(String fetchMail) {
		this.fetchMail = fetchMail;
	}

	public String getFetchSrc() {
		return this.fetchSrc;
	}

	public void setFetchSrc(String fetchSrc) {
		this.fetchSrc = fetchSrc;
	}

	public String getUserUrl() {
		return this.userUrl;
	}

	public void setUserUrl(String userUrl) {
		this.userUrl = userUrl;
	}

	public String getWeibo() {
		return this.weibo;
	}

	public void setWeibo(String weibo) {
		this.weibo = weibo;
	}

	public int getWeiboCom() {
		return this.weiboCom;
	}

	public void setWeiboCom(int weiboCom) {
		this.weiboCom = weiboCom;
	}

	public Date getWeiboDate() {
		return this.weiboDate;
	}

	public void setWeiboDate(Date weiboDate) {
		this.weiboDate = weiboDate;
	}

	public int getWeiboFav() {
		return this.weiboFav;
	}

	public void setWeiboFav(int weiboFav) {
		this.weiboFav = weiboFav;
	}

	public int getWeiboShare() {
		return this.weiboShare;
	}

	public void setWeiboShare(int weiboShare) {
		this.weiboShare = weiboShare;
	}

	public String getWeiboUser() {
		return this.weiboUser;
	}

	public void setWeiboUser(String weiboUser) {
		this.weiboUser = weiboUser;
	}

}