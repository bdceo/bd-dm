package com.bdsoft.fetch;

import java.io.File;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.bdsoft.datamin.util.BDLogUtil;
import com.bdsoft.datamin.util.http.BDHttpParam;
import com.bdsoft.datamin.util.http.BDHttpUtil;
import com.bdsoft.datamin.util.http.NetUtil;
import com.google.gson.Gson;

public class JdPdDescTest {

	static {
		BDLogUtil.init();
	}

	public static void main(String[] args) throws Exception {
		String url = "http://item.jd.com/10492706527.html";

		String path = "/home/ddc/jd.html";

		String jsctx = null;
		Document doc = Jsoup.parse(new File(path), NetUtil.CHARSET_UTF8);
		Elements jss = doc.getElementsByTag("script");
		for (Element js : jss) {
			if (js.html().contains("pageConfig")) {
				jsctx = js.html();
				break;
			}
		}
		System.out.println(jsctx);
		System.out.println("---------------------------\n\n");

		jsctx = jsctx.substring(jsctx.indexOf("desc:"));
		String descUrl = "http:"
				+ jsctx.substring(jsctx.indexOf("'") + 1, jsctx.indexOf("',"));
		System.out.println(jsctx);
		System.out.println("---------------------------\n\n");

		BDHttpParam hp = BDHttpParam.init();
		hp.addHeader("Referer", url);
		String src = BDHttpUtil.sendGet(descUrl, hp);
		src = src.substring(src.indexOf("(") + 1, src.indexOf(")"));
		// System.out.println(src);
		JdDesc desc = new Gson().fromJson(src, JdDesc.class);
		System.out.println(desc.getContent());

		Document dhtml = Jsoup.parse(desc.getContent());
		Elements imgs =  dhtml.getElementsByTag("img");
		for(Element img : imgs){
			System.out.println(img.attr("data-lazyload"));
		}
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
