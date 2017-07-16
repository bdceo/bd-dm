package com.bdsoft.datamin.fetch.douban.book.job;

import java.util.Date;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.bdsoft.datamin.fetch.douban.DoubanController;

public class BuyLinkJob extends QuartzJobBean {

	protected void executeInternal(JobExecutionContext arg0)
			throws JobExecutionException {
		System.out.println("\n开始抓取导购--" + new Date().toLocaleString());

		DoubanController.getInstance().fetchBuyLinks();
	}

}
