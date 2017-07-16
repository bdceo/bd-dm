package com.bdsoft.datamin.fetch.huxiu;

public class HuxiuConfig {

	public static String DEV_LANG = "PHP";

	public static String WEB_DOMAIN = "huxiu.com";
	public static String WEB_CHARSET = "UTF-8";

	// URL 参数
	public static String URL_P_PAGER = "#PAGER";

	public static int DEF_PAGESIZE = 0;// 每页数量
	public static int DEF_PAGETOTAL = 50;// 总页数

	public static void main(String[] args) throws Exception {
	}

	// 拼接分页地址
	public static String getPagerUrl(String url, int page) {
		return url.replaceAll(URL_P_PAGER, new Integer(page).toString());
	}

}
