package com.bdsoft.datamin.fetch.weibo.sina;

import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.impl.client.DefaultHttpClient;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.bdsoft.datamin.fetch.weibo.feed.JSFeedJson;
import com.bdsoft.datamin.fetch.weibo.feed.UserFeed;
import com.bdsoft.datamin.fetch.weibo.sina.util.SinaEncoder;
import com.bdsoft.datamin.util.BDFileUtil;
import com.bdsoft.datamin.util.SMS;
import com.bdsoft.datamin.util.http.NetUtil;
import com.google.gson.Gson;

public class FetchUser {

	public static SimpleDateFormat sdf = new SimpleDateFormat(
			"yyyy-MM-dd hh:mm");

	public static String BASE_URL = "http://s.weibo.com/user/";
	public static String CHARSET = "UTF-8";
	public static Integer PAGE = 0;

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
	public static List<UserFeed> pick(String key, int page) throws Exception {
		PAGE = page;
		System.out.println("\n开始抓取第<" + page + ">页");
		SinaEncoder pe = new SinaEncoder(key);
		pe.add("page", "" + page);
		// pe.add("Refer", "SUer_box");

		// SinaEncoder pe = new SinaEncoder("nickname", key);
		// pe.add("page", "" + page);

		String url = BASE_URL + pe;
		System.out.println(url);

		String src = NetUtil.getHtmlSrc(url, CHARSET);
		// System.out.println(src);
		return pickSerFeeds(src);
	}

	public static List<UserFeed> pickWithLogin(String key, int page,
			DefaultHttpClient client) throws Exception {
		PAGE = page;
		System.out.println("\n开始抓取第<" + page + ">页");
		SinaEncoder pe = new SinaEncoder(key);
		pe.add("page", "" + page);
		pe.add("Refer", "weibo_user");

		String url = BASE_URL + pe;
		System.out.println(url);

		String src = NetUtil.getHtmlSrc(url, client);
		// System.out.println(src);
		return pickSerFeeds(src);
	}

	/**
	 * 手动抓取
	 * 
	 * @param name
	 * @return
	 * @throws Exception
	 */
	public static List<UserFeed> pickByHand(String name) throws Exception {
		File file = new File(BDFileUtil.getClassPath() + name);
		FileInputStream fi = new FileInputStream(file);
		String src = NetUtil.stream2string(fi, CHARSET);
		return pickSerFeeds(src);
	}

	/**
	 * 提取新浪搜索结果Feed
	 * 
	 * @param src
	 *            搜索结果页
	 * @return Feed搜索结果集合
	 */
	private static List<UserFeed> pickSerFeeds(String src) throws Exception {
		List<UserFeed> feeds = new ArrayList<UserFeed>();
		Document html = Jsoup.parse(src);
		Elements scpts = html.getElementsByTag("script");
		// 找到搜索结果的script标签，搜索结果是通过js输出的
		int size = scpts.size();

		System.out.println("script size = " + scpts.size());
		if (size < 10) {
			SMS.sendSMS("18668639647", "script小于10个，IP被封...");
			throw new Exception("script小于10个，IP被封...");
		}

		// 索引重要，改版可能会变7
		Element ele = scpts.get(7);

		// 搜索结果的js输出内容是json结构
		String json = ele.data();
		json = json.substring(json.indexOf("(") + 1, json.lastIndexOf(")"));
		// System.out.println(json);

		// 解析出json结构中的feed内容-转成html
		Gson g = new Gson();
		JSFeedJson fj = g.fromJson(json, JSFeedJson.class);
		// System.out.println(fj.getHtml());
		html = Jsoup.parse(fj.getHtml());

		Elements fls = html.getElementsByClass("pl_personlist").get(0)
				.getElementsByClass("list_person");
		size = fls.size();
		if (size == 0) {
			System.out.println("分页完毕，没有新结果了，返回");
			return feeds;
		}
		System.out.println("feed size = " + size);
		// 遍历Feed输出转换
		for (int i = 0; i < size; i++) {
			Element fl = fls.get(i);
			UserFeed f = new UserFeed();
			Element detail = fl.getElementsByClass("person_detail").get(0);
			Element pn = detail.getElementsByTag("p").get(0);// person_name
			Element tmp = pn.getElementsByTag("a").get(0);
			f.setUser(tmp.attr("title"));
			f.setUid(tmp.attr("uid"));
			f.setUserUrl(tmp.attr("href"));

			pn = detail.getElementsByClass("person_addr").get(0);// person_addr
			tmp = pn.getElementsByTag("span").get(1);
			f.setAddr(tmp.text());

			int ss = detail.getElementsByClass("person_card").size();
			if (ss > 0) {
				pn = detail.getElementsByClass("person_card").get(0);// person_card
				f.setCard(pn.text());
			}

			pn = detail.getElementsByClass("person_num").get(0);// person_num
			tmp = pn.getElementsByTag("span").get(0);
			String num = tmp.getElementsByTag("a").get(0).text();
			f.setGz(Integer.parseInt(num));

			tmp = pn.getElementsByTag("span").get(1);
			num = tmp.getElementsByTag("a").get(0).text();
			f.setFs(Integer.parseInt(num.replaceAll("万", "0000")));

			tmp = pn.getElementsByTag("span").get(2);
			num = tmp.getElementsByTag("a").get(0).text();
			f.setWb(Integer.parseInt(num.replaceAll("万", "0000")));

			ss = detail.getElementsByClass("person_info").size();
			if (ss > 0) {
				pn = detail.getElementsByClass("person_info").get(0);// person_info
				String text = pn.getElementsByTag("p").get(0).text();
				f.setInfo(text);
			}

			Elements pls = detail.getElementsByClass("person_label");// person_label
			for (Element p : pls) {
				if (p.text().contains("标签")) {
					f.setLabel(p.text());
					continue;
				} else if (p.text().contains("教育信息")) {
					f.setSchool(p.text());
					continue;
				} else {
					f.setJob(p.text());// 职业信息
				}
			}

			System.out.println("第" + PAGE + "#" + (i + 1) + "条\t" + f);
			feeds.add(f);
		}
		return feeds;
	}
}
