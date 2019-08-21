package com.bdsoft.datamin.fetch.jd.cattegory;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.bdsoft.datamin.entity.JDQueueCats;
import com.bdsoft.datamin.fetch.jd.JdFetcher;
import com.bdsoft.datamin.util.BDFileUtil;
import com.bdsoft.datamin.util.http.NetUtil;

/**
 * 从分类页抓取商品入库，作为种子url
 * 
 * @author 丁辰叶
 * @date 2015-11-04
 * @date 2016-09-29
 */
public class FetchProductUrl extends JdFetcher {

	private static String ITEM_REP_FLAG = "#ID#";
	private static final String ITEM_BASE = "http://item.jd.com/#ID#.html";

	public static String SEED_FILE = "/home/dcy/tmp/seed2.txt";

	// 从分类地址页，提取商品URL
	public static void main(String[] args) {
		FetchProductUrl fpu = new FetchProductUrl();
		// 提取各分类首页商品，作为种子url
		List<JDQueueCats> data = fpu.jdqcMapper.selectFirstPageCat();
		System.out.println("待抓取分类页：" + data.size());

		for (JDQueueCats obj : data) {
			String url = obj.getUrl();
			Set<String> skuIds = new HashSet<String>();
			System.out.println("抓取分类页：" + url);
			try {
				Document html = NetUtil.getJsoupDocByGet(url, NetUtil.CHARSET_UTF8);
				if (html.getElementById("plist") != null) {
					Elements eles = html.select("div#plist ul li.gl-item");
					if (eles.size() > 0) {
						for (Element e : eles) {
							String sku = e.child(0).attr("data-sku");
							if (StringUtils.isNotEmpty(sku)) {
								skuIds.add(sku);
							}
						}
						formUrlFromSku(skuIds);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 组装商品url
	 * 
	 * @param skuIds
	 * @return
	 */
	public static Set<String> formUrlFromSku(Collection<String> skuIds) {
		Set<String> urls = new HashSet<String>();
		StringBuilder sb = new StringBuilder();
		for (String id : skuIds) {
			String url = ITEM_BASE.replaceAll(ITEM_REP_FLAG, id);
			System.out.println("\t提取：" + url);
			sb.append(url).append("\n");
			urls.add(url);
		}
		System.out.println("累计提取：" + urls.size());

		// 写入文件
		BDFileUtil.appendWrite(SEED_FILE, sb.toString().getBytes());
		System.out.println("写入文件：" + SEED_FILE);

		return urls;
	}

}
