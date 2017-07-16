/**
 * WeiboFetchMapper.java
 * com.bdsoft.datamin.mapper
 * Copyright (c) 2016, 北京微课创景教育科技有限公司版权所有.
*/

package com.bdsoft.datamin.mapper;

import java.util.List;

import com.baomidou.mybatisplus.mapper.AutoMapper;
import com.bdsoft.datamin.entity.WeiboFetch;

public interface WeiboFetchMapper extends AutoMapper<WeiboFetch> {

	/**
	 * 加载今天抓取的链接
	 */
	public List<WeiboFetch> selectTodayFetch();
}
