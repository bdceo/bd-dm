package com.bdsoft.datamin.fetch.www;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.bdsoft.datamin.entity.WwwFetchTmp;
import com.bdsoft.datamin.mapper.WwwFetchTmpMapper;
import com.bdsoft.datamin.util.BDSpringUtil;
import com.bdsoft.datamin.util.http.NetUtil;

public class FetchTmp {

	public static String CHARSET = "UTF-8";

	private static Integer START = 1;
	private static Integer PAGE = 24;

	public static String WWW_ADMEN = "http://www.adquan.com/";// 广告门

	// 抓取url
	private static String BASE_URL = "http://www.adquan.com/list_job.php?cid=7&location=other&page=";

	public static void main(String[] args) {
		START = 1;
		PAGE = 94;
		List<WwwFetchTmp> feeds = new ArrayList<WwwFetchTmp>();
		try {
			for (int i = START; i < PAGE; i++) {
				System.out.println("\n\n抓取，第" + i + "页:" + WWW_ADMEN);
				feeds = pick(i);
				saveDB(feeds);
				feeds.clear();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			saveDB(feeds);
		}
	}

	public static void fetch() {
		List<WwwFetchTmp> feeds = new ArrayList<WwwFetchTmp>();
		try {
			for (int i = START; i <= PAGE; i++) {
				System.out.println("抓取" + WWW_ADMEN + "，第" + i + "页");
				feeds = pick(i);
				saveDB(feeds);
				feeds.clear();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			saveDB(feeds);
		}
	}

	public static List<WwwFetchTmp> pick(int page) throws Exception {
		String url = BASE_URL + page;
		System.out.println(url);
		String src = NetUtil.getHtmlSrc(url, CHARSET);
		// System.out.println(src);
		return pickPerFeeds(src);
	}

	public static List<WwwFetchTmp> pickPerFeeds(String src) throws Exception {
		List<WwwFetchTmp> feeds = new ArrayList<WwwFetchTmp>();

		Document html = Jsoup.parse(src);
		Element root = html.getElementById("newslist");
		Elements eles = root.getElementsByClass("newsone");
		int i = 0;
		for (Element e : eles) {
			i++;
			WwwFetchTmp wft = new WwwFetchTmp(WWW_ADMEN);
			Element tmp = e.getElementsByClass("newsimg").get(0).getElementsByTag("a").get(0);
			wft.setFetchUrl(WWW_ADMEN + tmp.attr("href"));

			tmp = e.getElementsByClass("newcontent").get(0);
			String info = tmp.getElementsByClass("newsname").get(0).text();
			info += "\n" + tmp.getElementsByClass("newsdesc").get(0).text();
			wft.setFetchInfo(info);

			System.out.println(PAGE + "#" + i + "\t" + wft.toString());
			feeds.add(wft);
		}

		return feeds;
	}

	/**
	 * 保存招聘微博入库
	 * 
	 * @param feeds
	 */
	public static void saveDB(List<WwwFetchTmp> feeds) {
		if (feeds.size() == 0) {
			return;
		}
		WwwFetchTmpMapper wftd = BDSpringUtil.getBean(WwwFetchTmpMapper.class);
		for (int i = 0; i < feeds.size(); i++) {
			WwwFetchTmp tmp = new WwwFetchTmp();
			tmp.setFetchUrl(feeds.get(i).getFetchUrl());
			tmp = wftd.selectOne(tmp);
			if (tmp == null) {
				wftd.insertSelective(feeds.get(i));
			} else {
				System.out.println("已存在：" + feeds.get(i).getFetchUrl());
			}
		}
	}

}
