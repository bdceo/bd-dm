package com.bdsoft.datamin.fetch.jd;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.bdsoft.datamin.util.http.NetUtil;

/**
 * 京东部分解析测试
 *
 * @author   丁辰叶
 * @date	 2016-5-26
 * @version  1.0.0
 */
public class JDParseTest {

	public static void main(String[] args) throws Exception {
		String url = null;
		// 价格地址
		url = "http://p.3.cn/prices/mgets?skuIds=J_268661&callback=json";
		// System.out.println(url);

		// 评论地址
		Pattern repat = Pattern.compile(".*jsonp([\\d]+)&_=([\\d]+).*");
		url = "http://club.jd.com/productpage/p-880730-s-0-t-3-p-0.html?callback=jsonp1380961777453&_=1380961783024";
		Matcher rem = repat.matcher(url);
		rem.find();
		System.out.println("评论：" + url);
		showTime(rem.group(1));
		showTime(rem.group(2));
		diffTime(rem.group(2), rem.group(1));

		url = "http://club.jd.com/productpage/p-#ID#-s-0-t-3-p-#PAGE#.html?callback=jsonp#B#&_=#NOW#";
		url = url.replaceAll("#ID#", "880730");
		long now = System.currentTimeMillis();
		url = url.replaceAll("#NOW#", now + "");
		long b = now - (new Random().nextInt(500));
		url = url.replaceAll("#B#", b + "");
		url = url.replaceAll("#PAGE#", "0");
		loadRev(url);

	}

	public static void loadRev(String url) throws Exception {
		System.out.println("请求：" + url);
		String src = NetUtil.getHtmlSrc(url, NetUtil.CHARSET_GBK, null);
		System.out.println("数据：" + src.trim());
	}

	public static void showTime(String time) {
		Long tm = Long.parseLong(time);
		Date d = new Date(tm);
		SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS");
		System.out.println(sd.format(d));
	}

	public static void diffTime(String t1, String t2) {
		System.out.println("时间差：" + (Long.parseLong(t1) - Long.parseLong(t2)));
	}

}
