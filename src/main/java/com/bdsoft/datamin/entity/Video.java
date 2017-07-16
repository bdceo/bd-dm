package com.bdsoft.datamin.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
 
@TableName(value = "t_video")
public class Video implements Serializable {

	@TableField(exist = false)
	private static final long serialVersionUID = 2735709987842090705L;

	@TableId
	private int id;

	private int webId;

	private String webCat;

	private String name;

	private String intro;

	private String url;

	private String vstat;// 0-删除；1-正常

	@Override
	public String toString() {
		return "Video[webId=" + webId + ", webCat=" + webCat + ", name=" + name
				+ ", intro=" + intro + "， url=" + url + "]";
	}

	public Video() {
	}

	public Video(int wid, String cat) {
		this.webId = wid;
		this.webCat = cat;
		this.vstat = "1";
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getWebId() {
		return webId;
	}

	public void setWebId(int webId) {
		this.webId = webId;
	}

	public String getWebCat() {
		return webCat;
	}

	public void setWebCat(String webCat) {
		this.webCat = webCat;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIntro() {
		return intro;
	}

	public void setIntro(String intro) {
		this.intro = intro;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getVstat() {
		return vstat;
	}

	public void setVstat(String vstat) {
		this.vstat = vstat;
	}

}