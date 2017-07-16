package com.bdsoft.datamin.entity;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

@TableName(value = "jd_queue")
public class JDQueue implements Serializable {

	@TableField(exist = false)
	private static final long serialVersionUID = 1L;

	@TableId
	private Long id;

	private String url;
	private Integer qstatus; // 0-未抓取，1-抓取成功，2-抓取失败，3-不再维护抓取
	private Integer qtype; // 1-商品地址，2-评论地址

	private Date ctime;
	private Date ftime;
	private Date rtime;

	private String ferr;

	public JDQueue() {
		super();
	}

	public JDQueue(String url) {
		this.url = url;
	}

	/**
	 * 新加地址
	 * 
	 * @param url
	 *            url
	 * @param qt
	 *            类型
	 */
	public JDQueue(String url, int qt) {
		this.url = url;
		this.qtype = qt;
		this.qstatus = 0;
		this.ctime = new Date();
	}

	@Override
	public String toString() {
		return "JDQueue [id=" + id + ", qstatus=" + qstatus + ", url=" + url + ", qtype=" + qtype + ", ctime=" + ctime
				+ ", ftime=" + ftime + ", rtime=" + rtime + ", ferr=" + ferr + "]";
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getQstatus() {
		return qstatus;
	}

	public void setQstatus(Integer qstatus) {
		this.qstatus = qstatus;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Integer getQtype() {
		return qtype;
	}

	public void setQtype(Integer qtype) {
		this.qtype = qtype;
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

	public Date getRtime() {
		return rtime;
	}

	public void setRtime(Date rtime) {
		this.rtime = rtime;
	}

	public String getFerr() {
		return ferr;
	}

	public void setFerr(String ferr) {
		this.ferr = ferr;
	}

}