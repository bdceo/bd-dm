package com.bdsoft.datamin.entity;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.bdsoft.datamin.fetch.douban.book.feed.ReviewFeed;
import com.bdsoft.datamin.util.DateUtil;
 
@TableName(value = "dou_review")
public class DouReview implements Serializable {

	@TableField(exist = false)
	private static final long serialVersionUID = 1L;

	@TableId
	private Long id;

	private String bookIsbn;

	private Date rewDate;
 
	private String rewInfo;

	private String rewTittle;

	private String usercode;

	public DouReview(ReviewFeed feed, DouUser user, DouFetchQueue tmp) {
		this.rewInfo = feed.getReview();
		this.rewTittle = feed.getTitle();
		this.rewDate = DateUtil.parse(feed.getDate(), "yyyy-MM-dd HH:mm:ss");
		
		this.usercode = user.getUsercode();
		this.bookIsbn = tmp.getBookIsbn();
	}

	public DouReview() {
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getBookIsbn() {
		return this.bookIsbn;
	}

	public void setBookIsbn(String bookIsbn) {
		this.bookIsbn = bookIsbn;
	}

	public Date getRewDate() {
		return this.rewDate;
	}

	public void setRewDate(Date rewDate) {
		this.rewDate = rewDate;
	}

	public String getRewInfo() {
		return this.rewInfo;
	}

	public void setRewInfo(String rewInfo) {
		this.rewInfo = rewInfo;
	}

	public String getRewTittle() {
		return this.rewTittle;
	}

	public void setRewTittle(String rewTittle) {
		this.rewTittle = rewTittle;
	}

	public String getUsercode() {
		return this.usercode;
	}

	public void setUsercode(String usercode) {
		this.usercode = usercode;
	}

}