package com.bdsoft.datamin.fetch.jd;

import com.bdsoft.datamin.mapper.JDQueueCatsMapper;
import com.bdsoft.datamin.mapper.JDQueueMapper;
import com.bdsoft.datamin.util.BDSpringUtil;

public class JDFetcher {

	// 商品/评论地址队列
	protected JDQueueMapper jdqMapper;

	// 分类地址队列
	protected JDQueueCatsMapper jdqcMapper;

	public JDFetcher() {
		BDSpringUtil.init();
		jdqMapper = BDSpringUtil.getBean(JDQueueMapper.class);
		jdqcMapper = BDSpringUtil.getBean(JDQueueCatsMapper.class);
	}

}
