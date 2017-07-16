package com.bdsoft.datamin.fetch.douban.fang;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bdsoft.datamin.entity.WwwFang;
import com.bdsoft.datamin.service.IWwwFangService;
import com.bdsoft.datamin.util.BDSpringUtil;
import com.bdsoft.datamin.util.DateUtil;
import com.bdsoft.datamin.util.http.BDHttpUtil;
import com.bdsoft.datamin.util.http.NetUtil;

/**
 * 豆瓣租房信息
 *
 * @author   丁辰叶
 * @date	 2016-5-18
 * @version  1.0.0
 */
@Deprecated
public class FetchFang {

	private Logger log = LoggerFactory.getLogger(FetchFang.class);

	private static int ITEM_PER_PAGE = 25; // 每页多少条信息
	private static int MAX_FETCH_PAGE = 1;// 最多抓取多少页

	private static String REP_PAGE = "#PAGE#";
	private static String BASE_PAM = "discussion?start=" + REP_PAGE;

	private static String[] GROUPS = new String[] {
			// 北京个人租房
			"http://www.douban.com/group/opking/",
			// 北京无中介租房
			"http://www.douban.com/group/zhufang/",
			// 豆瓣租房小组
			"http://www.douban.com/group/fangzi/",
			// 北京租房
			"http://www.douban.com/group/sweethome/",
			// 北京租房（非中介）
			"http://www.douban.com/group/279962/",
			// 北京租房豆瓣
			"http://www.douban.com/group/26926/",
			// 北京租房房东联盟(中介勿扰)
			"http://www.douban.com/group/257523/",
			// 北京出租房
			"http://www.douban.com/group/276176/",
			// 北京租房专家
			"http://www.douban.com/group/252218/" };

	private static String[] FILTER_WORDS = new String[] { "搬家", "限女", "女友", "女女", "女生", "妹子", "妹纸", "姑娘", "拼饭", "求租",
			"单间", "经验分享", "甩租" };

	/**
	 * 抓取房屋信息
	 *
	 * @param fangMap
	 * @throws Exception
	 */
	@Deprecated
	public void fetchFang(Map<String, WwwFang> fangMap) throws Exception {
		Iterator<String> items = fangMap.keySet().iterator();
		while (items.hasNext()) {
			String furl = items.next();
			WwwFang fang = fangMap.get(furl);

			log.info("抓取房源：" + furl);

			String src = BDHttpUtil.sendGet(furl);
			Document html = Jsoup.parse(src);

			Elements eles = html.getElementsByTag("h1");
			if (eles != null && eles.size() == 1) {
				String title = eles.get(0).text();
				log.info(">>标题：" + title);

				eles = html.getElementsByClass("color-green");
				if (eles.isEmpty()) {
					log.info("貌似被封了");
					continue;
				}
				Element ele = eles.get(0);
				String timep = ele.text();
				Date ptime = DateUtil.parse(timep, "yyyy-MM-dd HH:mm:ss");
				log.info(">>发布时间：" + timep);

				ele = html.getElementById("link-report");
				String cont = ele.text();
				log.info(">>介绍：" + cont);

				fang.setmTime(new Date());
				fang.setpTime(ptime);
				fang.setInfoTitle(title);
				fang.setInfoDetail(cont);
				BDSpringUtil.getBean(IWwwFangService.class).updateFang(fang);
			}
			Thread.sleep(1500);
		}
	}

	/**
	 * 抓取个小组前三页房源链接
	 */
	@Deprecated
	public Set<String> fetchGroups() throws Exception {
		Set<String> urlSet = new HashSet<String>();
		for (int i = 0; i < GROUPS.length; i++) {
			String url = GROUPS[i];
			log.info("抓取小组：" + url);
			for (int p = 0; p < MAX_FETCH_PAGE; p++) {
				String purl = url + BASE_PAM.replace(REP_PAGE, p * ITEM_PER_PAGE + "");
				log.info("抓取分页：" + purl);

				String src = BDHttpUtil.sendGet(purl);
				Document html = Jsoup.parse(src);

				Elements eles = html.getElementsByClass("olt");
				if (eles != null && eles.size() == 1) {
					eles = eles.get(0).getElementsByClass("title");
					log.info("本页房源：" + eles.size());
					for (Element ele : eles) {
						String itext = ele.getElementsByTag("a").get(0).text();
						String iurl = ele.getElementsByTag("a").get(0).attr("href");
						if (!filterByKeywords(itext)) {
							log.info("++ 房源地址：" + iurl + "\t" + itext);
							urlSet.add(iurl);
						} else {
							log.info("-- 过滤房源：" + iurl + "\t" + itext);
						}
					}
				}
				Thread.sleep(1500);
			}
		}
		return urlSet;
	}

	// 通过关键字，过滤链接
	public static boolean filterByKeywords(String str) {
		return filterByKeywords(null, str);
	}

	public static boolean filterByKeywords(String[] keys, String str) {
		boolean filter = false;
		if (keys == null) {
			keys = FILTER_WORDS;
		}
		for (int i = 0; i < keys.length; i++) {
			boolean tmp = str.toLowerCase().contains(keys[i]);
			if (tmp) {
				filter = true;
				break;
			}
		}
		return filter;
	}

}
