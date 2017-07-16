package com.bdsoft.datamin.service;

import java.util.List;

import com.bdsoft.datamin.entity.DouFetchQueue;
import com.bdsoft.datamin.fetch.douban.book.feed.ReviewsFeed;

public interface IDouFetchQueueService {

	public List<DouFetchQueue> getUnFetchBuyLink(int size, Long from);

	public List<DouFetchQueue> getUnFetchReviews(int size, Long from);

	public List<DouFetchQueue> getUnFetchReview(int size, Long from);

	public List<DouFetchQueue> getUnFetchBook(int size, Long from);

	public boolean saveBooks(List<DouFetchQueue> bookTmps);

	/**
	 * 待抓取评论列表
	 * 
	 * @param fetchTmp
	 * @param feeds
	 * @return
	 */
	public boolean saveReviews(DouFetchQueue fetchTmp, List<ReviewsFeed> feeds);
}