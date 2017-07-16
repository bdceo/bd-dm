/**
 * LvYuanFetcher.java
 * com.bdsoft.datamin.fetch.ddc.lvyuan
 * Copyright (c) 2016, 北京微课创景教育科技有限公司版权所有.
 */

package com.bdsoft.datamin.fetch.ddc.lvyuan;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.bdsoft.datamin.fetch.ddc.DdcCat;
import com.bdsoft.datamin.fetch.ddc.DdcConfig;
import com.bdsoft.datamin.util.BDFileUtil;
import com.bdsoft.datamin.util.BDLogUtil;
import com.bdsoft.datamin.util.http.BDHttpUtil;
import com.bdsoft.datamin.util.http.NetUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * 绿源电动车官网
 * 
 * @author 丁辰叶
 * @date 2016-7-8
 * @version 1.0.0
 */
public class LvYuanFetcher {

	// 图片存储
	static String basePic = DdcConfig.BASE_STORE + "/home/ddc/lvyuan/";

	// 官网
	static String baseUri = "http://www.luyuan.cn";
	// 产品列表
	static String pdList = "http://www.luyuan.cn/pro_list.html";
	// 分类映射
	static Map<String, String> catMap = Maps.newHashMap();

	static {
		BDLogUtil.init();
		catMap.put("hy", "豪华款");
		catMap.put("jy", "简易款");
		catMap.put("ld", "锂电款");
		catMap.put("tz", "特种车");
	}

	/**
	 * 入口
	 */
	public static void main(String[] args) throws Exception {
		// 抓取商品中心
		String src = BDHttpUtil.sendGet(pdList);
		Document html = Jsoup.parse(src, baseUri);

		// 提取分类信息及入口地址
		List<DdcCat> catList = Lists.newArrayList();
		Elements divCat = html.select("div.pro_list");
		System.out.println("商品分类款数：" + divCat.size());
		for (Element div : divCat) {
			String catId = div.attr("id");
			String catName = catMap.get(catId);
			System.out.println("款式：" + catName);

			Elements divXl = div.select("div.three");
			for (Element xl : divXl) {
				String xlName = xl.select("div.pro_bo_text h2").get(0).text();
				String xlUrl = xl.select("div.pro_bo a").get(0).attr("abs:href");
				System.out.println("\t系列：" + xlName + "\t" + xlUrl);
				catList.add(new DdcCat(catName, xlName, xlUrl));
			}
		}

		// 抓取分类页商品列表
		int pdSize = 0;
		for (DdcCat cat : catList) {
			String xlUrl = cat.getXlUrl();
			String id = xlUrl.substring(xlUrl.lastIndexOf("_") + 1, xlUrl.lastIndexOf("."));
			String pgUrl = "http://www.luyuan.cn/pro_listlb";
			for (int i = 1; i < 3; i++) {
				xlUrl = pgUrl + "-" + i + "-" + id + ".html";
				fetchPdList(cat, xlUrl);
			}
			System.out.println(cat);

			// 商品信息目录
			File pdDir = new File(basePic + cat.getCatName() + File.separator + cat.getXlName());
			pdDir.mkdirs();

			// 抓取商品
			pdSize += cat.getPdList().size();
			for (String pdUrl : cat.getPdList()) {
				html = Jsoup.parse(BDHttpUtil.sendGet(pdUrl), baseUri);

				String name = html.select("div.sec1_pro_text h3").eq(0).text();
				name += html.select("div.sec1_pro_text h2").eq(0).text();
				System.out.println("商品：" + name);
				String info = html.select("div.sec1_pro_text p").eq(0).html();
				info = info.replaceAll("<br> ", "\n");
				info = pdUrl + "\n" + info;
				System.out.println("介绍：" + info);
				String storeInfo = pdDir.getAbsolutePath() + File.separator + name + ".txt";
				BDFileUtil.writeFile(storeInfo, info, true);

				Elements imgList = html.select("div.sec1_pro_img img");
				int i = 1;
				for (Element img : imgList) {
					String storeImg = pdDir.getAbsolutePath() + File.separator + name + "-" + (i++) + ".png";
					if (BDHttpUtil.is200(img.attr("abs:src"))) {
						NetUtil.download(img.attr("abs:src"), storeImg);
						File storeFile = new File(storeImg);
						String umTmFile = BDFileUtil.setPngUnTm(storeFile);
						BDFileUtil.setImgZfx(StringUtils.isNotEmpty(umTmFile) ? new File(umTmFile) : storeFile);
					}
				}
			}
		}
		System.out.println("商品总数：" + pdSize);
	}

	// 抓取商品列表，提取商品url
	static void fetchPdList(DdcCat cat, String xlUrl) {
		System.out.println("商品列表：" + xlUrl);
		String src = BDHttpUtil.sendGet(xlUrl);
		Document html = Jsoup.parse(src, baseUri);
		// 商品url
		Elements aPd = html.select("div.row div.cp2 a");
		for (Element a : aPd) {
			cat.addPd(a.attr("abs:href"));
		}
	}

}
