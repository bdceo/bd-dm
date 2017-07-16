package com.bdsoft.datamin.fetch.douban.book;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bdsoft.datamin.entity.DouFetchQueue;
import com.bdsoft.datamin.fetch.douban.DoubanController;
import com.bdsoft.datamin.fetch.douban.book.feed.BookFeed;
import com.bdsoft.datamin.util.StringUtil;
import com.bdsoft.datamin.util.exception.IpLimitedException;
import com.bdsoft.datamin.util.http.BDHttpUtil;
import com.google.common.collect.Lists;

/**
 * 抓取豆瓣图书
 *
 * @author   丁辰叶
 * @date	 2016-5-17
 * @version  1.0.0
 */
@Deprecated
public class FetchBook {

	private static Logger log = LoggerFactory.getLogger(FetchBook.class);

	// 最新图书，新书速递
	public static final String LATEST_BOOK = "http://book.douban.com/latest";

	// 榜单
	private static final String CHART_BOOK = "http://book.douban.com/chart";
	private static final String CHART_BOOK_F = "http://book.douban.com/chart?subcat=F";

	// 最受欢迎书评
	private static final String REVIEW_BEST = "https://book.douban.com/review/best/?start=#s";

	/**
	 * 抓取图书-bug：评论列表没分页修复
	 * 
	 * @param url 图书地址
	 */
	@Deprecated
	public BookFeed fetchBug_ReviewList(String url) throws IpLimitedException, Exception {
		log.info("-->抓取图书-Bug：" + url);
		BookFeed bf = new BookFeed(url);
		String src = BDHttpUtil.sendGet(url);
		if (StringUtil.isEmpty(src)) {
			log.info("网络出错，返回空内容，无法解析");
			return null;// 网络出错，返回空
		}

		try {
			//			Document html = Jsoup.parse(new File("d:/home/fetch.html"), NetUtil.CHARSET_UTF8);
			Document html = Jsoup.parse(src);
			Element e = html.getElementById("content");
			if (e == null) {
				throw new IpLimitedException("抓取豆瓣图书时IP被封");
			}
			// 所有评论
			// String reviewsUrl = url + "reviews?sort=time";
			bf.setReviewLink(url + "reviews");
			// 评论数 :html=书评 · · · · · · (共21条)
			Elements es = html.select("#reviews h2 a span");
			if (es != null && es.size() > 0) {
				bf.setReviewCount(Integer.parseInt(es.get(0).text()));
			} else {
				bf.setReviewCount(0);
			}
			log.info(bf.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bf;
	}

	/**
	 * 抓取图书信息
	 * 
	 * @param url 图书地址
	 */
	@Deprecated
	public BookFeed fetch(String url) throws IpLimitedException, Exception {
		log.info("-->抓取图书：" + url);
		BookFeed bf = new BookFeed(url);
		String src = BDHttpUtil.sendGet(url);
		if (StringUtil.isEmpty(src)) {
			log.info("网络出错，返回空内容，无法解析");
			return null;// 网络出错，返回空
		}

		try {
			//			Document html = Jsoup.parse(new File("d:/home/fetch.html"), NetUtil.CHARSET_UTF8);
			Document html = Jsoup.parse(src);
			Element e = html.getElementById("content");
			if (e == null) {
				throw new IpLimitedException("抓取豆瓣图书时IP被封");
			}

			Element root = html.getElementById("wrapper");
			Elements es = root.getElementsByTag("h1");
			// 解析书名
			bf.setName(es.get(0).text());
			// 图片
			e = root.getElementById("mainpic").getElementsByTag("img").get(0);
			bf.setName(e.attr("alt"));
			bf.setPic(e.attr("src"));
			// 图书基本信息
			Iterator<Node> nodes = root.getElementById("info").childNodes().iterator();
			Map<String, String> infoMap = new HashMap<String, String>();
			while (nodes.hasNext()) {
				Node node = nodes.next();
				String key = "", value = "";
				if (node instanceof Element) {
					Element ele = (Element) node;
					if (ele.tagName().equalsIgnoreCase("br")) {
						continue;
					} else if (ele.tagName().equalsIgnoreCase("span")) {
						if (ele.hasClass("pl")) {
							key = ele.text().replaceAll(":", "");
							node = nodes.next();
							value = ((TextNode) node).toString().replaceAll("&nbsp;", "").trim();
							if (StringUtils.isEmpty(value)) {
								node = nodes.next();
								value = ((Element) node).text().trim();
							}
						} else {
							key = ele.getElementsByTag("span").get(1).text().replaceAll(":", "");
							es = ele.getElementsByTag("a");
							StringBuilder sb = new StringBuilder();
							for (Element ae : es) {
								sb.append(ae.text().replaceAll(":", "").trim()).append(",");
							}
							value = sb.toString();
							value = value.substring(0, value.length() - 1);
						}
						infoMap.put(key.trim(), value.trim());
					}
				}
				mapCn(infoMap);
				bf.setInfoMap(infoMap);
				// 评分
				e = root.getElementsByClass("rating_num").get(0);
				if (!StringUtil.isEmpty(e.text())) {
					bf.setRank(e.text().trim());
				}
				// 内容简介
				es = html.select("#link-report p");
				StringBuilder sb = new StringBuilder();
				for (Element ele : es) {
					sb.append(ele.text());
				}
				bf.setBookIntro(sb.toString());
				// 作者简介
				es = root.getElementById("link-report").nextElementSibling().nextElementSibling().getElementsByTag("p");
				sb = new StringBuilder();
				for (Element ele : es) {
					sb.append(ele.text());
				}
				bf.setAuthorIntro(sb.toString());
				// 图书目录
				String urlId = pickUrlId(url);
				e = root.getElementById("dir_" + urlId + "_full");
				if (e == null) {
					e = root.getElementById("dir_" + urlId + "_short");
				}
				bf.setBookDir(e.text());
				// 标签
				es = root.getElementById("db-tags-section").getElementsByTag("a");
				StringBuffer sbi = new StringBuffer();
				for (Element ele : es) {
					sbi.append(ele.text()).append(" ");
				}
				bf.setTags(sbi.toString());
				// 推荐图书
				Map<String, String> bookRec = new HashMap<String, String>();
				e = root.getElementById("db-rec-section");
				if (e != null) {
					es = e.getElementsByTag("dd");
					for (Element ele : es) {
						e = ele.getElementsByTag("a").get(0);
						bookRec.put(e.text(), e.attr("href"));
					}
					bf.setBookRec(bookRec);
				}
				// 导购及评论列表页
				bf.setBuyLink(url + "buylinks");
				bf.setReviewLink(url + "reviews");
				// 评论数 :html=书评 · · · · · · (共21条)
				es = html.select("#reviews h2 a span");
				if (es != null && es.size() > 0) {
					bf.setReviewCount(Integer.parseInt(es.get(0).text()));
				} else {
					bf.setReviewCount(0);
				}
			}
			log.info(bf.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bf;
	}

	/**
	 * 从url地址，提取对象ID
	 *
	 * @param url 地址
	 */
	private static String pickUrlId(String url) {
		String reg = "\\d+";
		Pattern pattern = Pattern.compile(reg, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(url);
		if (matcher.find()) {
			return matcher.group();
		}
		return "";
	}

	/**
	 * 书籍属性格式化
	 */
	private static void mapCn(Map<String, String> infoMap) {
		for (Entry<String, String> en : infoMap.entrySet()) {
			if (en.getKey().equals("定价")) {
				infoMap.put(en.getKey(), en.getValue().replace("元", "").trim());
			} else if (en.getKey().equals("作者") || en.getKey().equals("译者") || en.getKey().equals("丛书")) {
				String[] aus = en.getValue().split("</a>");
				StringBuffer sb = new StringBuffer();
				for (String tmp : aus) {
					if (StringUtil.isEmpty(tmp)) {
						continue;
					}
					sb.append(tmp.substring(tmp.indexOf(">") + 1)).append(" / ");
				}
				String tmp = sb.toString();
				tmp = tmp.substring(0, tmp.lastIndexOf("/"));
				infoMap.put(en.getKey(), tmp.trim());
			} else if (en.getKey().equals("页数")) {
				infoMap.put(en.getKey(), en.getValue().replace("页", "").trim());
			}
		}
		if (infoMap.get("统一书号") != null) {
			String val = infoMap.get("统一书号");
			infoMap.put("ISBN", val);
		}
	}

	/**
	 * 抓取新书速递：LATEST_BOOK
	 */
	@Deprecated
	public List<DouFetchQueue> fetchLatest() throws IpLimitedException, Exception {
		log.info("-->抓取新书推荐：" + LATEST_BOOK);
		List<DouFetchQueue> bookTmps = new ArrayList<DouFetchQueue>();

		String src = BDHttpUtil.sendGet(LATEST_BOOK);
		if (StringUtil.isEmpty(src)) {
			log.info("网络出错，返回空内容，无法解析");
			return null;// 网络出错，返回空
		}

		Document html = Jsoup.parse(src);
		//		Document html = Jsoup.parse(new File("d:/home/fetch.html"), NetUtil.CHARSET_UTF8);
		Element e = html.getElementById("content");
		if (e == null) {
			throw new IpLimitedException("抓取豆瓣图书时IP被封");
		}
		try {
			// 虚构类
			Element ele = html.getElementsByClass("article").get(0);
			bookTmps.addAll(latest(ele));

			// 非虚构类
			ele = html.getElementsByClass("aside").get(0);
			bookTmps.addAll(latest(ele));

			// log.info("新书速递：" + bookTmps.toString());
			return bookTmps;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return new ArrayList<DouFetchQueue>();
	}

	/**
	 * 解析最新书籍
	 */
	private List<DouFetchQueue> latest(Element ele) {
		List<DouFetchQueue> bookTmps = new ArrayList<DouFetchQueue>();
		Elements lis = ele.getElementsByTag("li");
		for (Element li : lis) {
			if (li.hasClass("clear")) {
				continue;
			}
			DouFetchQueue tmp = new DouFetchQueue(DoubanController.FETCH_BOOK);
			ele = li.getElementsByTag("h2").get(0);
			tmp.setBookName(ele.text());
			ele = li.getElementsByTag("a").get(0);
			tmp.setFetchUrl(ele.attr("href"));
			bookTmps.add(tmp);
		}
		return bookTmps;
	}

	/**
	 * 抓取关注图书榜： http://book.douban.com/chart
	 * http://book.douban.com/chart?subcat=F
	 */
	@Deprecated
	public List<DouFetchQueue> fetchChart() {
		List<DouFetchQueue> bookTmps = new ArrayList<DouFetchQueue>();
		try {
			// 抓取虚构类排行榜
			log.info("-->抓取排行榜：" + CHART_BOOK);
			String src = BDHttpUtil.sendGet(CHART_BOOK);
			bookTmps.addAll(chart(src));

			// 抓取非虚构类排行榜
			log.info("-->抓取排行榜" + CHART_BOOK_F);
			src = BDHttpUtil.sendGet(CHART_BOOK_F);
			bookTmps.addAll(chart(src));

			log.info("排行榜：" + bookTmps);
			return bookTmps;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<DouFetchQueue>();
	}

	/**
	 * 解析新书推荐列表
	 */
	private static List<DouFetchQueue> chart(String content) {
		Document html = Jsoup.parse(content);
		Element ele = html.getElementsByClass("article").get(0);
		Elements lis = ele.getElementsByTag("li");
		List<DouFetchQueue> bookTmps = new ArrayList<DouFetchQueue>();
		for (Element li : lis) {
			DouFetchQueue tmp = new DouFetchQueue(DoubanController.FETCH_BOOK);
			ele = li.getElementsByTag("h2").get(0);
			ele = ele.getElementsByTag("a").get(0);
			tmp.setBookName(ele.text());
			tmp.setFetchUrl(ele.attr("href"));
			bookTmps.add(tmp);
		}
		return bookTmps;
	}

	@Deprecated
	public static List<DouFetchQueue> bestReview() {
		List<DouFetchQueue> list = Lists.newArrayList();
		log.info("-->抓取最受欢迎评论：" + REVIEW_BEST);
		int p = 10, l = 5;
		for (int i = 0; i < l; i++) {
			int s = i * p;
			String url = REVIEW_BEST.replace("#s", s + "");
			String src = BDHttpUtil.sendGet(url);
			Document html = Jsoup.parse(src);

			Elements eles = html.select(".ilst a");
			log.info("第{}页，共{}本书", (i + 1), eles.size());
			
			for(Element ele : eles){
				DouFetchQueue tmp = new DouFetchQueue(DoubanController.FETCH_BOOK);
				tmp.setBookName(ele.attr("title"));
				tmp.setFetchUrl(ele.attr("href"));
				list.add(tmp);
			}
		}

		return list;
	}
}
