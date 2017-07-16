package com.bdsoft.datamin.fetch.weibo.sina;

import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.bdsoft.datamin.fetch.weibo.feed.JSFeedJson;
import com.bdsoft.datamin.fetch.weibo.feed.WeiboFeed;
import com.bdsoft.datamin.fetch.weibo.sina.util.SinaEncoder;
import com.bdsoft.datamin.util.BDFileUtil;
import com.bdsoft.datamin.util.SMS;
import com.bdsoft.datamin.util.http.NetUtil;
import com.google.gson.Gson;

public class FetchWeibo {

	public static SimpleDateFormat sdf = new SimpleDateFormat(
			"yyyy-MM-dd hh:mm");

	public static String BASE_URL = "http://s.weibo.com/weibo/";
	public static String CHARSET = "UTF-8";
	private static Integer PAGE = 0;

	/**
	 * 手动抓取
	 * 
	 * @param name
	 * @return
	 * @throws Exception
	 */
	public static List<WeiboFeed> pickByHand(String name) throws Exception {
		File file = new File(BDFileUtil.getClassPath() + name);
		FileInputStream fi = new FileInputStream(file);
		String src = NetUtil.stream2string(fi, CHARSET);
		return pickSerFeeds(src);
	}

	/**
	 * 抓取搜索结果
	 * 
	 * @param key
	 *            关键字
	 * @param page
	 *            分页码
	 * @return Feed搜索结果集合
	 * 
	 * @throws Exception
	 */
	public static List<WeiboFeed> pick(String key, int page) throws Exception {
		PAGE = page;
		System.out.println("\n开始抓取第<" + page + ">页");
		SinaEncoder pe = new SinaEncoder(key);
		// pe.add("Refer", "STopic_box");
		pe.add("page", "" + page);

		String url = BASE_URL + pe;
		System.out.println(url);

		String src = NetUtil.getHtmlSrc(url, CHARSET);
		// System.out.println(src);
		return pickSerFeeds(src);
	}

	/**
	 * 提取新浪搜索结果Feed
	 * 
	 * @param src
	 *            搜索结果页
	 * @return Feed搜索结果集合
	 */
	private static List<WeiboFeed> pickSerFeeds(String src) throws Exception {
		List<WeiboFeed> feeds = new ArrayList<WeiboFeed>();
		Document html = Jsoup.parse(src);
		Elements scpts = html.getElementsByTag("script");
		// 找到搜索结果的script标签，搜索结果是通过js输出的
		int size = scpts.size();
		System.out.println("script size = " + scpts.size());
		if (size < 10) {
			SMS.sendSMS("18668639647", "script less then 10");
			throw new Exception("ip");
		}

		// 索引重要，改版可能会变9，11，13
		// Element ele = scpts.get(13);// 1-数数
		Element ele = getJSfeed(scpts);// 2-循环
		String json = ele.data();
		// String json = getJSfeed2(scpts);// 3-正则

		// 搜索结果的js输出内容是json结构
		json = json.substring(json.indexOf("(") + 1, json.lastIndexOf(")"));
		// System.out.println(json);

		// 解析出json结构中的feed内容-转成html
		Gson g = new Gson();
		JSFeedJson fj = g.fromJson(json, JSFeedJson.class);
		// System.out.println(fj.getHtml());
		html = Jsoup.parse(fj.getHtml());
		// System.out.println(html);

		// 未找到“关键词”相关结果
		if (html.getElementsByClass("pl_noresult").size() > 0) {
			System.out.println("分页完毕，没有新结果了，返回");
			return feeds;
		}

		Elements fls = html.getElementsByClass("feed_list");
		size = fls.size();
		System.out.println("feed size = " + size);
		// 遍历Feed输出转换
		int i = 1;
		for (Element fl : fls) {
			WeiboFeed f = new WeiboFeed();
			Element face = fl.getElementsByClass("face").get(0);
			Element a = face.getElementsByTag("a").get(0);
			f.setUserHome(a.attr("href"));
			f.setUserName(a.attr("title"));

			Element content = fl.getElementsByClass("content").get(0);
			Element cp = content.getElementsByTag("p").get(0);
			f.setEm(cp.getElementsByTag("em").get(0).text());
			// 转发内容
			int s = content.getElementsByTag("dl").size();
			if (s > 0) {
				Element dl = content.getElementsByTag("dl").get(0);
				s = dl.getElementsByTag("dt").size();
				if (s > 0) {
					Element dt = dl.getElementsByTag("dt").get(0);
					s = dt.getElementsByTag("em").size();
					if (s > 0) {
						String txt = f.getEm() + "；转发："
								+ dt.getElementsByTag("em").get(0).text();
						f.setEm(txt);
					}
				}
			}

			cp = content.getElementsByTag("p").get(1);
			// feed包含地图坐标信息，需要拿第三个p
			if (cp.hasClass("map_data")) {
				cp = content.getElementsByTag("p").get(2);
			}
			Element span = cp.getElementsByTag("span").get(0);
			a = span.getElementsByTag("a").get(0);
			f.setShare(f.parseCount(a.text()));
			a = span.getElementsByTag("a").get(1);
			f.setFav(f.parseCount(a.text()));
			a = span.getElementsByTag("a").get(2);
			f.setComm(f.parseCount(a.text()));

			// 发布时间
			a = cp.getElementsByClass("date").get(0);
			f.setDate(new Date(Long.parseLong(a.attr("date"))));

			// 邮箱
			// String mail = pickEmail(f.toString());
			// f.set_mail(pickEmail(mail));

			System.out.println("第" + PAGE + "#" + i + "条\t" + f);
			// System.out.println(pickEmail(f.toString()));
			feeds.add(f);
			i++;
		}
		return feeds;
	}

	/**
	 * 从js中获取微博feed
	 * 
	 * @param scpts
	 * @return
	 */
	private static Element getJSfeed(Elements scpts) {
		Element ele = null;
		int size = scpts.size();
		// long start = new Date().getTime();
		for (int i = size - 1; i > 0; i--) {
			ele = scpts.get(i);
			System.out.println(i + "\t" + ele.html());
			if (ele.html().contains("\"pid\":\"pl_weibo_feedlist\"")) {
				break;
			}
		}
		// long stop = new Date().getTime();
		// System.out.println("#1 = " + (stop - start));
		return ele;
	}

	/**
	 * 正则提取微博feed的script标签
	 * 
	 * @param scpts
	 * @return
	 */
	private static String getJSfeed2(Elements scpts) {
		// long start = new Date().getTime();
		String script = "";
		String reg = "<script[^>]*?>.*?\"pid\":\"pl_weibo_feedlist\"[\\s\\S]*?<\\/script>";
		Pattern pattern = Pattern.compile(reg, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(scpts.outerHtml());
		if (matcher.find()) {
			script = matcher.group();
		}
		// long stop = new Date().getTime();
		// System.out.println("#2 = " + (stop - start));
		return script;
	}

}