package com.bdsoft.datamin.fetch.ddc.doohan;

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
 * 逗哈电动车官网
 * 
 * @author 丁辰叶
 * @date 2016-8-4
 * @version 1.0.0
 */
public class DoohanFetcher {

	// 图片存储
	static String basePic = DdcConfig.BASE_STORE + "/home/ddc/doohan/";

	// 官网
	static String baseUri = "http://www.doohan.cn/";

	// 产品列表
	static List<String> pdList = Lists.newArrayList();

	static {
		BDLogUtil.init();

		pdList.add("http://doohan.cn/Webmall/Index/item/id/Mw%3D%3D.html");
	}

	/**
	 * 入口
	 */
	public static void main(String[] args) throws Exception {
		// 抓取商品页
		for (String pdUrl : pdList) {
			System.out.println("抓取商品：" + pdUrl);
			Document html = Jsoup.parse(BDHttpUtil.sendGet(pdUrl), baseUri);

			File pdDir = new File(basePic);
			pdDir.mkdirs();
			File dtDir = new File(basePic + File.separator + "详情");
			dtDir.mkdirs();

			// 图片
			int i = 1;
			Elements imgs = html.select("ul.DB_thumMove a");
			for (Element img : imgs) {
				String imgUrl = img.attr("abs:href");
				System.out.println(imgUrl);
				String storeImg = pdDir.getAbsolutePath() + File.separator
						+ (i++) + ".png";
				NetUtil.download(imgUrl, storeImg);
				File storeFile = new File(storeImg);
				String umTmFile = BDFileUtil.setPngUnTm(storeFile);
				BDFileUtil.setImgZfx(
						StringUtils.isNotEmpty(umTmFile) ? new File(umTmFile)
								: storeFile, null);
			}

			// 详情
			i = 1;
			Elements infoImgs = html.select("div.item-info img");
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
