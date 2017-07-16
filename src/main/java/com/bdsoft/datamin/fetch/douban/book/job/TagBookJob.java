package com.bdsoft.datamin.fetch.douban.book.job;

import java.util.Date;
import java.util.List;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.bdsoft.datamin.entity.DouBookTag;
import com.bdsoft.datamin.entity.DouFetchQueue;
import com.bdsoft.datamin.fetch.douban.DoubanController;
import com.bdsoft.datamin.fetch.douban.book.FetchTag;
import com.bdsoft.datamin.service.IDouBookTagService;
import com.bdsoft.datamin.service.IDouFetchQueueService;

public class TagBookJob extends QuartzJobBean {

	@Autowired
	private IDouBookTagService douBookTagService;
	@Autowired
	private IDouFetchQueueService douFetchTmpService;

	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		System.out.println("\n开始抓取标签图书--" + new Date().toLocaleString());
		// BookAction.getInstance().fetchTagBooks();

		List<DouBookTag> tags = douBookTagService.getUnfetchTags(DoubanController.FETCH_TAG_PERS);
		for (final DouBookTag tag : tags) {
			// new Thread(new Runnable() {
			// public void run() {
			// 分页抓取某标签的图书
			List<DouFetchQueue> tmps = new FetchTag().fetchTagBook(tag);

			// 更新标签
			tag.setFetchStat((tmps != null && tmps.size() == 0) ? 3 : 2);
			tag.setFetchPage((tmps != null) ? (tag.getFetchPage() + 1) : tag.getFetchPage());
			douBookTagService.updateTag(tag);

			// 保存待抓图书
			douFetchTmpService.saveBooks(tmps);
			// }
			// }).start();
		}
	}

}
