package com.bdsoft.datamin.entity;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
 
@TableName(value = "www_fetch")
public class WwwFetch implements Serializable {

	@TableField(exist = false)
	private static final long serialVersionUID = 1L;

	@TableId
	private int id;

	private String fetchSrc;// 抓取来源
	private String fetchKey;// 关键词
	private String fetchUrl;// 抓取地址
	private Date fetchDate;// 抓取时间

	// 抓取的内容
	private String fetchMail;
	private String fetchPhone;
	private String fetchQq;

	private String fetchContent;

	public WwwFetch() {
	}

	public WwwFetch(WwwFetchTmp tmp) {
		this.fetchDate = new Date();
		this.fetchUrl = tmp.getFetchUrl();
		this.fetchKey = tmp.getFetchInfo();
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

	public String getFetchUrl() {
		return fetchUrl;
	}

	public void setFetchUrl(String fetchUrl) {
		this.fetchUrl = fetchUrl;
	}

	public void setFetchSrc(String fetchSrc) {
		this.fetchSrc = fetchSrc;
	}

	public String getFetchPhone() {
		return fetchPhone;
	}

	public void setFetchPhone(String fetchPhone) {
		this.fetchPhone = fetchPhone;
	}

	public String getFetchQq() {
		return fetchQq;
	}

	public void setFetchQq(String fetchQq) {
		this.fetchQq = fetchQq;
	}

	public String getFetchContent() {
		return fetchContent;
	}

	public void setFetchContent(String fetchContent) {
		this.fetchContent = fetchContent;
	}

}