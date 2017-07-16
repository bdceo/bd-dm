package com.bdsoft.datamin.mapper;

import com.baomidou.mybatisplus.mapper.AutoMapper;
import com.bdsoft.datamin.entity.JDProduct;

/**
 * ProductMapper数据库操作接口类
 */
public interface JDProductMapper extends AutoMapper<JDProduct> {

	public Integer selectCount();
}