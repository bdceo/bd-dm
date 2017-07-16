package com.bdsoft.datamin.entity;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
 
@TableName(value = "weibo_mail")
public class WeiboMail implements Serializable {

	@TableField(exist = false)
	private static final long serialVersionUID = 1L;

	@TableId
	private int id;

	private String mail;
	private int status;

	private Date lastSend;
	private Date fetchDate;
	private String fetchSrc;

	public WeiboMail() {
		super();
	}

	public WeiboMail(String mail, Date fetchDate, String fetchSrc) {
		super();
		this.mail = mail;
		this.fetchDate = fetchDate;
		this.fetchSrc = fetchSrc;

		status = 0;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Date getLastSend() {
		return lastSend;
	}

	public void setLastSend(Date lastSend) {
		this.lastSend = lastSend;
	}

	public Date getFetchDate() {
		return fetchDate;
	}

	public void setFetchDate(Date fetchDate) {
		this.fetchDate = fetchDate;
	}

	public String getFetchSrc() {
		return fetchSrc;
	}

	public void setFetchSrc(String fetchSrc) {
		this.fetchSrc = fetchSrc;
	}

}