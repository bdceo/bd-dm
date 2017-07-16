/**
 * ProductExt.java
 * com.bdsoft.datamin.entitys.jd
 * Copyright (c) 2016, 北京微课创景教育科技有限公司版权所有.
*/

package com.bdsoft.datamin.entity;

import java.util.Date;

import com.bdsoft.datamin.fetch.jd.feed.JDProductFeed;

/**
 * <p>
 *
 * @author   丁辰叶
 * @date	 2016-1-28
 * @version  1.0.0
 */
public class ProductExt extends JDProduct {

	private static final long serialVersionUID = 1L;

	public ProductExt() {
		super();
	}

	public ProductExt(String pid) {
		super();
		setPid(pid);
	}

	/**
	 * 从 抓取解析到的Feed对象，封装成入库对象
	 * 
	 * @param jdp
	 */
	public ProductExt(JDProductFeed jdp) {
		setPid(jdp.getSkuid());
		// 20140125: mysql中商品名称字段长度200
		setPname(jdp.getName());
		if (jdp.getName().length() > 200) {
			setPname(jdp.getName().substring(0, 200));
		}

		setPrice(jdp.getPrice());
		if (jdp.getPrice() == 0f) {
			setPstat(2);
		} else if (jdp.getPrice() < 0f) {
			setPstat(3);
		} else {
			setPstat(1);
		}
		setPurl(jdp.getUrl());
		setPtype(jdp.getJdSell());
		if (getPtype() == 2) {
			setVid(jdp.getJdv().getVid());
		}
		setRvecount(jdp.getReviewCount());
		setCat1(jdp.getCat1());
		setCat1Code(jdp.getCat1Code());
		setCat2(jdp.getCat2());
		setCat2Code(jdp.getCat2Code());
		setCat3(jdp.getCat3());
		setCat3Code(jdp.getCat3Code());
		setCtime(new Date());
		setUtime(new Date());
	}

	public void update(JDProduct newp) {
		setUtime(new Date());
		setPrice(newp.getPrice());
		setRvecount(newp.getRvecount());
	}
}
