package com.bdsoft.datamin.fetch.sdxjpc;

import java.util.HashSet;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bdsoft.datamin.util.BDSpringUtil;
import com.bdsoft.datamin.util.http.BDHttpParam;
import com.bdsoft.datamin.util.http.BDHttpUtil;
import com.bdsoft.datamin.util.http.NetUtil;

/**
 * 三联书店-书籍列表
 * 
 * @author 丁辰叶
 * @date 2014-7-18
 */
public class SdxjpcFetcher {

	private static Logger log = LoggerFactory.getLogger(SdxjpcFetcher.class);
	/** 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		BDSpringUtil.init();
		// 提取种子
		Set<String> seeds = SdxjpcSeed.initSeeds();

		// 提取书籍
		Set<String> books = takeBookUrl(seeds);

		// 提取详情
		for (String url : books) {
			try {
				takeBookInfo(url);
			} catch (Exception e) {
				continue;
			}
		}
	}

	// 提取书籍详情
	public static void takeBookInfo(String url) throws Exception {
		log.info(String.format("\n准备提取书籍：%s", url));
		Document html;
		try {
			html = NetUtil.getJsoupDocByGet(url, SdxjpcConfig.WEB_CHARSET);
		} catch (Exception e) {
			return;
		}

		Element root = html.getElementById("ContentArea");

		Element a_img = root.getElementsByTag("img").first();
		String bigImg = a_img.absUrl("src");
		log.info(String.format("大图地址：%s", bigImg));

		String tmp = root.nextElementSibling().html();
		log.info(tmp);
		String[] details = tmp.replaceAll("<br />", "#").split("#");
		for (String detail : details) {
			if (detail.split("[：|:]").length < 2) {
				continue;
			}
			tmp = detail.split("[：|:]")[1].trim();
			if (detail.contains("书名")) {
				log.info(String.format("书籍名称：%s", tmp));
			} else if (detail.contains("作者")) {
				log.info(String.format("作者：%s", tmp));
			} else if (detail.contains("出版日期")) {
				log.info(String.format("出版日期：%s", tmp));
			} else if (detail.contains("定价")) {
				log.info(String.format("价格：%s", tmp));
			} else if (detail.contains("ISBN")) {
				log.info(String.format("ISBN：%s", tmp));
			}
		}

		Elements trs = html.getElementsByAttributeValue("id", "trt");
		for (Element a_tr : trs) {
			if (a_tr.html().contains("编辑推荐")) {
				String bookRec = a_tr.nextElementSibling().text();
				log.info(String.format("编辑推荐：%s", bookRec));
			} else if (a_tr.html().contains("内容简介")) {
				String bookSumy = a_tr.nextElementSibling().text();
				log.info(String.format("内容简介：%s", bookSumy));
			} else if (a_tr.html().contains("作者简介")) {
				String authorInfo = a_tr.nextElementSibling().text();
				log.info(String.format("作者简介：%s", authorInfo));
			}
		}

	}

	// 从种子URL分页提取书籍URL
	public static Set<String> takeBookUrl(Set<String> seeds) throws Exception {
		BDHttpParam hp = BDHttpParam.init().addCommon("sBrowType", "c").addCommon("iSortField", "7")
				.addCommon("sSortOrder", "desc").addCommon("iSno", "0").addHeader("Origin", "http://www.sdxjpc.com")
				.addHeader("Referer", "http://www.sdxjpc.com/scrp/bookcustomore.cfm").setCharset(NetUtil.CHARSET_GBK);
		Set<String> books = new HashSet<String>();
		for (String url : seeds) {
			log.info(String.format("准备抓取：%s", url));

			// 分页提取书籍URL
			for (int i = 1; i < SdxjpcConfig.DEF_PAGETOTAL; i++) {
				SdxjpcConfig.HTTP_PARAM.put(SdxjpcConfig.URL_P_PAGER, new Integer(i).toString());

				String src = BDHttpUtil.sendGet(url, hp);
				Document html = Jsoup.parse(src, url);

				// 根节点：书籍列表
				Element root = html.getElementsByClass("tblinput").first();
				Elements as = root.getElementsByTag("a");
				if (as != null && as.size() > 0) {
					log.info(String.format("提取第%d页书籍,请求参数：%s", i, SdxjpcConfig.HTTP_PARAM.toString()));
					for (Element a : as) {
						String a_href = a.absUrl("href");
						if (books.add(a_href)) {
							log.info(String.format("\t书籍地址：%s", a_href));
						}
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
