package com.bdsoft.datamin.fetch.jd.cattegory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.bdsoft.datamin.entity.JDQueueCats;
import com.bdsoft.datamin.fetch.jd.JdFetcher;
import com.bdsoft.datamin.fetch.jd.JdUtil;
import com.bdsoft.datamin.util.http.BDHttpParam;
import com.bdsoft.datamin.util.http.BDHttpUtil;
import com.bdsoft.datamin.util.http.NetUtil;

/**
 * 抓取分类地址入库
 * 
 * @author 丁辰叶
 * @date 2015-11-3
 */
public class FetchCattegory extends JdFetcher {

	// 京东总分类页
	private static String JD_CATS_HOME = "http://www.jd.com/allSort.aspx";

	// 请求时间间隔
	private static long THREAD_CATS_SLEEP = 300L;

	/**
	 * 入口
	 */
	public static void main(String[] args) {
		FetchCattegory fc = new FetchCattegory();
		// 解析分类首页
		Map<String, List<String>> catMap = parseCatsHome();

		System.out.println("处理标准三级分类：" + catMap.get("std").size());
		fc.handleStdCat(catMap.get("std"), false);

		System.out.println("处理二级分类：" + catMap.get("scd").size());
		fc.handleScdCat(catMap.get("scd"), false);

		System.out.println("处理运营分类：" + catMap.get("ops").size());
		fc.handleOpsCat(catMap.get("ops"), false);
	}

	// 处理三级分类，分页url，入库
	// 标准分类
	public void handleStdCat(List<String> urls, boolean test) {
		Set<String> errCat = new HashSet<String>();
		Pattern pat = Pattern.compile("([\\d]+,[\\d]+,[\\d]+)");
		Matcher mat = null;
		for (String url : urls) {
			url = JdUtil.formStdCatUrl(url);
			System.out.println("处理分类：" + url);
			try {
				Document html = NetUtil.getJsoupDocByGet(url, NetUtil.CHARSET_GBK);
				Elements eles = html.getElementsByClass("fp-text"); // 总页数指示区域
				if (eles != null && eles.size() == 1) {
					String text = eles.get(0).text();
					String cp = text.split("/")[0];
					String tp = text.split("/")[1];
					System.out.println("\t分页：" + cp + "-" + tp);
					int total = Integer.parseInt(tp);
					mat = pat.matcher(url);
					if (mat.find()) {
						String cat = mat.group(1);
						System.out.println("\t分类：" + cat);
						for (int i = 1; i <= total; i++) {
							String purl = url + "&page=" + i;
							System.out.println("\t地址：" + purl);
							JDQueueCats jdqc = new JDQueueCats();
							jdqc.setUrl(purl.trim());
							jdqc = jdqcMapper.selectOne(jdqc);
							if (jdqc == null) {
								jdqc = new JDQueueCats(purl);
								jdqcMapper.insertSelective(jdqc);
							} else {
								System.out.println("URL已存在 >> " + jdqc.getId());
							}
						}
					} else {
						errCat.add(url);
					}
				} else {
					errCat.add(url);
				}
				Thread.sleep(THREAD_CATS_SLEEP);
			} catch (Exception e) {
				e.printStackTrace();
				errCat.add(url);
			}
		}
		System.out.println("处理失败：" + errCat.toString());
	}

	// 二级分类
	public void handleScdCat(List<String> urls, boolean test) {
		if (test) {
			urls = new ArrayList<String>();
			urls.add("http://channel.jd.com/1713-3258.html");// 小说
			urls.add("http://channel.jd.com/6994-6995.html");// 宠物
		}
		Set<String> errCat = new HashSet<String>();
		Set<String> tirCat = new HashSet<String>();
		// 请求二级分类页，提取三级分类
		for (String url : urls) {
			System.out.println("处理分类：" + url);
			Set<String> tmpCat = new HashSet<String>();
			try {
				Document html = NetUtil.getJsoupDocByGet(url, NetUtil.CHARSET_GBK);
				Elements eles = html.getElementById("sortlist").getElementsByTag("li"); // 分类列表中的项
				if (eles != null && eles.size() > 0) {
					for (Element ele : eles) {
						String turl = ele.getElementsByTag("a").get(0).attr("href");
						if (turl.startsWith("//")) {
							turl = "http:" + turl;
						}
						System.out.println("\t提取三级分类：" + turl);
						tmpCat.add(turl);
					}
					System.out.println("\t三级分类总数：" + tmpCat.size());
				} else {
					errCat.add(url);
				}
				Thread.sleep(THREAD_CATS_SLEEP);
			} catch (Exception e) {
				e.printStackTrace();
				errCat.add(url);
			}
			tirCat.addAll(tmpCat);
		}
		// 处理三级分类
		System.out.println(">>处理三级分类");
		urls = new ArrayList<String>();
		urls.addAll(tirCat);
		handleStdCat(urls, test);
	}

	// 运营分类
	public void handleOpsCat(List<String> urls, boolean test) {
		if (test) {
			urls = new ArrayList<String>();
			urls.add("http://www.jd.com/children/3402.html");// 0-3岁
			urls.add("http://www.jd.com/children/3405.html");// 11-14岁
		}
		Set<String> errCat = new HashSet<String>();
		Set<String> tirCat = new HashSet<String>();
		// 请求二级分类页，提取三级分类
		for (String url : urls) {
			System.out.println("处理分类：" + url);
			Set<String> tmpCat = new HashSet<String>();
			try {
				Document html = NetUtil.getJsoupDocByGet(url, NetUtil.CHARSET_GBK);
				Elements eles = html.getElementById("sortlist").getElementsByClass("mc");
				if (eles != null && eles.size() == 1) {
					eles = eles.get(0).getElementsByTag("li"); // 分类列表中的项
					for (Element ele : eles) {
						String turl = ele.getElementsByTag("a").get(0).attr("href");
						if (turl.startsWith("//")) {
							turl = "http:" + turl;
						}
						System.out.println("\t提取三级分类：" + turl);
						tmpCat.add(turl);
					}
					System.out.println("\t三级分类总数：" + tmpCat.size());
				} else {
					errCat.add(url);
				}
				Thread.sleep(THREAD_CATS_SLEEP);
			} catch (Exception e) {
				e.printStackTrace();
				errCat.add(url);
			}
			tirCat.addAll(tmpCat);
		}
		// 处理三级分类
		System.out.println(">>处理三级分类");
		urls = new ArrayList<String>();
		urls.addAll(tirCat);
		handleStdCat(urls, test);
	}

	// 解析京东总分类页
	public static Map<String, List<String>> parseCatsHome() {
		Map<String, List<String>> catMap = new HashMap<String, List<String>>();
		String filterHostReg = ".*(caipiao|piao|hotel|tuan|chongzhi|movie|zuche|dujia|huoche|book|e|mvd).jd.com.*";
		try {
			// String src = NetUtil.getHtmlSrc(JD_CATS_HOME,
			// NetUtil.CHARSET_UTF8);
			String src = BDHttpUtil.sendGet(JD_CATS_HOME, BDHttpParam.init().setCharset(NetUtil.CHARSET_UTF8));
			Document doc = Jsoup.parse(src);
			// 一级分类
			Elements cats1 = doc.getElementsByClass("category-item");

			List<String> opUrl = new ArrayList<String>();// 三级分类，是运营页面，单独解析
			List<String> scdUrl = new ArrayList<String>();// 二级分类页，单独解析
			List<String> catUrl = new ArrayList<String>();// 正规三级分类页

			// 遍历一级分类
			for (int i = 0; i < cats1.size(); i++) {
				// 获取一级分类名称
				Element cat1Ele = cats1.get(i);
				String cat1Name = cat1Ele.getElementsByTag("h2").get(0).text();
				// 过滤不抓去的一级分类
				if (cat1Name.matches(".*(图书|彩票).*")) {
					System.out.println(cat1Name + "\t一级分类不处理");
					continue;
				}

				// 二级分类
				Elements cats2 = cat1Ele.getElementsByClass("mc").get(0).getElementsByTag("dl");
				for (int j = 0; j < cats2.size(); j++) {
					Element cat2Ele = cats2.get(j);
					String cat2Name = cat2Ele.getElementsByTag("dt").get(0).text();
					// 过滤部分二级分类不抓取
					if (cat2Name.contains("xxx")) {
						System.out.println(cat2Name + "\t二级分类不处理");
						continue;
					}

					// 三级分类
					Elements cats3 = cat2Ele.getElementsByTag("dd").get(0).getElementsByTag("a");
					for (int k = 0; k < cats3.size(); k++) {
						Element cat3Ele = cats3.get(k);
						String cat3Name = cat3Ele.text();
						String href = cat3Ele.attr("href");
						if (href.startsWith("//")) {
							href = "http:" + href;
						}
						if (href.matches(filterHostReg)) {
							System.out.println(cat3Name + " 不处理\t" + href);
							continue;
						}
						if (href.indexOf("channel.jd.com") > 0) {
							scdUrl.add(href);// 二级分类页
							continue;
						}
						if (href.indexOf("list.jd.com") < 0) {
							opUrl.add(href); // 运营页面
							continue;
						}
						catUrl.add(href); // 标准三级分类
					}
				}
			}
			System.out.println("运营分类页：" + opUrl.size());
			System.out.println("二级分类页" + scdUrl.size());
			System.out.println("标准三级分页类" + catUrl.size());
			catMap.put("ops", opUrl);
			catMap.put("scd", scdUrl);
			catMap.put("std", catUrl);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return catMap;
	}

}
