package com.bdsoft.datamin.fetch.ddc.dushifeng;

import java.io.File;
import java.net.URLEncoder;
import java.util.Set;

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
 * 都市风电动车官网
 * 
 * @author 丁辰叶
 * @date 2016-7-9
 * @version 1.0.0
 */
public class DuShiFengFetcher {

	// 图片存储
	static String basePic = DdcConfig.BASE_STORE + "/home/ddc/dushifeng/";

	// 官网
	static String baseUri = "http://www.tjmingjia.com/";
	// 产品列表
	static String pdsUrl = "http://www.tjmingjia.com/Pro.aspx?page=";

	// 总页数
	static int pageSize = 7;

	static {
		BDLogUtil.init();
	}

	/**
	 * 入口
	 */
	public static void main(String[] args) throws Exception {
		Set<String> pdList = Sets.newHashSet();
		// 抓取商品中心
		for (int i = 1; i <= pageSize; i++) {
			String src = BDHttpUtil.sendGet(pdsUrl + i);
			Document html = Jsoup.parse(src, baseUri);

			Elements xlAs = html.select("div.div_imgbg > a");
			for (Element a : xlAs) {
				pdList.add(a.attr("abs:href"));
			}
		}

		// 抓取商品页
		for (String pdUrl : pdList) {
			System.out.println("抓取商品：" + pdUrl);
			Document html = Jsoup.parse(BDHttpUtil.sendGet(pdUrl), baseUri);

			// 系列
			String xlName = html.select("span.span01").get(0).text();
			System.out.println(xlName);
			File pdDir = new File(basePic + xlName);
			pdDir.mkdirs();

			// 商品id
			String id = pdUrl.substring(pdUrl.lastIndexOf("=") + 1);
			System.out.println("商品：" + id);

			// 介绍
			Elements infoSpan = html.select("div.pro03 span");
			String info = pdUrl + "\n技术参数\n";
			for (Element is : infoSpan) {
				info += is.text() + "\n";
			}
			info += "\n细节描述\n";
			Elements infoStrong = html.select("div#tagContent0 strong");
			for (Element is : infoStrong) {
				String txt = is.text().trim();
				info += txt + (txt.length() > 1 ? "\n" : "");
			}
			
			System.out.println("介绍：" + info);
			String storeInfo = pdDir.getAbsolutePath() + File.separator + id
					+ ".txt";
			BDFileUtil.writeFile(storeInfo, info, true);

			Elements imgEles = html.select("a.a_bigImg > img");
			int i = 1;
			for (Element img : imgEles) {
				String storeImg = pdDir.getAbsolutePath() + File.separator + id
						+ "-" + (i++) + ".png";
				String src = img.attr("src");
				if (src.endsWith("jpg") || src.endsWith("png")) {
					src = src.substring(src.indexOf("/") + 1,
							src.lastIndexOf("."));
					src = baseUri + "Upimg/" + URLEncoder.encode(src) + ".jpg";
					NetUtil.download(src, storeImg);
					File storeFile = new File(storeImg);
					BDFileUtil.setPngUnTm(storeFile);
					BDFileUtil.setImgZfx(storeFile);
				}
			}
		}
		System.out.println("商品总数："+pdList.size());
	}

}
