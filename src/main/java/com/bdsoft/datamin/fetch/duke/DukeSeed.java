package com.bdsoft.datamin.fetch.duke;

import java.util.HashSet;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bdsoft.datamin.util.http.BDHttpUtil;

/**
 * 读客网，种子url
 * 
 * @author 丁辰叶
 * @date 2014-7-18
 */
public class DukeSeed {

	private static Logger log = LoggerFactory.getLogger(DukeSeed.class);

	static String WEB_INDEX = "http://www.dookbook.com/";
	static String URL_IGNORE = ".*/(zuojia|news|aboutUs)\\.php";

	public static void main(String[] args) throws Exception {
		initSeeds();
	}

	// 提取网站首页分类导航地址作为种子URL
	public static Set<String> initSeeds() throws Exception {
		String src = BDHttpUtil.sendGet(WEB_INDEX+"/index.php");
		Document html = Jsoup.parse(src, WEB_INDEX);
		Set<String> seeds = new HashSet<String>();
		Elements menuLIs = html.select(".Menu li");
		for (Element li : menuLIs) {
			Element li_a = li.getElementsByTag("a").first();

			String li_a_href = li_a.absUrl("href");
			if (li_a_href.matches(URL_IGNORE)) {
				log.info("--\t" + li_a_href);
				continue;
			}

			log.info("++\t" + li_a_href);
			seeds.add(li_a_href);
		}
		return seeds;
	}

}
