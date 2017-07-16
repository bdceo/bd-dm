/**
 * LvNengFetcher.java
 * com.bdsoft.datamin.fetch.ddc.lvneng
 * Copyright (c) 2016, 北京微课创景教育科技有限公司版权所有.
 */

package com.bdsoft.datamin.fetch.ddc.lvneng;

import java.io.File;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.bdsoft.datamin.fetch.ddc.DdcConfig;
import com.bdsoft.datamin.util.BDFileUtil;
import com.bdsoft.datamin.util.BDLogUtil;
import com.bdsoft.datamin.util.http.BDHttpUtil;
import com.bdsoft.datamin.util.http.NetUtil;
import com.google.common.collect.Sets;

/**
 * 绿能电动车官网
 * 
 * @author 丁辰叶
 * @date 2016-7-8
 * @version 1.0.0
 */
public class LvNengFetcher {

	// 图片存储
	static String basePic = DdcConfig.BASE_STORE + "/home/ddc/lvneng/";

	// 官网
	static String baseUri = "http://www.lvneng.com/";
	// 产品列表
	static String pdList = "http://www.lvneng.com/products.asp?Action=Search1";

	static {
		BDLogUtil.init();
	}

	/**
	 * 入口
	 */
	public static void main(String[] args) throws Exception {
		// 抓取商品中心
		String src = BDHttpUtil.sendGet(pdList);
		Document html = Jsoup.parse(src, baseUri);

		Set<String> pdList = Sets.newHashSet();

		// 提取产品中心分类信息入口地址
		Elements areaCat = html.select("map#Map area");
		System.out.println("商品分类款数：" + areaCat.size());
		for (Element area : areaCat) {
			String href = area.attr("abs:href");
			if (href.endsWith("#")) {
				continue;
			}

			// 抓取分类入口提取系列或商品
			System.out.println("提取分类入口：" + href);
			html = Jsoup.parse(BDHttpUtil.sendGet(href), baseUri);
			Elements divXl = html.select("div.Pro_Class_Bg");
			// 系列列表
			if (divXl.size() > 0) {
				System.out.println("--系列列表");
				for (Element div : divXl) {
					String xlUrl = div.select("a.image_rollover_bottom").eq(0).attr("abs:href");
					System.out.println("\t系列：" + xlUrl);
					src = BDHttpUtil.sendGet(xlUrl);
					html = Jsoup.parse(src, baseUri);
					pickPd(pdList, html);
				}
			}
			// 产品列表
			else {
				System.out.println("--商品列表");
				pickPd(pdList, html);

				Elements elePg = html.select("form#ListForm div[style=float:right] a");
				if (elePg.size() > 0) {
					elePg.remove(elePg.size() - 1);
					for (Element ele : elePg) {
						src = BDHttpUtil.sendGet("http://www.lvneng.com/products.asp" + ele.attr("href"));
						html = Jsoup.parse(src, baseUri);
						pickPd(pdList, html);
					}
				}
			}
		}

		// 抓取商品详情
		for (String pdUrl : pdList) {
			System.out.println("抓取商品：" + pdUrl);
			String id = pdUrl.substring(pdUrl.lastIndexOf("=") + 1);
			html = Jsoup.parse(BDHttpUtil.sendGet(pdUrl), baseUri);

			Element rootDiv = html.select("div[style='background:#f7f7f7'] div").get(0);

			// 提取商品分类
			Element siteDiv = rootDiv.child(2);
			Elements sitAs = siteDiv.getElementsByTag("a");
			String catName = sitAs.get(0).text();
			String xlName = sitAs.size() > 1 ? sitAs.get(1).text() : "默认";
			File pdDir = new File(basePic + catName + File.separator + xlName);
			pdDir.mkdirs();

			String name = BDFileUtil.safeFileName(rootDiv.child(7).text());
			System.out.println("商品：" + name);

			String info = rootDiv.select("div[style='width:359px; height:200px']").get(0).html();
			info = info.replaceAll("\n", "").replaceAll("<div class=\"pro_line\">\\s+", "")
					.replaceAll("</div>\\s*", "\n");
			info = pdUrl + "\n" + info;
			System.out.println("介绍：" + info);
			String storeInfo = pdDir.getAbsolutePath() + File.separator + name + ".txt";
			BDFileUtil.writeFile(storeInfo, info, true);

			html = Jsoup.parse(BDHttpUtil.sendGet("http://www.lvneng.com/2.asp?ProductID=" + id), baseUri);
			int i = 1;
			for (Element a : html.getElementsByTag("a")) {
				String fun = a.attr("onmouseover");
				String img = fun.substring(fun.indexOf(",") + 2, fun.length() - 3);
				String storeImg = pdDir.getAbsolutePath() + File.separator + name + "-" + (i++) + ".png";
				NetUtil.download("http://www.lvneng.com/Upload/product/" + img, storeImg);
				File storeFile = new File(storeImg);
				String umTmFile = BDFileUtil.setPngUnTm(storeFile);
				BDFileUtil.setImgZfx(StringUtils.isNotEmpty(umTmFile) ? new File(umTmFile) : storeFile, null);
			}
		}

		System.out.println("商品总数：" + pdList.size());
	}

	// 提取商品地址
	static void pickPd(Set<String> pdList, Document html) {
		Elements divPd = html.select("div.Pro_Bg");
		if (divPd.size() > 0) {
			for (Element div : divPd) {
				String pdUrl = "http://www.lvneng.com/products.asp" + div.select("div.Pro_Bg a").eq(0).attr("href");
				pdList.add(pdUrl);
				System.out.println("\t\t商品：" + pdUrl);
			}
		}
	}

}
