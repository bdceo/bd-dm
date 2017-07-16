package com.bdsoft.datamin.fetch.dangdang;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.bdsoft.datamin.util.BDSpringUtil;
import com.bdsoft.datamin.util.TimeCoster;
import com.bdsoft.datamin.util.http.NetUtil;

/**
 * 抓取当当商品
 * 
 * @author	丁辰叶
 * @date	2014-9-25
 */
public class DangDangController {

	public static void main(String[] args) throws Exception {
		BDSpringUtil.init();
		TimeCoster tc = TimeCoster.getInstance();

		String url = "http://product.dangdang.com/8822794.html";
		url = "http://product.dangdang.com/23319327.html";
		url = "http://product.dangdang.com/23439608.html";

		tc.start("fetch_dd_product", "抓取当当商品页");
		Document doc = NetUtil.getJsoupDocByGet(url, NetUtil.CHARSET_GBK);
		tc.end("fetch_dd_product");

		tc.start("take_product_info", "提取商品详情");
		// 商品编号
		String pid = doc.getElementById("pid_span").attr("product_id");
		System.out.println(String.format("商品编号=%s", pid));

		tc.end("take_product_info");
	}

}
