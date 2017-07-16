package com.bdsoft.datamin.fetch.amazon;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.bdsoft.datamin.util.BDSpringUtil;
import com.bdsoft.datamin.util.TimeCoster;
import com.bdsoft.datamin.util.http.BDHttpUtil;

/**
 * 抓取亚马逊商品
 * 
 * @author	丁辰叶
 * @date	2014-9-22
 */
public class AmazonController {

	public static void main(String[] args) throws Exception {
		BDSpringUtil.init();
		TimeCoster tc = TimeCoster.getInstance();

		String url = "http://www.amazon.cn/dp/B00M3XHP6O";// 参与感
		//		url = "http://www.amazon.cn/dp/B00HZFOCJ8"; // 教父
		//		url = "http://www.amazon.cn/dp/B00M1JRCCC";// 我的简史

		tc.start("take_amazon_product_info", "提取亚马逊商品详情");
		String src = BDHttpUtil.sendGet(url);
		Document doc = Jsoup.parse(src);
 

		tc.end("take_amazon_product_info");
	}
}
