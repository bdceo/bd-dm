package com.bdsoft.datamin.mapper;

import java.util.List;

import com.baomidou.mybatisplus.mapper.AutoMapper;
import com.bdsoft.datamin.entity.JDQueueCats;

/**
 * QueueCatsMapper数据库操作接口类
 */
public interface JDQueueCatsMapper extends AutoMapper<JDQueueCats> {

	/**
	 * 提取各分类首页商品，作为种子url
	 */
	public List<JDQueueCats> selectFirstPageCat();

}