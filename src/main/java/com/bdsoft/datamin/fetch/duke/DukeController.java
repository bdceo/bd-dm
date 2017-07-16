package com.bdsoft.datamin.fetch.duke;

import java.util.HashSet;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bdsoft.datamin.util.BDSpringUtil;
import com.bdsoft.datamin.util.http.BDHttpUtil;

/**
 * 读客网，首页分类频道
 * 
 * @author 丁辰叶
 * @date 2014-7-18
 */
public class DukeController {

	private static Logger log = LoggerFactory.getLogger(DukeController.class);

	// 特殊分类页，决定列表样式
	static String URL_DIFF = ".*/(baiwanji)\\.php";

	public static void main(String[] args) throws Exception {
		BDSpringUtil.init();
		// 提取种子
		Set<String> seeds = DukeSeed.initSeeds();

		// 提取书籍
		Set<String> books = takeBookUrl(seeds);

		// 提取详情
		for (String url : books) {
			takeBookInfo(url);
		}

	}

	// 提取书籍详情
	public static void takeBookInfo(String url) throws Exception {
		log.info(String.format("\n准备提取书籍：%s", url));
		String src = BDHttpUtil.sendGet(url);
		Document html = Jsoup.parse(src, url);

		Element root = html.getElementById("ctl00_CenterContent_TrData");

		String name = root.getElementById("ctl00_CenterContent_UCFixProperty1_GoodsName").text();
		log.info(String.format("书籍名称：%s", name));

		String isbn = root.getElementById("ctl00_CenterContent_UCFixProperty1_ISBN").text();
		log.info(String.format("ISBN：%s", isbn));

		String pubHouse = root.getElementById("ctl00_CenterContent_UCFixProperty1_PubHouse").text();
		log.info(String.format("出版社：%s", pubHouse));

		String pubDateInfo = root.getElementById("ctl00_CenterContent_UCFixProperty1_DivPublishTime").text();
		log.info(String.format("出版日期：%s", pubDateInfo.substring(pubDateInfo.indexOf("：") + 1)));
		String pubDate = root.getElementById("ctl00_CenterContent_UCFixProperty1_PublishTime").text();
		log.info(String.format("出版年：%s", pubDate));

		String fixedPrice = root.getElementById("ctl00_CenterContent_UCFixProperty1_Price").text();
		log.info(String.format("定价：%s", fixedPrice.substring(0, fixedPrice.indexOf("元"))));

		String author = root.getElementById("ctl00_CenterContent_UCFixProperty1_AuthorName").text();
		String translator = root.getElementById("ctl00_CenterContent_UCFixProperty1_BookMaking").nextElementSibling()
				.text();
		log.info(String.format("作者：%s  译者：%s", author, translator));

		String bigImg = root.getElementById("ctl00_CenterContent_UCFixProperty1_BigGoodsPic").absUrl("href");
		log.info(String.format("大图地址：%s", bigImg));

		Elements div_as = root.getElementById("ctl00_CenterContent_UCFixProperty1_LChildSortName").nextElementSibling()
				.getElementsByAttributeValueMatching("href", "^http.*");
		for (Element abuy : div_as) {
			log.info(String.format("%s，地址：%s", abuy.text(), abuy.attr("href")));
		}

		String bookRec = root.getElementById("ctl00_CenterContent_UCBookExplain1_Repeater1_ctl00_Content").html();
		log.info(String.format("编辑推荐：%s", bookRec));
		String bookInfo = root.getElementById("ctl00_CenterContent_UCBookExplain1_Repeater1_ctl01_Content").html();
		log.info(String.format("内容简介：%s", bookInfo));
		String authorInfo = root.getElementById("ctl00_CenterContent_UCBookExplain1_Repeater1_ctl02_Content").html();
		log.info(String.format("作者简介：%s", authorInfo));

	}

	// 从种子URL分页提取书籍URL
	public static Set<String> takeBookUrl(Set<String> seeds) throws Exception {
		Set<String> books = new HashSet<String>();
		for (String url : seeds) {
			log.info(String.format("准备抓取：%s", url));
			String rootClass = url.matches(URL_DIFF) ? "ContentBar" : "txtList";

			// 分页提取书籍URL
			for (int i = 1; i < DukeConfig.DEF_PAGETOTAL; i++) {
				String pageUrl = DukeConfig.getPagerUrl(url, i);

				String src = BDHttpUtil.sendGet(pageUrl);
				Document html = Jsoup.parse(src, pageUrl);
				// 根节点：书籍列表
				Element root = html.getElementsByClass(rootClass).first();
				if (root.hasText()) {
					log.info(String.format("提取第%d页书籍：%s", i, pageUrl));
					Elements dts = root.getElementsByTag("dt");
					for (Element dt : dts) {
						Element dt_a = dt.getElementsByTag("a").first();
						String dt_a_href = dt_a.absUrl("href");
						log.info(String.format("\t书籍地址：%s", dt_a_href));
						books.add(dt_a_href);
					}
				} else {
					break;// 超出分页，退出进入下一分类
				}
			}
		}
		log.info(String.format("总共提取书籍：%d", books.size()));
		return books;
	}

}
