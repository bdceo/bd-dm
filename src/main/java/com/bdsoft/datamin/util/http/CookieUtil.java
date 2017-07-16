/**
 * CookieUtil.java
 * com.bdsoft.datamin.util.http
 * Copyright (c) 2014, 北京微课创景教育科技有限公司版权所有.
*/
package com.bdsoft.datamin.util.http;

import java.util.Iterator;
import java.util.Map;

import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;

/**
 * Cookie 处理工具
 * 
 * @author	丁辰叶
 * @date	2015-11-6
 */
public class CookieUtil {

	public static void main(String[] args) throws Exception {
		String cookie = "user-key=9598b78c-cfeb-4efa-b6bf-66ec722aaf63; __utmz=122270672.1446531357.1.1.utmcsr=jd.com|utmccn=(referral)|utmcmd=referral|utmcct=/allSort.aspx; jwotest_product=98; cn=0; _tp=KVl5NKRxAukOSDXosbc9XA%3D%3D; _pst=bdceo; TrackID=11aI38u3ARq3CL09PvpFfK1lj_JZQKrUQ2ssHezQApagVin_nkXH5xQkdIK7J6kyWgb6MKPHVUyM3Bh6sX8iIWQ; pinId=S6gSNir0Ryo; unick=bdceo; pin=bdceo; thor=53C975E2D860D491296707A7872559C21A1322C99F3D3FED5D58736280E6358B2CD460D8D6B30A14DC926EA4F1E25A0D3CB37F55C11763DF48FF358AC66125580CEC0A9B0305359F99380981F5AC8ABBACF471F3402103822399A07EC2901B3CDA73257BE44507B1D0CFDB4335C78C835C2C8701D71B29C4C35679549275524F; __jda=122270672.352832047.1446186710.1446775336.1446792530.10; __jdc=122270672; __jdv=122270672|direct|-|none|-; ipLocation=%u5317%u4EAC; areaId=1; ipLoc-djd=1-72-2799-0; __jdu=352832047";

		// 字符串分割，》数组
		// 数组单元素分割，》map
		Map<String, String> ckMap = parseCookie(cookie);
		System.out.println(ckMap);
	}

	public static Map<String, String> parseCookie(String cookie) {
		Map<String, String> ckMap = Maps.newHashMap();
		Iterator<String> cksi = Splitter.on(";").trimResults().omitEmptyStrings().split(cookie).iterator();
		while (cksi.hasNext()) {
			String ack = cksi.next();
			//			System.out.println("原="+ack);
			int eqidx = ack.indexOf("=");
			String key = ack.substring(0, eqidx);
			String value = ack.substring(++eqidx, ack.length());
			//			System.out.println("后="+key + "=" + value);
			if (!Strings.isNullOrEmpty(value)) {
				ckMap.put(key, value);
			}
		}
		return ckMap;
	}

}
