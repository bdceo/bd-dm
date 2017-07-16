package com.bdsoft.datamin.service;

import java.util.List;

import com.bdsoft.datamin.entity.DouFetchQueue;
import com.bdsoft.datamin.fetch.douban.book.feed.BuyLinkFeed;

public interface IDouBuylinkService {

	public boolean saveBuyLinks(DouFetchQueue fetchTmp, List<BuyLinkFeed> feeds);

}