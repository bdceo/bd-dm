package com.bdsoft.datamin.mapper;

import com.baomidou.mybatisplus.mapper.AutoMapper;
import com.bdsoft.datamin.entity.JDUser;

/**
 * UserMapper数据库操作接口类
 */
public interface JDUserMapper extends AutoMapper<JDUser> {

	public Integer selectCount();
}