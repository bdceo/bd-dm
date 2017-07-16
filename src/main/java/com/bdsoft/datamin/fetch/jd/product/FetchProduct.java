package com.bdsoft.datamin.fetch.jd.product;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.bdsoft.datamin.entity.JDProduct;
import com.bdsoft.datamin.entity.JDQueue;
import com.bdsoft.datamin.entity.ProductExt;
import com.bdsoft.datamin.fetch.jd.JdUtil;
import com.bdsoft.datamin.fetch.jd.feed.JDJson;
import com.bdsoft.datamin.fetch.jd.feed.JDProductFeed;
import com.bdsoft.datamin.fetch.jd.feed.JDVenderFeed;
import com.bdsoft.datamin.fetch.jd.review.FetchReview;
import com.bdsoft.datamin.mapper.JDProductMapper;
import com.bdsoft.datamin.mapper.JDVenderMapper;
import com.bdsoft.datamin.util.BDSpringUtil;
import com.bdsoft.datamin.util.StringUtil;
import com.bdsoft.datamin.util.http.BDHttpParam;
import com.bdsoft.datamin.util.http.BDHttpUtil;
import com.bdsoft.datamin.util.http.NetUtil;
import com.google.gson.Gson;

public class FetchProduct {

	private static JDProductMapper jdProductDao;
	private static JDVenderMapper jdVenderDao;

	public FetchProduct() {
	}

	static {
		BDSpringUtil.init();
		jdProductDao = BDSpringUtil.getBean(JDProductMapper.class);
		jdVenderDao = BDSpringUtil.getBean(JDVenderMapper.class);
	}

	/**
	 * 抓取商品及其卖家信息
	 */
	public static void main(String[] args) {
		Set<String> urls = new HashSet<String>();
//		urls.add("http://item.jd.com/268661.html");// 下架
//		urls.add("http://item.jd.com/1685716.html");// 上架
		urls.add("http://item.jd.com/11143153.html");// 书
//		urls.add("http://item.jd.com/1583901383.html");// 商户
		urls.add("http://item.jd.com/530848.html");// 新抓测试

		for (String url : urls) {
			// Utils.openBrowser(url); // 打开浏览器查看商品详情
			new FetchProduct().getProduct(url);
		}

	}

	/**
	 * 保存商品信息
	 * 
	 * @param jdp
	 */
	public JDProduct saveProduct(JDProductFeed jdp) {
		// 待入库商品信息
		JDProduct product = new ProductExt(jdp);
		// 查询是否已存在
		JDProduct dbp = new ProductExt(product.getPid());
		dbp = jdProductDao.selectOne(dbp);
		if (dbp == null) {
			try {
				jdProductDao.insertSelective(product);
				dbp = product;
				System.out.println("新商品入库成功>>" + product.getPurl());
			} catch (Exception e) {
				String msg = String.format("商品入库失败>>err=%s\turl=%s", e.getMessage(), product.getPurl());
				System.err.println(msg);
			}
		} else {
			System.out.println("更新商品>>" + product.getPurl());
			product.setId(dbp.getId());
			jdProductDao.updateById(product);
		}
		return dbp;
	}

	/**
	 * 通过SKUID获取商品卖家信息
	 * 
	 * @param skuid
	 * @param jdv
	 */
	public void takeVender(String skuid, JDVenderFeed jdv) {
		String url = JdUtil.formVenderUrl(skuid);
		System.out.println("\t商户地址：" + url);
		String src = "";
		// 获取卖家基本信息
		try {
			src = NetUtil.getHtmlSrc(url, NetUtil.CHARSET_GB2312);
			// System.out.println("\t卖家基本信息：" + src.trim());
			src = JDJson.subSafeJson(src, JDJson.BRACKET_1L);
			JDJson json = new Gson().fromJson(src, JDJson.class);
			// System.out.println("\t准备提取：" + json);
			String vid = json.getVid();
			String ven = json.getVender();
			String vurl = json.getUrl();
			jdv.setBaseInfo(vid, ven, vurl);
		} catch (Exception e) {
			System.out.println("获取卖家信息数据异常：" + url);
			e.printStackTrace();
		}

		// 获取卖家公司及所在地信息
		src = "";
		url = JdUtil.formVenderCmpUrl(jdv.getVid());
		try {
			src = NetUtil.getHtmlSrc(url, NetUtil.CHARSET_GB2312);

			// String purl = formUrlFromSku(skuid);
			// Map<String, String> header = new HashMap<String, String>();
			// FetchReview.addMutstHeader(header, purl);
			// src = NetUtil.getAjaxContent(url, Utils.CHARSET_GBK, header);

			// System.out.println("\t卖家公司信息：" + src.trim());
			src = JDJson.subSafeJson(src, JDJson.BRACKET_1L);

			JDJson json = new Gson().fromJson(src, JDJson.class);
			// System.out.println("\t准备提取：" + json);
			if (json != null) {
				String cmp = json.getCompanyName();
				String prv = json.getFirstAddr();
				String cty = json.getSecAddr();
				jdv.setCmpInfo(cmp, prv, cty);
			}
		} catch (Exception e) {
			System.out.println("解析卖家数据异常：" + url);
			e.printStackTrace();
		}
	}

	/**
	 * 供抓取队列调用入口，执行商品抓取
	 * 
	 * @param jdq
	 * @return
	 */
	public JDProduct fetch(JDQueue jdq) {
		return getProduct(jdq.getUrl());
	}

	/**
	 * 解析网页获取商品基本信息
	 * 
	 * @param url
	 *            商品详情页
	 * @return
	 */
	public JDProduct getProduct(String url) {
		System.out.println("\t抓取商品：" + url);
		JDProductFeed jdp = new JDProductFeed(url);
		try {
			String src = BDHttpUtil.sendGet(url, BDHttpParam.init().setCharset(NetUtil.CHARSET_GB2312));
			Document html = Jsoup.parse(src);

			// 提取商品名称
			String name = pickName(html);
			jdp.setName(name);

			// 提取商品ID
			String skuid = JdUtil.takeSkuid(url);
			jdp.setSkuid(skuid);

			// 提取商品价格
			String price = pickPrice(html, skuid);
			jdp.setPrice(price);

			// TODO 提取卖家信息，忽略
			jdp.setJdSell(1);

			// 提取分类信息
			pickCatInfo(jdp, html);

			// 获取评论总数
			int rc = new FetchReview().getReviewCount(url);
			jdp.setReviewCount(rc);

			System.out.println("\t商品详情：" + jdp);

			// 入库保存
			return saveProduct(jdp);
		} catch (Exception e) {
			System.err.println("抓取商品出错：" + e.getMessage());
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 提取商品名称
	 * 
	 * @param html
	 *            页面doc
	 * @return
	 */
	private String pickName(Document html) {
		Element ele = html.getElementById("name");
		if (ele == null) {
			ele = html.getElementsByClass("sku-name").first();
		} else {
			// 只提取大标题
			ele = ele.getElementsByTag("h1").first();
		}
		return ele.text().trim();
	}

	/**
	 * 提取商品价格信息
	 * 
	 * @param html
	 *            页面doc
	 * @param skuid
	 *            商品唯一码
	 * @return
	 */
	private String pickPrice(Document html, String skuid) {
		String price = "-1";
		Element ele = html.getElementById("summary-price");
		if (ele != null && !StringUtil.isEmpty(skuid)) {
			price = JdUtil.takePrice(skuid);
		}
		return price;
	}

	/**
	 * 提取商品分类信息
	 * 
	 * @param jdp
	 *            临时对象
	 * @param html
	 *            页面doc
	 */
	private void pickCatInfo(JDProductFeed jdp, Document html) {
		Elements eles = html.getElementsByClass("breadcrumb");
		if (eles.size() > 0) {
			Elements eles1 = eles.get(0).getElementsByTag("strong");
			if (eles1 != null && eles1.size() == 1) {
				String cat1 = eles1.get(0).text().trim();

				eles = eles.get(0).getElementsByTag("span").get(0).getElementsByTag("a");
				Element ele = eles.get(0);
				String cat2 = ele.text().trim();

				ele = eles.get(1);
				String cat3 = ele.text().trim();
				jdp.setCat(cat1, cat2, cat3);

				String tmp = ele.attr("href").trim();
				Matcher catM = JdUtil.CATG_PATTERN.matcher(tmp);
				if (catM.find()) {
					tmp = catM.group(1);
					jdp.setCatCode(tmp);
				}
			}
			return;
		}

		// 另一种分类导航风格
		eles = html.getElementsByClass("crumb-wrap");
		if (eles.size() > 0) {
			eles = eles.first().getElementsByClass("item");
			Element ele = eles.get(0);
			String cat1 = ele.text().trim();
			ele = eles.get(2);
			String cat2 = ele.text().trim();
			ele = eles.get(4);
			String cat3 = ele.text().trim();
			jdp.setCat(cat1, cat2, cat3);
			String tmp = ele.attr("href").trim();
			Matcher catM = JdUtil.CATG_PATTERN.matcher(tmp);
			if (catM.find()) {
				tmp = catM.group(1);
				jdp.setCatCode(tmp);
			}
		}

	}

}
