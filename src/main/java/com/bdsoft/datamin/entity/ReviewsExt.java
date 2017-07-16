/**
 * ReviewsExt.java
 * com.bdsoft.datamin.entitys.jd
 * Copyright (c) 2016, 北京微课创景教育科技有限公司版权所有.
*/

package com.bdsoft.datamin.entity;

import java.util.Date;

import com.bdsoft.datamin.fetch.jd.feed.JDReviewFeed;
import com.bdsoft.datamin.util.DateUtil;

/**
 *
 * @author   丁辰叶
 * @date	 2016-1-28
 * @version  1.0.0
 */
public class ReviewsExt extends JDReviews {

	private static final long serialVersionUID = 1L;

	public ReviewsExt() {
		super();
	}

	public ReviewsExt(JDReviewFeed jdr) {
		setRid(jdr.getId());
		setPid(jdr.getPid());
		setUid(jdr.getUid());

		setRdetail(jdr.getContent());
		setContime(DateUtil.parse(jdr.getConTime(), "yyyy-MM-dd HH:mm:ss"));
		setBuytime(DateUtil.parse(jdr.getBuyTime(), "yyyy-MM-dd HH:mm:ss"));
		setCtime(new Date());
	}

}
