/**
 * VkoFree.java
 * com.bdsoft.datamin.www
 * Copyright (c) 2014, 北京微课创景教育科技有限公司版权所有.
*/
package com.bdsoft.datamin.fetch.www.vko;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.bdsoft.datamin.util.http.NetUtil;

/**
 * 
 * @author	丁辰叶
 * @date	2015-4-2
 */
public class VkoFree {

	public static void main(String[] args) {
		String url = "http://www.vko.cn/freev8.html";
		Document html = NetUtil.getJsoupDocByGet(url, NetUtil.CHARSET_UTF8);

		List<String> cgList = new ArrayList<String>();
		Element higEle = html.getElementById("hig_school");
		cgList.addAll(picImg(higEle));

		Element midEle = html.getElementById("mid_school");
		cgList.addAll(picImg(midEle));
		ptf(cgList, "初高中");

		Element smaEle = html.getElementById("sma_school");
		List<String> xxList = picImg(smaEle);
		//		ptf(xxList, "小学");
	}

	private static void ptf(List<String> list, String title) {
		String sql = "select * from video_url where videoNo in #vn and storeType=3;";
		//		System.out.println("\n" + title + ":" + list.size());

		for (String cno : list) {
			System.out.println(cno.split("#")[0] + "\t" + cno.split("#")[1]);
		}

		StringBuilder sqlb = new StringBuilder("(");
		for (String cno : list) {
			sqlb.append("'").append(cno.split("#")[0]).append("',");
		}
		sqlb.append(")");

		sql = sql.replaceAll("#vn", sqlb.toString());
		System.out.println(sql);
	}

	private static List<String> picImg(Element ele) {
		List<String> cnoList = new ArrayList<String>();
		Elements imgs = ele.getElementsByTag("img");
		for (Element img : imgs) {
			String src = img.attr("src");
			src = src.substring(src.lastIndexOf("/") + 1, src.lastIndexOf("."));

			String uv = img.parent().attr("uv");

			cnoList.add(src + "#" + uv);

			//			cnoList.add(src);
		}
		return cnoList;
	}
}
