package com.bdsoft.datamin.fetch.lottery.ssq;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.bdsoft.datamin.fetch.lottery.LotteryFetcher;
import com.bdsoft.datamin.util.http.NetUtil;

// 双色球，中彩网抓取：http://www.zhcw.com/
public class SSQfromZHCW implements LotteryFetcher {

	private static final String SSQ_BASE = "http://kaijiang.zhcw.com/";

	private static final String WEB_CHARSET = "utf-8";

	private static String KAIJIANG = "http://kaijiang.zhcw.com/zhcw/html/ssq/list_#PAGE#.html";
	private static String PAGE_REG = "#PAGE#";// int 1-78

	private static int START = 1;
	private static int END = 78;

	public static void main(String[] args) {
		for (int i = START; i <= END; i++) {
			String url = KAIJIANG.replaceAll(PAGE_REG, i + "");
			String src = "";
			try {
				src = NetUtil.getHtmlSrc(url, WEB_CHARSET);
				System.out.println("抓取：" + url);
			} catch (Exception e) {
				System.out.println("抓取出错，地址：" + url);
				e.printStackTrace();
				continue;
			}
			Document html = Jsoup.parse(src);
			Elements eles = html.getElementsByTag("tbody");
			if (eles != null && eles.size() == 1) {
				try {
					Element tbody = eles.get(0);
					Elements trs = tbody.getElementsByTag("tr");
					if (trs != null && trs.size() > 2) {
						int size = trs.size() - 1;
						for (int j = 2; j < size; j++) {
							Element tr = trs.get(j);
							Elements tds = tr.getElementsByTag("td");
							Element td = tds.get(0);// 开奖日期
							String date = td.text();
							td = tds.get(1);// 期号
							String issue = td.text();
							td = tds.get(2);// 开奖号码
							String number = td.text().trim();
							td = tds.get(3);// 销售额
							String sales = td.text().replaceAll(",", "");
							td = tds.get(4);// 一等奖
							String w1 = td.getElementsByTag("strong").get(0)
									.text();
							td = tds.get(5);// 二等奖
							String w2 = td.text();
							System.out.println(date + "\t" + issue + "\t"
									+ number + "\t" + sales + "\t" + w1 + "\t"
									+ w2);
						}
					}
				} catch (Exception e) {
					System.out.println("解析出错，地址：" + url);
					e.printStackTrace();
				}
			}
		}
	}

}
