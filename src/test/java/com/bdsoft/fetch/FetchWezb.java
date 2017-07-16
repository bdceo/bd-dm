package com.bdsoft.fetch;

import java.io.File;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.bdsoft.datamin.util.BDFileUtil;
import com.bdsoft.datamin.util.BDLogUtil;
import com.bdsoft.datamin.util.http.BDHttpParam;
import com.bdsoft.datamin.util.http.BDHttpUtil;
import com.bdsoft.datamin.util.http.NetUtil;

public class FetchWezb {

	static {
		BDLogUtil.init();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		String baseUri = "http://www.wezb.cn";

		String index = "d:/download/wezb/index.html";
		Document html = Jsoup.parse(new File(index), NetUtil.CHARSET_UTF8);
		Elements lis = html.select("div.bd li");
		System.out.println(lis.size());
		for (Element li : lis) {
			String room = baseUri + li.select("a").first().attr("href");
			String name = li.select("figcaption").first().text();
			System.out.println(name + "\t地址\t" + room);
			String src = fetchSub(room);
			BDFileUtil.writeFile("d:/download/wezb/" + name + ".html", src,true);
			Thread.sleep(500);
		}

	}

	private static String fetchSub(String url) {
		BDHttpParam pm = BDHttpParam.init();
		pm.addCookie("CNZZDATA4800679",
				"cnzz_eid%3D1928198229-1470318567-%26ntime%3D1470318567");
		pm.addCookie(
				"ci_session",
				"a%3A4%3A%7Bs%3A10%3A%22session_id%22%3Bs%3A32%3A%227f17403894d45767c3e2a57b598c34e6%22%3Bs%3A10%3A%22ip_address%22%3Bs%3A13%3A%22117.100.93.92%22%3Bs%3A10%3A%22user_agent%22%3Bs%3A120%3A%22Mozilla%2F5.0+%28iPhone%3B+CPU+iPhone+OS+9_1+like+Mac+OS+X%29+AppleWebKit%2F601.1.46+%28KHTML%2C+like+Gecko%29+Version%2F9.0+Mobile%2F13B143%22%3Bs%3A13%3A%22last_activity%22%3Bi%3A1470323592%3B%7D63781747096da17f07d5433cfc9404d2");

		pm.addHeader(
				"User-Agent",
				"Mozilla/5.0 (iPhone; CPU iPhone OS 9_1 like Mac OS X) AppleWebKit/601.1.46 (KHTML, like Gecko) Version/9.0 Mobile/13B143 Safari/601.1");
		pm.addHeader("Referer", "http://www.wezb.cn/ms/index");
		pm.addHeader("Host", "www.wezb.cn");
		return BDHttpUtil.sendGet(url, pm);

	}

}
