package com.bdsoft.datamin.fetch.jd.feed;

// 京东，第三方卖家店铺信息
public class JDVenderFeed {

	private String vid;
	private String name;
	private String vurl;
	private String cmpy; // 公司
	private String prvc;// 省、市地区
	private String city;// 城市

	@Override
	public String toString() {
		return "JDVenderFeed [vid=" + vid + ", name=" + name + ", vurl=" + vurl
				+ ", cmpy=" + cmpy + ", prvc=" + prvc + ", city=" + city + "]";
	}

	public JDVenderFeed() {
		super();
	}

	public JDVenderFeed(String name) {
		this.name = name;
	}

	public void setBaseInfo(String id, String name, String url) {
		this.vid = id;
		this.name = name;
		this.vurl = url;
	}

	public void setCmpInfo(String cmp, String prv, String cty) {
		this.cmpy = cmp;
		this.prvc = prv;
		this.city = cty;
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

	public String getVurl() {
		return vurl;
	}

	public void setVurl(String vurl) {
		this.vurl = vurl;
	}

}
