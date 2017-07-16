/**
 * VenderExt.java
 * com.bdsoft.datamin.entitys.jd
 * Copyright (c) 2016, 北京微课创景教育科技有限公司版权所有.
*/

package com.bdsoft.datamin.entity;

import java.util.Date;

import com.bdsoft.datamin.fetch.jd.feed.JDVenderFeed;

/**
 * <p>
 *
 * @author   丁辰叶
 * @date	 2016-1-28
 * @version  1.0.0
 */
public class VenderExt extends JDVender {

	private static final long serialVersionUID = 1L;

	public VenderExt() {
		super();
	}

	public VenderExt(String vid) {
		super();
		setVid(vid);
	}

	public VenderExt(JDVenderFeed jdv) {
		setVid(jdv.getVid());
		setName(jdv.getName());
		setUrl(jdv.getVurl());
		setCmpy(jdv.getCmpy());
		setPrvc(jdv.getPrvc());
		setCity(jdv.getCity());
		setCtime(new Date());
	}
}
