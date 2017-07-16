package com.bdsoft.datamin.entity;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.bdsoft.datamin.fetch.douban.book.feed.ReviewFeed;
import com.bdsoft.datamin.util.IdGenerator;
 
@TableName(value = "dou_user")
public class DouUser implements Serializable {

	@TableField(exist = false)
	private static final long serialVersionUID = 1L;

	@TableId
	private int id;
 
	private Date addDate;
 
	private String douHome;
 
	private String userStat;

	private String usercode;

	private String username;

	private String userpwd;

	public DouUser(ReviewFeed feed) {
		super();
		this.usercode = IdGenerator.userCode();
		this.douHome = feed.getUserHome();
		this.username = feed.getUser();
		this.userpwd = "";
		this.userStat = "1";// 1-ok；2-error
		this.addDate = new Date();
	}

	public DouUser(Date addDate, String douHome, String userStat,
			String usercode, String username, String userpwd) {
		super();
		this.addDate = addDate;
		this.douHome = douHome;
		this.userStat = userStat;
		this.usercode = usercode;
		this.username = username;
		this.userpwd = userpwd;
	}

	public DouUser() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getAddDate() {
		return this.addDate;
	}

	public void setAddDate(Date addDate) {
		this.addDate = addDate;
	}

	public String getDouHome() {
		return this.douHome;
	}

	public void setDouHome(String douHome) {
		this.douHome = douHome;
	}

	public String getUserStat() {
		return this.userStat;
	}

	public void setUserStat(String userStat) {
		this.userStat = userStat;
	}

	public String getUsercode() {
		return this.usercode;
	}

	public void setUsercode(String usercode) {
		this.usercode = usercode;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUserpwd() {
		return this.userpwd;
	}

	public void setUserpwd(String userpwd) {
		this.userpwd = userpwd;
	}

}