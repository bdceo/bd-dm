package com.bdsoft.datamin.fetch.douban.book.job;

import java.util.Date;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.bdsoft.datamin.fetch.douban.DoubanController;

public class ReviewsJob extends QuartzJobBean {

	protected void executeInternal(JobExecutionContext arg0)
			throws JobExecutionException {
		System.out.println("\n开始抓取评论列表--" + new Date().toLocaleString());

		DoubanController.getInstance().fetchReviews();
	}

}
