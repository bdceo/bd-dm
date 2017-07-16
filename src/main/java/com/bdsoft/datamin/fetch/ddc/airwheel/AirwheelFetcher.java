package com.bdsoft.datamin.fetch.ddc.airwheel;

import java.io.File;
import java.util.List;
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
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

/**
 * 爱尔威电动车官网
 * 
 * @author 丁辰叶
 * @date 2016-8-4
 * @version 1.0.0
 */
public class AirwheelFetcher {

	// 图片存储
	static String basePic = DdcConfig.BASE_STORE + "/home/ddc/airwheel/";

	// 官网
	static String baseUri = "http://www.airwheel.cn/";

	// 产品列表
	static String pdsUrl = "http://www.airwheel.cn/product.html";

	static {
		BDLogUtil.init();
	}

	/**
	 * 入口
	 */
	public static void main(String[] args) throws Exception {
		Set<String> pdList = Sets.newHashSet();
		// 抓取商品中心
		Document html = Jsoup.parse(BDHttpUtil.sendGet(pdsUrl), baseUri);
		Elements pdAs = html.select("div.inner_cp a");
		for (Element a : pdAs) {
			String pdUrl = a.attr("abs:href");
			// 商品名称
			String name = pdUrl.substring(pdUrl.lastIndexOf("/") + 1,
					pdUrl.lastIndexOf("."));
			File pdDir = new File(basePic + File.separator + name);
			pdDir.mkdirs();

			// 商品图片
			String imgUrl = a.select("img").get(0).attr("abs:src");
			System.out.println(imgUrl);
			String storeImg = pdDir.getAbsolutePath() + File.separator + name
					+ ".png";
			NetUtil.download(imgUrl, storeImg);
			File storeFile = new File(storeImg);
			String umTmFile = BDFileUtil.setPngUnTm(storeFile);
			BDFileUtil.setImgZfx(StringUtils.isNotEmpty(umTmFile) ? new File(
					umTmFile) : storeFile, null);

			pdList.add(pdUrl);
		}

		// 抓取商品页
		for (String pdUrl : pdList) {
			System.out.println("抓取商品：" + pdUrl);
			html = Jsoup.parse(BDHttpUtil.sendGet(pdUrl), baseUri);

			// 参数地址
			String infoUrl = null;
			Elements urlAs = html.select("div.top_menus a");
			for (Element a : urlAs) {
				// System.out.println(a.text() + "--" + a.attr("abs:href"));
				if (a.text().equals("参数")) {
					infoUrl = a.attr("abs:href");
					break;
				}
			}
			if (StringUtils.isEmpty(infoUrl)) {
				System.out.println("没有参数页");
				continue;
			}
			System.out.println("商品参数：" + infoUrl);
			html = Jsoup.parse(BDHttpUtil.sendGet(infoUrl), baseUri);

			// 商品名称
			String name = pdUrl.substring(pdUrl.lastIndexOf("/") + 1,
					pdUrl.lastIndexOf("."));
			File pdDir = new File(basePic + File.separator + name);

			// 详情参数
			int i = 1;
			Elements imgs = html.select("div.banner_s8_specs img");
			for (Element img : imgs) {
				String imgUrl = img.attr("abs:src");
				System.out.println(imgUrl);
				String storeImg = pdDir.getAbsolutePath() + File.separator
						+ "参数图-" + (i++) + ".png";
				NetUtil.download(imgUrl, storeImg);
			}

			// 详情
			StringBuilder info = new StringBuilder(pdUrl);
			info.append("\n").append(infoUrl).append("\n技术参数\n");
			Elements infoDivs = html.select("div.product-parameter-item");
			for (Element div : infoDivs) {
				String key = div.select("h6").get(0).text();
				String val = div.select("p").get(0).text();
				String style = div.select("p").get(0).attr("style");
				System.out.println(key + "," + val + "," + style);

				info.append(key).append(":");
				if (StringUtils.isNotEmpty(val)) {
					info.append(val);
				}
				if (StringUtils.isNotEmpty(style)) {
					// background-image:-webkit-image-set(url(images/airwheel_size-s3.jpg)
					// 1x, url(images/airwheel_size-s3.jpg) 2x);
					// background-image: url(images/Airwheel_Z3_spec-3.jpg);
					List<String> dtImgs = Lists.newArrayList();
					String[] styles = style.split(";");
					for (String st : styles) {
						if (st.startsWith("background-image")) {
							st = st.substring(st.indexOf("(") + 1,
									st.lastIndexOf(")"));
							if (st.contains(",")) {
								String[] sts = st.split(",");
								for (String s : sts) {
									s = s.substring(s.indexOf("(") + 1,
											s.indexOf(")"));
									dtImgs.add(baseUri + s);
								}
							} else {
								dtImgs.add(baseUri + st);
							}
							break;
						}
					}
					for (String img : dtImgs) {
						String storeImg = pdDir.getAbsolutePath()
								+ File.separator + "参数图-" + i + ".png";
						info.append("\t图-").append(i++);
						System.out.println("参数图：" + img);
						NetUtil.download(img, storeImg);
					}
				}
				info.append("\n");
			}

			String storeInfo = pdDir.getAbsolutePath() + File.separator + "参数"
					+ ".txt";
			BDFileUtil.writeFile(storeInfo, info.toString(), true);
		}
		System.out.println("商品总数：" + pdList.size());
	}
}
