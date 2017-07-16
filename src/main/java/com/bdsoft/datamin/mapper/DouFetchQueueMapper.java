/**
 * DouFetchTmpMapper.java
 * com.bdsoft.datamin.mapper
 * Copyright (c) 2016, 北京微课创景教育科技有限公司版权所有.
*/

package com.bdsoft.datamin.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.AutoMapper;
import com.bdsoft.datamin.entity.DouFetchQueue;

public interface DouFetchQueueMapper extends AutoMapper<DouFetchQueue> {

	/**
	 * 初始化图书抓取url
	 */
	public List<DouFetchQueue> selectBookUrl4init();

	/**
	 * 加载未抓取的链接
	 * 
	 * @param id 起始id
	 * @param flag 类型
	 * @param size 数量
	 * @return
	 */
	public List<DouFetchQueue> selectUnfetchObjlinks(@Param("id") Long id, @Param("flag") String flag,
			@Param("size") int size);
}
