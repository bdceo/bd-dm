package com.bdsoft.datamin.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.AutoMapper;
import com.bdsoft.datamin.entity.JDQueue;

/**
 * QueueMapper数据库操作接口类
 */
public interface JDQueueMapper extends AutoMapper<JDQueue> {

	public Integer selectCount();

	/**
	 * 从DB中删除已抓商品URL
	 */
	public void deleteFetchedQueue();

	/**
	 * 从jd_queue中读取指定数目待抓URL
	 *
	 * @param ordby 排序规则
	 * @param count 加载数量
	 * @return
	 */
	public List<JDQueue> selectUnFetchQueue(@Param("ordby") String ordby, @Param("count") int count);

}