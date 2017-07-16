package com.bdsoft.datamin.service;

import com.bdsoft.datamin.entity.DouFetchQueue;
import com.bdsoft.datamin.fetch.douban.book.feed.ReviewFeed;

public interface IDouReviewService {

	public boolean saveReview(DouFetchQueue fetchTmp, ReviewFeed feed);
}