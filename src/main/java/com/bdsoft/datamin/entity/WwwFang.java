package com.bdsoft.datamin.entity;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

@TableName(value = "www_fang")
public class WwwFang implements Serializable {

	@TableField(exist = false)
	private static final long serialVersionUID = 6371879300280509026L;

	@TableId
	private Long id;

	private Integer infoFrom;// 信息来源：1-豆瓣，2-58
	private String infoUrl;
	private String infoTitle;
	private String infoDetail;

	private Date pTime; // 信息发布日期，网站提取
	private Date cTime;// 收录日期，爬虫抓取时间
	private Date mTime;// 更新时间，爬虫更新时间

	private Integer infoStat;// 信息状态：1-正常，2-删除，3-过期

	private String infoTel;
	private String infoQQ;
	private String infoWX;
	private String infoEmail;

	public WwwFang() {
	}

	public WwwFang(String url) {
		this.infoUrl = url;
		this.cTime = new Date();
		this.infoStat = 1;
		this.infoDetail = "未抓取";
	}	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getInfoFrom() {
		return infoFrom;
	}

	public void setInfoFrom(Integer infoFrom) {
		this.infoFrom = infoFrom;
	}

	public String getInfoUrl() {
		return infoUrl;
	}

	public void setInfoUrl(String infoUrl) {
		this.infoUrl = infoUrl;
	}

	public String getInfoTitle() {
		return infoTitle;
	}

	public void setInfoTitle(String infoTitle) {
		this.infoTitle = infoTitle;
	}

	public String getInfoDetail() {
		return infoDetail;
	}

	public void setInfoDetail(String infoDetail) {
		this.infoDetail = infoDetail;
	}

	public Date getpTime() {
		return pTime;
	}

	public void setpTime(Date pTime) {
		this.pTime = pTime;
	}

	public Date getcTime() {
		return cTime;
	}

	public void setcTime(Date cTime) {
		this.cTime = cTime;
	}

	public Date getmTime() {
		return mTime;
	}

	public void setmTime(Date mTime) {
		this.mTime = mTime;
	}

	public Integer getInfoStat() {
		return infoStat;
	}

	public void setInfoStat(Integer infoStat) {
		this.infoStat = infoStat;
	}

	public String getInfoTel() {
		return infoTel;
	}

	public void setInfoTel(String infoTel) {
		this.infoTel = infoTel;
	}

	public String getInfoQQ() {
		return infoQQ;
	}

	public void setInfoQQ(String infoQQ) {
		this.infoQQ = infoQQ;
	}

	public String getInfoWX() {
		return infoWX;
	}

	public void setInfoWX(String infoWX) {
		this.infoWX = infoWX;
	}

	public String getInfoEmail() {
		return infoEmail;
	}

	public void setInfoEmail(String infoEmail) {
		this.infoEmail = infoEmail;
	}

}