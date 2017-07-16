package com.bdsoft.datamin.fetch.douban.book.support;

import java.util.Date;
import java.util.Observable;
import java.util.Observer;

import com.bdsoft.datamin.util.JobUtil;

public class DouJobObserver implements Observer {

	private static DouJobObserver observer = null;

	private DouJobObserver() {
		super();
	}

	public synchronized static DouJobObserver getInstance() {
		if (observer == null) {
			observer = new DouJobObserver();
		}
		return observer;
	}

	/**
	 * 处理监听事件
	 */
	public void update(Observable src, Object args) {
		System.out.println("-->豆瓣图书抓取job监听处理：" + new Date().toLocaleString());

		System.out.println("由《" + src + "》发出事件");

		Integer jobId = (Integer) args;
		long delay = JobUtil.getDelay();
		JobUtil.pauseTrigger(delay, jobId);
	}

}
