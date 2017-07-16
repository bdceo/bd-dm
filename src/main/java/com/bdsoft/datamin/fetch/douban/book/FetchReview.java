package com.bdsoft.datamin.fetch.douban.book;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bdsoft.datamin.fetch.douban.book.feed.ReviewFeed;
import com.bdsoft.datamin.fetch.douban.book.feed.ReviewsFeed;
import com.bdsoft.datamin.util.StringUtil;
import com.bdsoft.datamin.util.exception.IpLimitedException;
import com.bdsoft.datamin.util.http.BDHttpUtil;

/**
 * 抓取评论列表
 *
 * @author   丁辰叶
 * @date	 2016-5-17
 * @version  1.0.0
 */
@Deprecated
public class FetchReview {

	private static Logger log = LoggerFactory.getLogger(FetchReview.class);

	/**
	 * 书的评论列表页，提取评论项，再单独抓评论详情
	 * 
	 * @param url 评论列表
	 */
	@Deprecated
	public List<ReviewsFeed> fetchReviewList(String url) throws IpLimitedException, Exception {
		log.info("-->抓取评论列表：" + url);
		List<ReviewsFeed> feeds = new ArrayList<ReviewsFeed>();
		String src = BDHttpUtil.sendGet(url);
		if (StringUtil.isEmpty(src)) {
			log.info("网络出错，返回空内容，无法解析");
			return null;// 网络出错，返回空
		}

		Document html = Jsoup.parse(src);
		//		Document html = Jsoup.parse(new File("d:/home/fetch.html"), NetUtil.CHARSET_UTF8);
		Element e = html.getElementById("content");
		if (e == null) {
			throw new IpLimitedException("抓取豆瓣图书评论列表时IP被封");
		}

		String title = e.getElementsByTag("h1").get(0).text();
		String rc = title.substring(title.lastIndexOf("(") + 1, title.lastIndexOf(")"));
		int total = Integer.parseInt(rc);
		if (total > 0) {
			e = html.getElementsByClass("article").get(0);
			Elements es = e.getElementsByClass("ctsh");
			for (Element ele : es) {
				// Elements tmp = ele.getElementsByTag("li");
				// 2013-3-1修改：li改为div，按样式读取
				Elements tmp = ele.getElementsByClass("nlst");
				tmp = tmp.get(0).getElementsByTag("a");// a
				e = tmp.get(2);
				ReviewsFeed feed = new ReviewsFeed(e.text(), e.attr("href"));
				feeds.add(feed);
			}
			log.info(feeds.toString());
		} else {
			log.info("此书无评论");
		}
		return feeds;
	}

	/**
	 * 抓取书评，提取用户信息，评论详情
	 * 
	 * @param url 评论详情
	 */
	@Deprecated
	public ReviewFeed fetchReview(String url) throws IpLimitedException, Exception {
		log.info("-->抓取评论：" + url);
		String src = BDHttpUtil.sendGet(url);
		if (StringUtil.isEmpty(src)) {
			log.info("网络出错，返回空内容，无法解析");
			return null;// 网络出错，返回空
		}

		Document html = Jsoup.parse(src);
		Element e = html.getElementById("content");
		if (e == null) {
			throw new IpLimitedException("抓取豆瓣图书评论详情时IP被封");
		}

		Elements es = html.getElementsByClass("article");
		ReviewFeed feed = new ReviewFeed();
		if (es != null && es.size() > 0) {
			Element root = html.getElementsByClass("article").get(0);
			e = html.getElementsByTag("h1").get(0);
			feed.setTitle(e.text());

			root = root.getElementsByClass("piir").get(0);
			e = root.getElementsByClass("mn").get(0);
			feed.setDate(e.text());

			e = root.getElementsByClass("pl2").get(0);
			e = e.getElementsByTag("a").get(0);
			feed.setUser(e.text());
			String str = e.attr("href");
			feed.setUserHome(str.replace("book", "www"));

			e = root.getElementById("link-report");
			feed.setReview(e.text());

			log.info(feed.toString());
		} else {
			log.info("该评论貌似被和谐了");
		}
		return feed;
	}
}
