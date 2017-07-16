package com.bdsoft.datamin.fetch.duke;

/**
 * 读客图书抓取配置
 *  
 * @author 丁辰叶
 * @date 2014-7-18
 */
public class DukeConfig {

	public static String DEV_LANG = "PHP";

	public static String WEB_DOMAIN = "dookbook.com";
	public static String WEB_CHARSET = "UTF-8";

	// URL 参数
	public static String URL_P_PAGER = "page";
	public static String URL_P_PAGESIZE = "page_size";

	public static int DEF_PAGESIZE = 20;// 每页数量
	public static int DEF_PAGETOTAL = 10;// 总页数 

	// 拼接分页地址
	public static String getPagerUrl(String url, int page) {
		StringBuilder sb = new StringBuilder(url);

		String pre = (url.indexOf("?") > 0) ? "&" : "?";
		sb.append(pre).append(URL_P_PAGER).append("=").append(page).append("&").append(URL_P_PAGESIZE).append("=")
				.append(DEF_PAGESIZE);

		return sb.toString();
	}

}
