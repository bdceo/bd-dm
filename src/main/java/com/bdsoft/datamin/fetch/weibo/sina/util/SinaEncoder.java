package com.bdsoft.datamin.fetch.weibo.sina.util;

import java.net.URLEncoder;

import com.bdsoft.datamin.fetch.weibo.sina.FetchWeibo;

public class SinaEncoder {
	private StringBuffer parame = new StringBuffer();

	public SinaEncoder(String value) {
		encode(value);
	}

	public SinaEncoder(String name, String value) {
		encode(name, value);
	}

	public synchronized void add(String name, String value) {
		parame.append("&");
		encode(name, value);
	}

	private synchronized void encode(String value) {
		parame.append(sinaEncode(value));
	}

	private synchronized void encode(String name, String value) {
		parame.append(sinaEncode(name));
		parame.append("=");
		parame.append(sinaEncode(value));
	}

	public String getParame() {
		return parame.toString();
	}

	public String toString() {
		return getParame();
	}

	public String sinaEncode(String url) {
		try {
			String first = URLEncoder.encode(url, FetchWeibo.CHARSET);
			first = first.replaceAll("\\+", "%20");
			return URLEncoder.encode(first, FetchWeibo.CHARSET);
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
}
