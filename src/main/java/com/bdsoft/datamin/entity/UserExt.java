/**
 * UserExt.java
 * com.bdsoft.datamin.entitys.jd
 * Copyright (c) 2016, 北京微课创景教育科技有限公司版权所有.
*/

package com.bdsoft.datamin.entity;

import java.util.Date;

import org.apache.commons.lang.StringUtils;

import com.bdsoft.datamin.fetch.jd.feed.JDUserFeed;
import com.bdsoft.datamin.util.DateUtil;
import com.bdsoft.datamin.util.StringUtil;

/**
 * <p>
 *
 * @author   丁辰叶
 * @date	 2016-1-28
 * @version  1.0.0
 */
public class UserExt extends JDUser {

	private static final long serialVersionUID = 1L;

	public UserExt() {
		super();
	}

	public UserExt(JDUserFeed jdu) {
		setUid(jdu.getUid());
		setNickname(jdu.getNickname());
		setIp(jdu.getIp());
		setPrvc(jdu.getProvince());
		setLvid(0);
		if (!StringUtil.isEmpty(jdu.getLevelId())) {
			setLvid(Integer.parseInt(jdu.getLevelId()));
		}
		setLvname(jdu.getLevelName());

		if (!StringUtils.isEmpty(jdu.getRegTime())) {
			setRtime(DateUtil.parse(jdu.getRegTime(), "yyyy-MM-dd HH:mm:ss"));
		}
		setCtime(new Date());
		setUtime(new Date());
	}
}
