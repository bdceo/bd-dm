package com.bdsoft.datamin.fetch.ddc.jd;

import java.io.File;
import java.util.List;
import java.util.Random;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.bdsoft.datamin.fetch.ddc.DdcConfig;
import com.bdsoft.datamin.util.BDFileUtil;
import com.bdsoft.datamin.util.BDLogUtil;
import com.bdsoft.datamin.util.http.BDHttpParam;
import com.bdsoft.datamin.util.http.BDHttpUtil;
import com.bdsoft.datamin.util.http.NetUtil;
import com.google.common.collect.Lists;
import com.google.gson.Gson;

/**
 * 京东
 * 
 * @author 丁辰叶
 * @date 2016-8-6
 * @version 1.0.0
 */
public class JdFetcher {

	// 图片存储
	static String basePic = DdcConfig.BASE_STORE + "/home/ddc/jd/";

	// 官网
	static String baseUri = "http://www.jd.com/";

	// 大图域
	static String baseImgUri = "http://img12.360buyimg.com/n1/";

	// 商品列表
	static List<String> pdList = Lists.newArrayList();

	static {
		BDLogUtil.init();

		pdList.add("http://item.jd.com/1705028051.html"); // 小牛-F1
		pdList.add("http://item.jd.com/10492706527.html"); // 小牛-M1
	}

	/**
	 * 入口
	 */
	public static void main(String[] args) throws Exception {
		// 抓取商品页
		for (String pdUrl : pdList) {
			System.out.println("抓取商品：" + pdUrl);
			String src = BDHttpUtil.sendGet(pdUrl);
			Document html = Jsoup.parse(src);

			// 商品名称
			String name = html.select("div.sku-name").get(0).text();
			name = BDFileUtil.safeFileName(name);
			File pdDir = new File(basePic + File.separator + name);
			pdDir.mkdirs();

			// 商品图片
			int i = 1;
			Elements imgs = html.select("ul.lh img");
			for (Element img : imgs) {
				String imgUrl = baseImgUri + img.attr("data-url");
				System.out.println(imgUrl);
				String storeImg = pdDir.getAbsolutePath() + File.separator
						+ "图-" + (i++) + ".png";
				NetUtil.download(imgUrl, storeImg);
			}

			// 商品参数
			i = 1;
			StringBuilder info = new StringBuilder(pdUrl);
			info.append("\n参数\n");
			Elements infoLis = html.select("ul.p-parameter-list li");
			for (Element li : infoLis) {
				info.append(li.text()).append("\n");
			}
			String storeInfo = pdDir.getAbsolutePath() + File.separator + "参数"
					+ ".txt";
			BDFileUtil.writeFile(storeInfo, info.toString(), true);

			// 详情图片
			Elements infoImgs = fetchDesc(html, pdUrl);
			for (Element img : infoImgs) {
				String imgUrl = "http:" + img.attr("data-lazyload");
				String storeImg = pdDir.getAbsolutePath() + File.separator
						+ "详情-" + (i++) + ".png";
				NetUtil.download(imgUrl, storeImg);
				Thread.sleep(new Random().nextInt(1000));
			}
		}
		System.out.println("商品总数：" + pdList.size());
	}

	/**
	 * 获取商品详情图片
	 * 
	 * @param html
	 *            商品页内容
	 * @param url
	 *            商品地址
	 * @return
	 */
	static Elements fetchDesc(Document html, String url) {
		// 从js片段提取请求详情url
		String jsc = null;
		String infoUrl = null;
		Elements jss = html.getElementsByTag("script");
		for (Element js : jss) {
			if (js.html().contains("pageConfig")) { // 页面配置中提取
				jsc = js.html();
				break;
			}
		}
		jsc = jsc.substring(jsc.indexOf("desc:"));
		infoUrl = jsc.substring(jsc.indexOf("'") + 1, jsc.indexOf("',"));
		infoUrl = "http:" + infoUrl;
		// System.out.println("详情：" + infoUrl);

		// 请求详情内容
		BDHttpParam hp = BDHttpParam.init();
		hp.addHeader("Referer", url);
		String desc = BDHttpUtil.sendGet(infoUrl, hp);

		// 提取内容片段，提取img标签
		desc = desc.substring(desc.indexOf("(") + 1, desc.lastIndexOf(")"));
		System.out.println("详情片段：" + desc);
		JdDesc jd = new Gson().fromJson(desc, JdDesc.class);
		Document dhtml = Jsoup.parse(jd.getContent());
		return dhtml.getElementsByTag("img");
	}

	class JdDesc {
		String date;
		String content;

		public String getDate() {
			return date;
		}

		public void setDate(String date) {
			this.date = date;
		}

		public String getContent() {
			return content;
		}

		public void setContent(String content) {
			this.content = content;
		}

	}
}
