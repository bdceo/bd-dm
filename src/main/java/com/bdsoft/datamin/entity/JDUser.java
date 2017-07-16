package com.bdsoft.datamin.entity;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang.StringUtils;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.bdsoft.datamin.fetch.jd.feed.JDUserFeed;
import com.bdsoft.datamin.util.DateUtil;
import com.bdsoft.datamin.util.StringUtil;

@TableName(value = "jd_user")
public class JDUser implements Serializable {

	@TableField(exist = false)
	private static final long serialVersionUID = -99620726877741079L;

	@TableId
	private Long id;

	private String uid;
	private String nickname;
	private String ip;
	private String prvc;
	private Integer lvid;
	private String lvname;

	private Date rtime;
	private Date ctime;
	private Date utime;

	public JDUser() {
		super();
	}

	public JDUser(JDUserFeed jdu) {
		this.uid = jdu.getUid();
		this.nickname = jdu.getNickname();
		this.ip = jdu.getIp();
		this.prvc = jdu.getProvince();
		this.lvid = 0;
		if (!StringUtil.isEmpty(jdu.getLevelId())) {
			lvid = Integer.parseInt(jdu.getLevelId());
		}
		this.lvname = jdu.getLevelName();

		if (!StringUtils.isEmpty(jdu.getRegTime())) {
			this.rtime = DateUtil.parse(jdu.getRegTime(), "yyyy-MM-dd HH:mm:ss");
		}
		this.ctime = new Date();
		this.utime = new Date();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getPrvc() {
		return prvc;
	}

	public void setPrvc(String prvc) {
		this.prvc = prvc;
	}

	public Integer getLvid() {
		return lvid;
	}

	public void setLvid(Integer lvid) {
		this.lvid = lvid;
	}

	public String getLvname() {
		return lvname;
	}

	public void setLvname(String lvname) {
		this.lvname = lvname;
	}

	public Date getRtime() {
		return rtime;
	}

	public void setRtime(Date rtime) {
		this.rtime = rtime;
	}

	public Date getCtime() {
		return ctime;
	}

	public void setCtime(Date ctime) {
		this.ctime = ctime;
	}

	public Date getUtime() {
		return utime;
	}

	public void setUtime(Date utime) {
		this.utime = utime;
	}

}