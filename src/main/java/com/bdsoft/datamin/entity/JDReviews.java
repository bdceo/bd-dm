package com.bdsoft.datamin.entity;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.bdsoft.datamin.fetch.jd.feed.JDReviewFeed;
import com.bdsoft.datamin.util.DateUtil;

@TableName(value = "jd_reviews")
public class JDReviews implements Serializable {

	@TableField(exist = false)
	private static final long serialVersionUID = 7621682605840464318L;

	@TableId
	private Long id;

	private Long rid;
	private String pid;
	private String uid;
	private String rdetail;

	private Date contime;
	private Date buytime;
	private Date ctime;

	public JDReviews() {
		super();
	}

	public JDReviews(JDReviewFeed jdr) {
		this.rid = jdr.getId();
		this.pid = jdr.getPid();
		this.uid = jdr.getUid();

		this.rdetail = jdr.getContent();
		this.contime = DateUtil.parse(jdr.getConTime(), "yyyy-MM-dd HH:mm:ss");
		this.buytime = DateUtil.parse(jdr.getBuyTime(), "yyyy-MM-dd HH:mm:ss");
		this.ctime = new Date();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getRid() {
		return rid;
	}

	public void setRid(Long rid) {
		this.rid = rid;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getRdetail() {
		return rdetail;
	}

	public void setRdetail(String rdetail) {
		this.rdetail = rdetail;
	}

	public Date getContime() {
		return contime;
	}

	public void setContime(Date contime) {
		this.contime = contime;
	}

	public Date getBuytime() {
		return buytime;
	}

	public void setBuytime(Date buytime) {
		this.buytime = buytime;
	}

	public Date getCtime() {
		return ctime;
	}

	public void setCtime(Date ctime) {
		this.ctime = ctime;
	}

}