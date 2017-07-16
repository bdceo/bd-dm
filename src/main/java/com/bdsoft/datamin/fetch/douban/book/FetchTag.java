package com.bdsoft.datamin.fetch.douban.book;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bdsoft.datamin.entity.DouBookTag;
import com.bdsoft.datamin.entity.DouFetchQueue;
import com.bdsoft.datamin.fetch.douban.DoubanController;
import com.bdsoft.datamin.util.http.BDHttpUtil;
import com.bdsoft.datamin.util.http.NetUtil;

/**
 * 抓取图书标签下的书籍列表
 *
 * @author   丁辰叶
 * @date	 2016-5-18
 * @version  1.0.0
 */
@Deprecated
public class FetchTag {

	private static Logger log = LoggerFactory.getLogger(FetchTag.class);

	public static final String TAG_URL = "http://book.douban.com/tag/";

	/**
	 * 抓取标签
	 */
	public List<DouBookTag> fetchTags() {
		List<DouBookTag> tags = new ArrayList<DouBookTag>();
		try {
			String src = BDHttpUtil.sendGet(TAG_URL);
			Document html = Jsoup.parse(src);
			//			Document html = Jsoup.parse(new File("d:/home/fetch.html"), NetUtil.CHARSET_UTF8);
			Element ele = null;
			Elements tables = html.getElementsByClass("tagCol");
			for (Element table : tables) {
				Elements tds = table.getElementsByTag("td");
				for (Element td : tds) {
					ele = td.getElementsByTag("a").get(0);
					String name = ele.text();
					DouBookTag tag = new DouBookTag(name, 0);
					tags.add(tag);
				}
			}
			log.info(tags.toString());
			log.info("标签总数：" + tags.size());
			return tags;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 分页抓取标签下的图书
	 * 
	 * @param tag
	 * @return
	 */
	@Deprecated
	public List<DouFetchQueue> fetchTagBook(DouBookTag tag) {
		List<DouFetchQueue> tmps = new ArrayList<DouFetchQueue>();
		String url = tag.getTagUrl() + "?start=" + (tag.getFetchPage() * 20) + "&type=T";
		log.info("抓取标签图书：" + url);
		try {
			String src = BDHttpUtil.sendGet(url);
			Document html = Jsoup.parse(src);
			Elements lis = html.getElementsByClass("subject-item");
			for (Element li : lis) {
				Element e = li.getElementsByTag("h2").get(0);
				e = e.getElementsByTag("a").get(0);
				DouFetchQueue tmp = new DouFetchQueue(DoubanController.FETCH_BOOK);
				tmp.setBookName(e.attr("title"));
				tmp.setFetchUrl(e.attr("href"));
				tmps.add(tmp);
			}
			log.info("标签：" + tag.getTagName() + "下的\n" + tmps);
			return tmps;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
