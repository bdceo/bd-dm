package com.bdsoft.datamin.entity;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.bdsoft.datamin.fetch.jd.feed.JDProductFeed;
 
@TableName(value = "jd_product")
public class JDProduct implements Serializable {

	@TableField(exist = false)
	private static final long serialVersionUID = -4258800018882957076L;

	@TableId
	private Long id;

	private String pid;
	private String pname;
	private Float price;
	private String purl;

	private Integer ptype; // 商品类型： 1-京东自营，2-卖家店铺
	private String vid;

	private Integer pstat; // 商品状态：1-有货，2-无货，3-下架

	private Integer rvecount; // 评论总数

	private String cat1;
	private String cat1Code;
	private String cat2;
	private String cat2Code;
	private String cat3;
	private String cat3Code;

	private Date ctime;
	private Date utime;

	public JDProduct() {
		super();
	}

	/**
	 * 从 抓取解析到的Feed对象，封装成入库对象
	 * 
	 * @param jdp
	 */
	public JDProduct(JDProductFeed jdp) {
		this.pid = jdp.getSkuid();
		// 20140125: mysql中商品名称字段长度200
		this.pname = jdp.getName();
		if (jdp.getName().length() > 200) {
			this.pname = jdp.getName().substring(0, 200);
		}
		
		this.price = jdp.getPrice();
		if (price == 0f) {
			this.pstat = 2;
		} else if (price < 0f) {
			this.pstat = 3;
		} else {
			this.pstat = 1;
		}
		this.purl = jdp.getUrl();
		this.ptype = jdp.getJdSell();
		if (ptype == 2) {
			this.vid = jdp.getJdv().getVid();
		}
		this.rvecount = jdp.getReviewCount();
		this.cat1 = jdp.getCat1();
		this.cat1Code = jdp.getCat1Code();
		this.cat2 = jdp.getCat2();
		this.cat2Code = jdp.getCat2Code();
		this.cat3 = jdp.getCat3();
		this.cat3Code = jdp.getCat3Code();
		this.ctime = new Date();
		this.utime = new Date();
	}

	public void update(JDProduct newp) {
		this.utime = new Date();
		this.price = newp.getPrice();
		this.rvecount = newp.getRvecount();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getPname() {
		return pname;
	}

	public void setPname(String pname) {
		this.pname = pname;
	}

	public Float getPrice() {
		return price;
	}

	public void setPrice(Float price) {
		this.price = price;
	}

	public String getPurl() {
		return purl;
	}

	public void setPurl(String purl) {
		this.purl = purl;
	}

	public Integer getPtype() {
		return ptype;
	}

	public void setPtype(Integer ptype) {
		this.ptype = ptype;
	}

	public String getVid() {
		return vid;
	}

	public void setVid(String vid) {
		this.vid = vid;
	}

	public Integer getPstat() {
		return pstat;
	}

	public void setPstat(Integer pstat) {
		this.pstat = pstat;
	}

	public Integer getRvecount() {
		return rvecount;
	}

	public void setRvecount(Integer rvecount) {
		this.rvecount = rvecount;
	}

	public String getCat1() {
		return cat1;
	}

	public void setCat1(String cat1) {
		this.cat1 = cat1;
	}

	public String getCat1Code() {
		return cat1Code;
	}

	public void setCat1Code(String cat1Code) {
		this.cat1Code = cat1Code;
	}

	public String getCat2() {
		return cat2;
	}

	public void setCat2(String cat2) {
		this.cat2 = cat2;
	}

	public String getCat2Code() {
		return cat2Code;
	}

	public void setCat2Code(String cat2Code) {
		this.cat2Code = cat2Code;
	}

	public String getCat3() {
		return cat3;
	}

	public void setCat3(String cat3) {
		this.cat3 = cat3;
	}

	public String getCat3Code() {
		return cat3Code;
	}

	public void setCat3Code(String cat3Code) {
		this.cat3Code = cat3Code;
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