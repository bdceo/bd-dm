/**
 * DdcCat.java
 * com.bdsoft.datamin.fetch.ddc
 * Copyright (c) 2016, 北京微课创景教育科技有限公司版权所有.
*/

package com.bdsoft.datamin.fetch.ddc;

import java.util.List;

import com.google.common.collect.Lists;

/**
 * 电动车分类
 *
 * @author   丁辰叶
 * @date	 2016-7-8
 * @version  1.0.0
 */
public class DdcCat {

	String catName; // 分类
	String xlName; // 系列
	String xlUrl; // 列表
	List<String> pdList = Lists.newArrayList(); // 商品

	@Override
	public String toString() {
		return "DdcCat [catName=" + catName + ", xlName=" + xlName + ", xlUrl=" + xlUrl + ", 商品数=" + pdList.size()
				+ "]";
	}

	public DdcCat(String cn, String xn, String xu) {
		this.catName = cn;
		this.xlName = xn;
		this.xlUrl = xu;
	}

	public void addPd(String url) {
		pdList.add(url);
	}

	public String getCatName() {
		return catName;
	}

	public String getXlName() {
		return xlName;
	}

	public String getXlUrl() {
		return xlUrl;
	}

	public void setXlUrl(String url) {
		this.xlName = url;
	}

	public List<String> getPdList() {
		return pdList;
	}

	public void setPdList(List<String> pdList) {
		this.pdList = pdList;
	}

	public void setCatName(String catName) {
		this.catName = catName;
	}

	public void setXlName(String xlName) {
		this.xlName = xlName;
	}
}
