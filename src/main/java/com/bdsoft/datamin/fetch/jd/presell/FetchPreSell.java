package com.bdsoft.datamin.fetch.jd.presell;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.bdsoft.datamin.entity.JDQueue;
import com.bdsoft.datamin.fetch.jd.JdFetcher;
import com.bdsoft.datamin.util.TimeCoster;
import com.bdsoft.datamin.util.http.NetUtil;

/**
 * 京东，图书预售专区
 * 
 * @author	丁辰叶
 * @date	2014-9-19
 */
public class FetchPreSell extends JdFetcher {
 
	
	/**
	 * 预售主页：http://jmall.jd.com/p64928.html
	 */
	public static void main(String[] args) throws Exception {
		FetchPreSell fps = new FetchPreSell();
		// 预售分类
		Map<Integer, String> catMap = new HashMap<Integer, String>();
		catMap.put(2155014, "文学艺术");
		catMap.put(2155154, "青春文学");
		catMap.put(2155091, "人文社科");
		catMap.put(2155118, "经管励志");
		catMap.put(2155121, "精彩生活");
		catMap.put(2155124, "美好童书");
		catMap.put(2155128, "尖端科技");
		catMap.put(2155144, "文教考试");
		catMap.put(2155149, "套装典藏");

		String baseUrl = "http://jmall.jd.com/view_page-#CAT.html";

		TimeCoster.getInstance().start("fetch-jd-presell", "抓取预售图书");
		for (Entry<Integer, String> en : catMap.entrySet()) {
			System.out.println("*************************************************");
			System.out.println(String.format("抓取预售图书分类：%s", en.getValue()));
			String preUrl = baseUrl.replaceAll("#CAT", String.valueOf(en.getKey()));

			fps.parsePreSellUrl(preUrl);
		}
		TimeCoster.getInstance().end("fetch-jd-presell");
	}

	/**
	 * 解析预售分类页面，提取书籍url
	 * 
	 * @param preUrl 预售分类页
	 * @throws Exception
	 */
	public void parsePreSellUrl(String preUrl) throws Exception {
		Document html = NetUtil.getJsoupDocByGet(preUrl, NetUtil.CHARSET_UTF8);

		Elements bookEles = html.getElementsByClass("jDesc");
		for (Element bookEle : bookEles) {
			String name = bookEle.attr("title");
			String href = bookEle.getElementsByTag("a").first().attr("href");
			if (href.startsWith("//")) {
				href = "http:" + href;
			}
			
			JDQueue jdq = jdqMapper.selectOne(new JDQueue(href));
			if (jdq == null) {
				jdqMapper.insertSelective(new JDQueue(href, 1));
			}
			System.out.println(String.format("\n《%s》 \t地址：%s", name, href));
		}

	}

}
