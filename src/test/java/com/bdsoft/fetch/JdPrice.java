package com.bdsoft.fetch;

import com.bdsoft.datamin.fetch.jd.JdUtil;
import com.bdsoft.datamin.util.Utils;

/**
 * 京东商品价格
 * 
 * @author	丁辰叶
 * @date	2014-9-19
 */
public class JdPrice {

	public static void main(String[] args) throws Exception {

		String url = "http://item.jd.com/11265521.html";// 书
		url = "http://item.jd.com/1027675125.html";// 鞋
		url = "http://item.jd.com/998200.html";// 登山
		url = "http://item.jd.com/1079800532.html";

		String skuid = JdUtil.takeSkuid(url);
		String price = JdUtil.takePrice(skuid);
		System.out.println(String.format("商品：%s，价格：%s", skuid, price));

		Thread.sleep(1500);
		Utils.openBrowser(url);
	}

}
