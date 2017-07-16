package com.bdsoft.datamin.mapper;

import com.baomidou.mybatisplus.mapper.AutoMapper;
import com.bdsoft.datamin.entity.JDVender;

/**
 * VenderMapper数据库操作接口类
 */
public interface JDVenderMapper extends AutoMapper<JDVender> {

	public Integer selectCount();
}