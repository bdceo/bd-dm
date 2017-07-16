package com.bdsoft.datamin.fetch.huxiu;

import java.net.URLDecoder;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.bdsoft.datamin.fetch.duke.DukeConfig;
import com.bdsoft.datamin.util.http.NetUtil;

/**
 * 虎嗅网-读点频道
 * 
 * @author	丁辰叶
 * @date	2014-8-4
 */
public class HuxiuFetcher {

	public static void main(String[] args) throws Exception {
		//		takeBookInfo("http://www.huxiu.com/index.php/article/168/1.html");

		// 提取种子
		Set<String> seeds = HuxiuSeed.initSeeds();

		// 提取书籍
		Set<String> books = takeBookUrl(seeds);

		// 提取详情
		for (String url : books) {
			takeBookInfo(url);
		}
	}

	// 提取书籍详情
	public static void takeBookInfo(String url) throws Exception {
		System.out.println(String.format("\n准备提取书籍：%s", url));
		Document html = NetUtil.getJsoupDocByGet(url, DukeConfig.WEB_CHARSET);

		Element root = html.getElementsByClass("books-neirong").first();

		Element tmp = root.getElementsByClass("book-box-subject").first();
		Elements tmps = tmp.getElementsByTag("p");
		int ts = tmps.size();

		String name = tmps.get(0).text();
		name = name.substring(name.indexOf("：") + 1);
		System.out.println(String.format("书籍名称：%s", name));

		String author = tmps.get(1).text();
		author = author.substring(author.indexOf("：") + 1);
		String translator = ts > 3 ? tmps.get(2).text() : "";
		translator = translator.substring(translator.indexOf("：") + 1);
		System.out.println(String.format("作者：%s  译者：%s", author, translator));

		String pubHouse = ts > 3 ? tmps.get(3).text() : tmps.get(2).text();
		pubHouse = pubHouse.substring(pubHouse.indexOf("：") + 1);
		System.out.println(String.format("出版社：%s", pubHouse));

		String bookRec = root.getElementsByClass("summary").first().text();
		System.out.println(String.format("编辑推荐：%s", bookRec));

		String bigImg = root.getElementsByClass("book-img").first().getElementsByTag("img").first().attr("src");
		System.out.println(String.format("大图地址：%s", bigImg));

		tmp = html.getElementsByClass("buy-book").first();
		if (tmp == null) {
			System.out.println("该书无销售地址，return下一个");
			return;
		}
		tmps = tmp.getElementsByTag("li");
		for (Element li : tmps) {
			Element li_a = li.getElementsByTag("a").first();
			String abuy = li_a.attr("href");
			System.out.println(String.format("%s，地址：%s", li_a.text(), abuy));
			System.out.println(decode(abuy));
		}
	}

	private static String decode(String url) throws Exception {
		url = URLDecoder.decode(url, NetUtil.CHARSET_UTF8);
		Map<String, String> params = parse(url);
		if (url.contains("amazon.cn")) {// 亚马逊
			url = url.substring(0, url.indexOf("?"));
		} else if (url.contains("jd.com")) {// 京东
			url = params.get("to");
		} else if (url.contains("dangdang.com")) {// 当当
			url = params.get("backurl");
		} else { // 默认提取导购连接中的url参数
			url = params.get("url");
		}
		return url;
	}

	private static Map<String, String> parse(String url) {
		String[] params = url.split("\\&");
		Map<String, String> pm = new HashMap<String, String>();
		for (String p : params) {
			int index = p.indexOf("=");
			pm.put(p.substring(0, index), p.substring(index + 1));
		}
		return pm;
	}

	// 从种子URL分页提取书籍URL
	public static Set<String> takeBookUrl(Set<String> seeds) throws Exception {
		Set<String> books = new HashSet<String>();
		for (String url : seeds) {
			System.out.println(String.format("准备抓取：%s", url));

			// 分页提取书籍URL
			for (int i = 1; i < HuxiuConfig.DEF_PAGETOTAL; i++) {
				String pageUrl = HuxiuConfig.getPagerUrl(url, i);

				Document html = NetUtil.getJsoupDocByGet(pageUrl, HuxiuConfig.WEB_CHARSET);
				// 根节点：书籍列表
				Element root = html.getElementsByClass("book-list").first();
				if (root.hasText()) {
					System.out.println(String.format("提取第%d页书籍：%s", i, pageUrl));
					Elements divs = root.getElementsByClass("book-box");
					for (Element div : divs) {
						Element div_a = div.getElementsByTag("a").first();
						String div_a_href = div_a.absUrl("href");
						System.out.println(String.format("\t书籍地址：%s", div_a_href));
						books.add(div_a_href);
					}
				} else {
					break;// 超出分页，退出进入下一分类
				}
			}
		}
		System.out.println(String.format("总共提取书籍：%d", books.size()));
		return books;
	}

}
