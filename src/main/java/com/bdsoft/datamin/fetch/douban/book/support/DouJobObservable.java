package com.bdsoft.datamin.fetch.douban.book.support;

import java.util.Observable;

import com.bdsoft.datamin.fetch.douban.DoubanController;

public class DouJobObservable extends Observable {

	public DouJobObservable() {
		this.addObserver(DouJobObserver.getInstance());
	}

	public void jobNeedPause(Integer jobId) {
		this.setChanged();
		this.notifyObservers(jobId);
		
		// IP被封检测，在此临时添加启用代理抓取机制
		DoubanController.BOOK_HOST_PROXY = true;
	}

}
