package com.bdsoft.datamin.fetch.ddc.tsinova;

import java.io.File;
import java.util.List;

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
import com.google.common.collect.Lists;

/**
 * 轻客电动车官网
 * 
 * @author 丁辰叶
 * @date 2016-8-4
 * @version 1.0.0
 */
public class TsinovaFetcher {

	// 图片存储
	static String basePic = DdcConfig.BASE_STORE + "/home/ddc/tsinova/";

	// 官网
	static String baseUri = "https://www.tsinova.com";

	// 产品列表
	static List<String> pdList = Lists.newArrayList();

	static {
		BDLogUtil.init();

		pdList.add("https://www.tsinova.com/web/products/2016041599");
		pdList.add("https://www.tsinova.com/web/products/2016041518");
	}

	/**
	 * 入口
	 */
	public static void main(String[] args) throws Exception {
		// 抓取商品页
		for (String pdUrl : pdList) {
			System.out.println("抓取商品：" + pdUrl);
			Document html = Jsoup.parse(BDHttpUtil.sendGet(pdUrl), baseUri);

			// 商品名称
			String name = html.select("div.goods-options-hd h2").get(0).text();
			System.out.println("商品：" + name);

			File pdDir = new File(basePic + File.separator + name);
			pdDir.mkdirs();
			File dtDir = new File(basePic + File.separator + name
					+ File.separator + "详情");
			dtDir.mkdirs();

			// 图片
			int i = 1;
			Elements imgs = html.select("ul.img-list img");
			for (Element img : imgs) {
				String imgUrl = img.attr("abs:src");
				System.out.println(imgUrl);
				String storeImg = pdDir.getAbsolutePath() + File.separator
						+ name + "-" + (i++) + ".png";
				NetUtil.download(imgUrl, storeImg);
				File storeFile = new File(storeImg);
				String umTmFile = BDFileUtil.setPngUnTm(storeFile);
				BDFileUtil.setImgZfx(
						StringUtils.isNotEmpty(umTmFile) ? new File(umTmFile)
								: storeFile, null);
			}

			// 详情
			i = 1;
			Elements infoImgs = html.select("div.good-detail-box img");
			for (Element img : infoImgs) {
				String imgUrl = img.attr("abs:src");
				System.out.println(imgUrl);
				String storeImg = dtDir.getAbsolutePath() + File.separator
						+ (i++) + ".png";
				NetUtil.download(imgUrl, storeImg);
			}

		}
		System.out.println("商品总数：" + pdList.size());
	}

}

class TaiLingImg {
	String largeimage;

	public String getLargeimage() {
		return largeimage;
	}

	public void setLargeimage(String largeimage) {
		this.largeimage = largeimage;
	}

}
