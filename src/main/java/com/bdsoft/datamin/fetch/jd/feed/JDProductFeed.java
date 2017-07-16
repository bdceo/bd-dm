package com.bdsoft.datamin.fetch.jd.feed;

import com.bdsoft.datamin.util.StringUtil;

public class JDProductFeed {

	private String skuid; // 商品唯一编码
	private String name; // 商品名
	private String url;

	private float price; // -1-下架，0-无货

	private int jdSell = 1;// 1-京东自营，2-卖家店铺
	private JDVenderFeed jdv;// 卖家信息

	// 商品分类信息
	private String cat1;
	private String cat1Code;
	private String cat2;
	private String cat2Code;
	private String cat3;
	private String cat3Code;

	private int reviewCount;

	@Override
	public String toString() {

		StringBuilder sb = new StringBuilder("ProductFeed [skuid=");
		sb.append(skuid).append(", 商品=").append(name).append(", 价格=").append(price);
		sb.append(", 分类=").append(cat1).append("-").append(cat2).append("-").append(cat3);
		sb.append(", 评论总数=").append(reviewCount);
		sb.append("]");
		return sb.toString();
	}

	public JDProductFeed() {
		super();
	}

	public JDProductFeed(String url) {
		this.url = url;
	}

	public JDProductFeed(String name, float price) {
		super();
		this.name = name;
		this.price = price;
	}

	public void setCat(String cat1, String cat2, String cat3) {
		this.cat1 = cat1;
		this.cat2 = cat2;
		this.cat3 = cat3;
	}

	/**
	 * 通过从商品URL中提取分类部分数据(xxx,xx,xxx)获取三级分类码
	 * 
	 * @param codeStr
	 */
	public void setCatCode(String codeStr) {
		if (StringUtil.isEmpty(codeStr)) {
			return;
		}
		// 必须保证包含‘,’，并且至少两个‘,’，判断索引位置不同
		if (codeStr.contains(",") && (codeStr.indexOf(",") != codeStr.lastIndexOf(","))) {
			setCatCode(codeStr.split(","));
		}
	}

	/**
	 * 按顺序设定商品三级分类码
	 * 
	 * @param cats
	 */
	public void setCatCode(String[] cats) {
		if (cats == null) {
			return;
		}
		// 至少需要获取三级分类
		if (cats.length < 3) {
			return;
		}
		this.cat1Code = cats[0];
		this.cat2Code = cats[1];
		this.cat3Code = cats[2];
	}

	public void setCatCode(String cat1c, String cat2c, String cat3c) {
		this.cat1Code = cat1c;
		this.cat2Code = cat2c;
		this.cat3Code = cat3c;
	}

	public void setPrice(String price) {
		if (StringUtil.isEmpty(price)) {
			price = "-1";
		}
		this.setPrice(Float.parseFloat(price));
	}

	public String getUrl() {
		return url;
	}

	public int getReviewCount() {
		return reviewCount;
	}

	public void setReviewCount(int reviewCount) {
		this.reviewCount = reviewCount;
	}

	public JDVenderFeed getJdv() {
		return jdv;
	}

	public void setJdv(JDVenderFeed jdv) {
		this.jdv = jdv;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getSkuid() {
		return skuid;
	}

	public void setSkuid(String skuid) {
		this.skuid = skuid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public int getJdSell() {
		return jdSell;
	}

	public void setJdSell(int jdSell) {
		this.jdSell = jdSell;
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

}
