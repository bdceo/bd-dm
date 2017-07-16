package com.bdsoft.datamin.service;

import com.bdsoft.datamin.entity.DouFetchQueue;
import com.bdsoft.datamin.fetch.douban.book.feed.BookFeed;

public interface IDouBookService {

	public boolean saveBook(BookFeed feed);

	public boolean saveBug_ReviewList(DouFetchQueue fetchTmp, BookFeed feed);

	public boolean saveBook(DouFetchQueue fetchTmp, BookFeed feed);

	public void initBookFetchFrom();
}