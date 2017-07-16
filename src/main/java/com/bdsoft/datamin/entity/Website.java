package com.bdsoft.datamin.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

@TableName(value = "t_website")
public class Website implements Serializable {

	@TableField(exist = false)
	private static final long serialVersionUID = 1L;

	@TableId
	private int id;

	private String name;

	private String website;

	private String webtype;

	private String stat;// 0-删除；1-正常

	public Website() {
	}
	public Website(int id) {
		this.id = id;
	}

	public Website(int id, String name, String website, String webtype,
			String stat) {
		super();
		this.id = id;
		this.name = name;
		this.website = website;
		this.webtype = webtype;
		this.stat = stat;
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public String getWebtype() {
		return webtype;
	}

	public void setWebtype(String webtype) {
		this.webtype = webtype;
	}

	public String getStat() {
		return stat;
	}

	public void setStat(String stat) {
		this.stat = stat;
	}

}