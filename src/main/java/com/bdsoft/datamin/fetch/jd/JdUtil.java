package com.bdsoft.datamin.fetch.jd;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.bdsoft.datamin.fetch.jd.feed.JDJson;
import com.bdsoft.datamin.util.StringUtil;
import com.bdsoft.datamin.util.http.BDHttpParam;
import com.bdsoft.datamin.util.http.BDHttpUtil;
import com.bdsoft.datamin.util.http.NetUtil;
import com.google.gson.Gson;

/**
 * 京东抓取工具类
 * 
 * @author 丁辰叶
 * @date 2014-9-19
 */
public class JdUtil {

	public static final Random RND = new Random(System.currentTimeMillis());

	// 商品详情
	public static final String URL_4_ITEM = "http://item.jd.com/#ID#.html";
	// 商品价格
	public static final String URL_4_PRICE = "http://p.3.cn/prices/mgets?skuIds=J_#ID#&callback=json";
	public static final String URL_4_PRICE2 = "http://p.3.cn/prices/get?pduid=#RND#&skuid=J_#ID#";

	// 第三方卖家详情
	public static final String URL_4_VENDER = "http://st.3.cn/gvi.html?callback=json&type=popdeliver&skuid=#ID#";
	// 第三方卖家公司信息
	public static final String URL_4_VENDER_CMPINFO = "http://rms.shop.jd.com/json/pop/popcompany.action?callback=json&venderID=#VID#";

	// 转换URL时使用
	public static final String REPLACE_ID = "#ID#";
	public static final String REPLACE_VID = "#VID#";
	public static final String REPLACE_RND = "#RND#";

	// 商品唯一码正则
	public static final String SKUID_REG = "^http://item.jd.com/([\\d]+).html.*";
	public static final Pattern SKUID_PATTERN = Pattern.compile(SKUID_REG);

	// 商品分类正则
	public static final String CATG_REG = ".*list.jd.com/.*cat=([\\d]+,[\\d]+,[\\d]+).*";
	public static final Pattern CATG_PATTERN = Pattern.compile(CATG_REG);
	public static final String CATG_REG_2 = ".*list.jd.com/([\\d]+)-([\\d]+)-([\\d]+).html$";
	public static final Pattern CATG_PATTERN_2 = Pattern.compile(CATG_REG_2);

	/*------------------------------从指定url提取商品部分属性信息--------------------------------------*/
	/**
	 * 提取商品唯一编号
	 * 
	 * @param url
	 *            商品详情URL
	 * @return skuid
	 */
	public static String takeSkuid(String url) {
		String skuid = "";
		if (StringUtil.isEmpty(url)) {
			return skuid;
		}
		Matcher mat = SKUID_PATTERN.matcher(url);
		if (mat.find()) {
			skuid = mat.group(1);
		}
		return skuid;
	}

	/**
	 * 通过SKUID获取商品价格信息
	 * 
	 * @param skuid
	 * @return
	 */
	public static String takePrice(String skuid) {
		String price = "-1";
		if (StringUtil.isEmpty(skuid)) {
			return price;
		}
		// 两次尝试获取价格
		price = takePrice1(skuid);
		if ("-1".equals(price)) {
			price = takePrice2(skuid);
		}
		return price;
	}

	private static String takePrice1(String skuid) {
		String price = "-1";
		String purl = formPriceUrlV1(skuid);
		try {
			String src = BDHttpUtil.sendGet(purl, BDHttpParam.init().setCharset(NetUtil.CHARSET_GB2312));
			src = JDJson.subSafeJson(src, JDJson.BRACKET_2L);
			price = new Gson().fromJson(src, JDJson.class).getP();
		} catch (Exception e) {
			System.err.println(String.format("获取商品#%s#价格数据异常：%s", skuid, e.getMessage()));
		}
		return price;
	}

	private static String takePrice2(String skuid) {
		String price = "-1";
		String purl = formPriceUrlV2(skuid);
		try {
			String src = BDHttpUtil.sendGet(purl, BDHttpParam.init().setCharset(NetUtil.CHARSET_GB2312));
			src = JDJson.subSafeJson(src, JDJson.BRACKET_2L);
			price = new Gson().fromJson(src, JDJson.class).getP();
		} catch (Exception e) {
			System.err.println(String.format("获取商品#%s#价格数据异常：%s", skuid, e.getMessage()));
		}
		return price;
	}

	/*-----------------------------END-从指定url提取商品部分属性信息-END--------------------------------------*/

	/*------------------------------根据商品唯一码组拼成获取商品信息的URL-------------------------------------*/
	/**
	 * 通过商品唯一码组拼成 商品详情URL
	 * 
	 * @param skuid
	 *            商品唯一码
	 * @return
	 */
	public static String formItemUrl(String skuid) {
		if (StringUtil.isEmpty(skuid)) {
			return "";
		}
		return URL_4_ITEM.replaceAll(REPLACE_ID, skuid);
	}

	/**
	 * 通过商品唯一码组拼成 商品价格URL
	 * 
	 * @param skuid
	 *            商品唯一码
	 * @return
	 */
	private static String formPriceUrlV1(String skuid) {
		if (StringUtil.isEmpty(skuid)) {
			return "";
		}
		return URL_4_PRICE.replaceAll(REPLACE_ID, skuid);
	}

	private static String formPriceUrlV2(String skuid) {
		if (StringUtil.isEmpty(skuid)) {
			return "";
		}
		return URL_4_PRICE2.replaceAll(REPLACE_ID, skuid).replaceAll(REPLACE_RND, String.valueOf(RND.nextInt()));
	}

	/**
	 * 通过商品唯一码组拼成 第三方卖家详情URL
	 * 
	 * @param skuid
	 *            商品唯一码
	 * @return
	 */
	public static String formVenderUrl(String skuid) {
		if (StringUtil.isEmpty(skuid)) {
			return "";
		}
		return URL_4_VENDER.replaceAll(REPLACE_ID, skuid);
	}

	/**
	 * 通过商品唯一码组拼成 第三方卖家公司及所在地信息URL
	 * 
	 * @param vid
	 *            第三方卖家编号
	 * @return
	 */
	public static String formVenderCmpUrl(String vid) {
		if (StringUtil.isEmpty(vid)) {
			return "";
		}
		return URL_4_VENDER_CMPINFO.replaceAll(REPLACE_VID, vid);
	}

	/*-----------------------------END-根据商品唯一码组拼成获取商品信息的URL-END------------------------------------*/

	/**
	 * 修复url：少http协议标示
	 * 
	 * @param url
	 *            待修复url
	 * @return
	 */
	public static String repairUrl(String url) {
		if (url.startsWith("//")) {
			return url = "http:" + url;
		}
		return url;
	}

	/**
	 * 标准三级分类地址
	 * 
	 * @param url
	 *            待校验地址
	 * @return
	 */
	public static String formStdCatUrl(String url) {
		Matcher mat = CATG_PATTERN.matcher(url);
		if (mat.find()) {
			return url;
		} else {
			mat = CATG_PATTERN_2.matcher(url);
			if (mat.find()) {
				StringBuilder sb = new StringBuilder();
				sb.append("http://list.jd.com/list.html?cat=");
				sb.append(mat.group(1)).append(",").append(mat.group(2)).append(",").append(mat.group(3));
				return sb.toString();
			}
		}
		System.err.println("分类url未处理可能不规范");
		return url;
	}
}
