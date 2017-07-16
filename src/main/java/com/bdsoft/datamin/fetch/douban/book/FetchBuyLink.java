package com.bdsoft.datamin.fetch.douban.book;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bdsoft.datamin.fetch.douban.book.feed.BuyLinkFeed;
import com.bdsoft.datamin.util.StringUtil;
import com.bdsoft.datamin.util.exception.IpLimitedException;
import com.bdsoft.datamin.util.http.BDHttpUtil;
import com.bdsoft.datamin.util.http.NetUtil;

/**
 * 抓取图书导购链接
 * 
 * TODO: 导购，改为存储各电商的SKUID，不存绝对地址
 *
 * @author   丁辰叶
 * @date	 2016-5-17
 * @version  1.0.0
 */
@Deprecated
public class FetchBuyLink {

	private static Logger log = LoggerFactory.getLogger(FetchBuyLink.class);

	public static void main(String[] args) throws Exception {
		String url = "https://book.douban.com/link2/?lowest=3190&pre=0&vendor=dangdang&srcpage=buylink&price=3679&pos=2&url=http%3A%2F%2Funion.dangdang.com%2Ftransfer.php%3Ffrom%3DP-306226-3115199-s26324497%26backurl%3Dhttp%3A%2F%2Fproduct.dangdang.com%2Fproduct.aspx%3Fproduct_id%3D23657718&cntvendor=7&srcsubj=&type=bkbuy&subject=26324497";

		url = decode(url);
		log.info(url);
	}

	/**
	 * 抓取图书购买链接信息
	 * 
	 * @param url 导购页地址
	 */
	@Deprecated
	public List<BuyLinkFeed> fetch(String url) throws IpLimitedException, Exception {
		log.info("-->抓取导购：" + url);
		List<BuyLinkFeed> feeds = new ArrayList<BuyLinkFeed>();
		String src = BDHttpUtil.sendGet(url);
		if (StringUtil.isEmpty(src)) {
			log.info("网络出错，返回空内容，无法解析");
			return null;// 网络出错，返回空
		}

		Document html = Jsoup.parse(src);
		Element e = html.getElementById("content");
		if (e == null) {
			throw new IpLimitedException("抓取豆瓣图书导购时IP被封");
		}

		Element root = html.getElementById("buylink-table");
		if (root == null) {
			log.info("该书不提供在线购买");
			return feeds;
		}
		Elements es = root.getElementsByTag("tr");
		int size = es.size();
		// 忽略表头
		for (int i = 1; i < size; i++) {
			BuyLinkFeed feed = new BuyLinkFeed();
			Elements tds = es.get(i).getElementsByTag("td");
			e = tds.get(1).getElementsByTag("a").get(0);
			feed.setBookstore(e.text());// 商家
			String bl = decode(e.attr("href"));
			if (StringUtils.isEmpty(bl)) {
				continue;
			}
			feed.setStoreUrl(bl);// 商家图书地址
			e = tds.get(2).getElementsByTag("a").get(0);
			feed.setStorePrice(e.text().trim());// 价格
			if (tds.size() > 4) {
				e = tds.get(3);
				if (!StringUtil.isEmpty(e.text())) {
					feed.setSavePrice(e.text().trim());// 节省
				}
			}
			feeds.add(feed);
			log.info(feed.toString());
		}
		return feeds;
	}

	/**
	 * 提取导购链接
	 */
	private static String decode(String url) throws Exception {
		Map<String, String> params = parse(url);
		if (url.contains("vendor=joyo")) {// 亚马逊
			url = params.get("url");
			//		} else if (url.contains("vendor=jingdong")) {// 京东
			//					url = params.get("url");
		} else if (url.contains("vendor=beifa")) {// 北发图书网
			url = params.get("url");
			//		} else if (url.contains("vendor=dangdang")) {// 当当
			//			url = params.get("url");
		} else if (url.contains("vendor=99read")) {// 99网上书城
			url = params.get("t");
		} else if (url.contains("vendor=chinapub")) {// china-pub
			url = params.get("URL");
		} else if (url.contains("vendor=bookuu")) { // 博库网
			url = URLDecoder.decode(url, NetUtil.CHARSET_UTF8);
			params = parse(url);
			url = params.get("t");
		} else if (url.contains("vendor=taoshu")) { // 淘书网
			url = params.get("url");
		} else if (url.contains("vendor=xinhua")) { // 新华书店
			url = URLDecoder.decode(url, NetUtil.CHARSET_UTF8);
			params = parse(url);
			url = params.get("url");
		} else if (url.contains("vendor=lanree")) { // 
			url = params.get("url");
		} else if (url.contains("vendor=kuaishubao")) { // 快书包
			url = params.get("url");
		} else if (url.contains("vendor=wenxuan")) { // 文轩网
			url = URLDecoder.decode(url, NetUtil.CHARSET_UTF8);
			params = parse(url);
			url = params.get("url");
		} else { // 默认提取导购连接中的url参数
			url = params.get("url");
		}
		return url;
	}

	/**
	 * 提取各网站原始商品地址
	 *
	 * @param url 原始url
	 */
	private static Map<String, String> parse(String url) throws Exception {
		url = url.substring(url.indexOf("?") + 1);
		String[] params = url.split("\\&");
		Map<String, String> pm = new HashMap<String, String>();
		for (String p : params) {
			String[] entry = p.split("=");
			String key = entry[0];
			String value = (entry.length > 1) ? entry[1] : "";
			value = URLDecoder.decode(value, NetUtil.CHARSET_UTF8);
			pm.put(key, value);
		}
		return pm;
	}
}
