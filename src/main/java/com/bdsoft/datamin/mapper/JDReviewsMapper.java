package com.bdsoft.datamin.mapper;

import com.baomidou.mybatisplus.mapper.AutoMapper;
import com.bdsoft.datamin.entity.JDReviews;

/**
 * ReviewsMapper数据库操作接口类
 */
public interface JDReviewsMapper extends AutoMapper<JDReviews> {

	public Integer selectCount();
}