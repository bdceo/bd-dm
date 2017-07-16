package com.bdsoft.datamin.fetch.douban.book.job;

import java.util.Date;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.bdsoft.datamin.fetch.douban.DoubanController;

public class LatestBookJob extends QuartzJobBean {

	@Override
	protected void executeInternal(JobExecutionContext context)
			throws JobExecutionException {
		System.out.println("\n开始抓取新书推荐--" + new Date().toLocaleString());
		DoubanController.getInstance().fetchLatestBooks();
	}

}
