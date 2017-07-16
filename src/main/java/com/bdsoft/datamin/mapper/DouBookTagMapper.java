/**
 * DouBookTagMapper.java
 * com.bdsoft.datamin.mapper
 * Copyright (c) 2016, 北京微课创景教育科技有限公司版权所有.
*/

package com.bdsoft.datamin.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.AutoMapper;
import com.bdsoft.datamin.entity.DouBookTag;
 
public interface DouBookTagMapper extends AutoMapper<DouBookTag> {

	/**
	 * 加载未抓取的图书标签url
	 *
	 * @param size 数量
	 * @return
	 */
	public List<DouBookTag> selectUnfetchTag(@Param("size") int size);
}
