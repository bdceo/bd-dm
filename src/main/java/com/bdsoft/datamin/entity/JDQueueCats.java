package com.bdsoft.datamin.entity;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

@TableName(value = "jd_queue_cats")
public class JDQueueCats implements Serializable {

	@TableField(exist = false)
	private static final long serialVersionUID = 1L;

	@TableId
	private Long id;

	private String url;
	private Integer stat; // 0-未抓取，1-抓取成功，2-抓取失败

	private Date ctime;
	private Date ftime;
	private Date lftime;

	public JDQueueCats() {
		super();
	}

	public JDQueueCats(String url) {
		super();
		this.url = url;
		this.ctime = new Date();
		this.stat = 0;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Integer getStat() {
		return stat;
	}

	public void setStat(Integer stat) {
		this.stat = stat;
	}

	public Date getCtime() {
		return ctime;
	}

	public void setCtime(Date ctime) {
		this.ctime = ctime;
	}

	public Date getFtime() {
		return ftime;
	}

	public void setFtime(Date ftime) {
		this.ftime = ftime;
	}

	public Date getLftime() {
		return lftime;
	}

	public void setLftime(Date lftime) {
		this.lftime = lftime;
	}
 

}