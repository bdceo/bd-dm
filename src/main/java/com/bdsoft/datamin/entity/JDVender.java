package com.bdsoft.datamin.entity;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.bdsoft.datamin.fetch.jd.feed.JDVenderFeed;
 
@TableName(value = "jd_vender")
public class JDVender implements Serializable {

	@TableField(exist = false)
	private static final long serialVersionUID = -4258800018882957076L;

	@TableId
	private Long id;

	private String vid;
	private String name;
	private String url;
	private String cmpy;
	private String prvc;
	private String city;

	private Date ctime;

	public JDVender() {
		super();
	}

	public JDVender(JDVenderFeed jdv) {
		this.vid = jdv.getVid();
		this.name = jdv.getName();
		this.url = jdv.getVurl();
		this.cmpy = jdv.getCmpy();
		this.prvc = jdv.getPrvc();
		this.city = jdv.getCity();
		this.ctime = new Date();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getVid() {
		return vid;
	}

	public void setVid(String vid) {
		this.vid = vid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getCmpy() {
		return cmpy;
	}

	public void setCmpy(String cmpy) {
		this.cmpy = cmpy;
	}

	public String getPrvc() {
		return prvc;
	}

	public void setPrvc(String prvc) {
		this.prvc = prvc;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public Date getCtime() {
		return ctime;
	}

	public void setCtime(Date ctime) {
		this.ctime = ctime;
	}

}