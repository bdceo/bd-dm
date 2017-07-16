/**
 * BDFetcher.java
 * com.bdsoft.datamin.fetch
 * Copyright (c) 2016, 北京微课创景教育科技有限公司版权所有.
 */

package com.bdsoft.datamin.fetch;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bdsoft.datamin.fetch.douban.DoubanController;
import com.bdsoft.datamin.util.BDFileUtil;
import com.bdsoft.datamin.util.BDSpringUtil;
import com.bdsoft.datamin.util.http.BDHttpUtil;

/**
 * 非web方式爬虫启动入口
 * 
 * @author 丁辰叶
 * @date 2016-5-6
 * @version 1.0.0
 */
public class BDFetcher {

	private Logger log = LoggerFactory.getLogger(BDFetcher.class);

	// 启动Spring容器
	static {
		BDSpringUtil.init();
	}

	/**
	 * 启动爬虫
	 */
	public static void main(String[] args) {

		String file = "d:/home/fetch.html";
		final String url = "https://book.douban.com/subject/26697350";
		if (!new File(file).exists()) {
			String src = BDHttpUtil.sendGet(url);
			BDFileUtil.writeFile(file, src);
		}

		// 豆瓣-图书
		DoubanController.getInstance().fetchTest(url);
	}

	/**
	 * 网络/网站联通性等检测服务
	 */
	public static class NetMonitor implements Runnable {

		private static Logger logm = LoggerFactory.getLogger(BDFetcher.class);

		public static void log(int httpCode, String url) {
			logm.error("alarm:{},{}", httpCode, url);
			System.exit(httpCode);
		}

		public static void log(Exception e, String url) {
			e.printStackTrace();
			logm.error("alarm:{},{}", e.getMessage(), url);
		}

		public void run() {

		}
	}

}
