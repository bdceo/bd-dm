package com.bdsoft.datamin.entity;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
 
@TableName(value = "www_fetch_tmp")
public class WwwFetchTmp implements Serializable {

	@TableField(exist = false)
	private static final long serialVersionUID = 1L;

	@TableId
	private int id;

	private Date addDate;// 入库日期

	private String fetchDomain;// 抓取网站域名

	private String fetchInfo;// 抓取关键词描述

	private String fetchUrl;// 待抓取地址

	private int fetchState;// 抓取状态：1-初始，2-已抓过，3-抓取失败

	private Date fetchDate;// 抓取日期

	public String toString() {
		return new StringBuffer("域名：").append(fetchDomain).append("\n关键词描述:").append(fetchInfo).append("\n待抓取地址：")
				.append(fetchUrl).toString();
	}

	public WwwFetchTmp() {
	}

	public WwwFetchTmp(String fetchDomain) {
		super();
		this.addDate = new Date();
		this.fetchDomain = fetchDomain;
		this.fetchState = 0;
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

	public String getFetchDomain() {
		return fetchDomain;
	}

	public void setFetchDomain(String fetchDomain) {
		this.fetchDomain = fetchDomain;
	}

	public String getFetchInfo() {
		return fetchInfo;
	}

	public void setFetchInfo(String fetchInfo) {
		this.fetchInfo = fetchInfo;
	}

	public String getFetchUrl() {
		return fetchUrl;
	}

	public void setFetchUrl(String fetchUrl) {
		this.fetchUrl = fetchUrl;
	}

	public int getFetchState() {
		return fetchState;
	}

	public void setFetchState(int fetchState) {
		this.fetchState = fetchState;
	}

	public Date getAddDate() {
		return addDate;
	}

	public void setAddDate(Date addDate) {
		this.addDate = addDate;
	}

}