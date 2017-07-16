package com.bdsoft.datamin.fetch.ddc.tailing;

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
import com.google.gson.Gson;

/**
 * 台铃电动车官网
 * 
 * @author 丁辰叶
 * @date 2016-7-9
 * @version 1.0.0
 */
public class TaiLingFetcher {

	// 图片存储
	static String basePic = DdcConfig.BASE_STORE + "/home/ddc/tailing/";

	// 官网
	static String baseUri = "http://www.tailg.com.cn";

	static {
		BDLogUtil.init();
	}

	/**
	 * 入口
	 */
	public static void main(String[] args) throws Exception {
		// 首页提取分类入口页
		String src = BDHttpUtil.sendGet(baseUri);
		Document html = Jsoup.parse(src, baseUri);

		Set<String> pdList = Sets.newHashSet();

		Elements xlAs = html.select("ul.second_list a");
		for (Element xla : xlAs) {
			String href = xla.attr("abs:href");
			if (href.contains("product")) {
				System.out.println("商品列表：" + href);

				// 遍历分类列表，提取商品地址
				for (int i = 1; i < 3; i++) {
					System.out.println("列表分页：" + href + "-" + i);
					src = BDHttpUtil.sendGet(href + "-" + i);
					html = Jsoup.parse(src, baseUri);
					Elements pdAs = html.select("ul.car_list > li > a");
					if (pdAs.size() > 0) {
						for (Element pd : pdAs) {
							System.out.println("\t++商品：" + pd.attr("abs:href"));
							pdList.add(pd.attr("abs:href"));
						}
					}
				}
			}
		}

		// 抓取商品页
		for (String pdUrl : pdList) {
			System.out.println("抓取商品：" + pdUrl);
			html = Jsoup.parse(BDHttpUtil.sendGet(pdUrl), baseUri);

			// 分类信息
			String siteMap = html.select("P.location").get(0).text();
			String[] xls = siteMap.split(">");
			String catName = xls[2].trim();
			String xlName = xls[3].trim();
			System.out.println("分类：" + catName + "-" + xlName);
			File pdDir = new File(basePic + catName + File.separator + xlName);
			pdDir.mkdirs();

			// 商品名称
			String name = xls[4].trim();
			System.out.println("商品：" + name);

			// 介绍
			String info = pdUrl + "\n";
			Elements trInfo = html.select("div.dd_cont").get(1).getElementsByTag("tr");
			for (Element tr : trInfo) {
				if (tr.getElementsByTag("td").size() == 2) {
					info += tr.getElementsByTag("td").get(0).text() + ":" + tr.getElementsByTag("td").get(1).text()
							+ "\n";
				} else {
					String txt = tr.text();
					int i = txt.indexOf("技术参数");
					info += (i > 0 ? txt.substring(i) : txt) + "\n";
				}
			}
			info += "\n产品详情\n";
			Elements divInfo = html.select("div.left_cc");
			for (Element div : divInfo) {
				info += div.text() + "\n";
			}
			System.out.println("介绍:" + info);
			String storeInfo = pdDir.getAbsolutePath() + File.separator + name + ".txt";
			BDFileUtil.writeFile(storeInfo, info, true);

			// 图片
			int i = 1;
			Elements imgAs = html.select("div.linu_in a");
			for (Element img : imgAs) {
				TaiLingImg io = new Gson().fromJson(img.attr("rel"), TaiLingImg.class);
				String storeImg = pdDir.getAbsolutePath() + File.separator + name + "-" + (i++) + ".png";
				NetUtil.download(baseUri + io.getLargeimage(), storeImg);
				File storeFile = new File(storeImg);
				String umTmFile = BDFileUtil.setPngUnTm(storeFile);
				BDFileUtil.setImgZfx(StringUtils.isNotEmpty(umTmFile) ? new File(umTmFile) : storeFile, null);
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
