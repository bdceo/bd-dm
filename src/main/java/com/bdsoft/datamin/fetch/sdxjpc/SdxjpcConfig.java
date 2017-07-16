package com.bdsoft.datamin.fetch.sdxjpc;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author 丁辰叶
 * @date 2014-7-18
 */
public class SdxjpcConfig {

	public static String DEV_LANG = "PHP";

	public static String WEB_DOMAIN = "sdxjpc.com";
	public static String WEB_CHARSET = "GBK";

	// URL 参数
	public static String URL_P_PAGER = "iPage";
	public static String URL_P_PAGESIZE = "";

	public static int DEF_PAGESIZE = 0;// 每页数量
	public static int DEF_PAGETOTAL = 3;// 总页数

	public static Map<String, String> HTTP_HEADER = new HashMap<String, String>();
	public static Map<String, String> HTTP_PARAM = new HashMap<String, String>();

	static {
		// 请求头参数
		HTTP_HEADER.put("Origin", "http://www.sdxjpc.com");
		HTTP_HEADER.put("Referer", "http://www.sdxjpc.com/scrp/bookcustomore.cfm");

		// URL请求参数
		HTTP_PARAM.put("sBrowType", "c");
		HTTP_PARAM.put("iSortField", "7");
		HTTP_PARAM.put("sSortOrder", "desc");
		HTTP_PARAM.put("iSno", "0");
	}

	public static void main(String[] args) throws Exception {

	}

	// 拼接分页地址
	public static String getPagerUrl(String url, int page) {
		StringBuilder sb = new StringBuilder(url);

		sb.append("?sBrowType=c&iSortField=7&sSortOrder=desc&iSno=0&iPage=").append(page);

		return sb.toString();
	}

}
