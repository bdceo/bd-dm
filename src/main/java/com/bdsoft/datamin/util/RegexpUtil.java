package com.bdsoft.datamin.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

/**
 * 正则工具
 *
 * @author   丁辰叶
 * @date	 2016-5-6
 * @version  1.0.0
 */
public class RegexpUtil {

	// 邮箱
	public static String emailPattern = "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*";
	// 手机号
	public static String telPattern = "^((13[0-9])|(14[0-9])|(15[^4,\\D])|(18[^4,\\D]))\\d{8}$";

	// 身份证号
	public static String idCardPattern1 = "^[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}$";
	public static String idCardPattern2 = "^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}(\\d|x|X){1}$";

	// 用户名
	public static String userNamePattern = "[ `~!#$%^&*()+=|{}':;',\\[\\]<>/?~！#￥%……&*（）——+|{}【】‘；：”“’。，、？]";

	public static void main(String[] ee) {
		boolean b = isIDcard("11010819771203422x");
		System.out.println(b);

		b = isTelNum("13910288559");
		System.out.println(b);

		b = safeUserName("小神~经");
		System.out.println(b);
	}

	/**
	 * username是否安全
	 *
	 * @param userName
	 * @return
	 */
	public static boolean safeUserName(String userName) {
		Pattern p = Pattern.compile(userNamePattern);
		Matcher m = p.matcher(userName);
		return !(m.find());
	}

	/**
	 * 验证身份证号
	 * 
	 * @param src
	 * @return
	 */
	public static boolean isIDcard(String src) {
		if (StringUtil.isEmpty(src)) {
			return false;
		}
		Pattern p = Pattern.compile(idCardPattern1);
		Matcher m = p.matcher(src);
		boolean b1 = m.find();
		p = Pattern.compile(idCardPattern2);
		m = p.matcher(src);
		boolean b2 = m.find();
		if (b1 || b2) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 验证email地址
	 * 
	 * @param src
	 * @return
	 */
	public static boolean isEmail(String src) {
		if (StringUtil.isEmpty(src)) {
			return false;
		}
		Pattern p = Pattern.compile(emailPattern);
		Matcher m = p.matcher(src);
		return m.find();
	}

	/**
	 * 验证手机号码
	 * 
	 * @param src
	 * @return
	 */
	public static boolean isTelNum(String src) {
		if (StringUtil.isEmpty(src)) {
			return false;
		}
		Pattern p = Pattern.compile(telPattern);
		Matcher m = p.matcher(src);
		return m.find();
	}

	/**
	 * 判断是否为整数
	 * 
	 * @param str 传入的字符串
	 * @return 是整数返回true, 否则返回false
	 */
	public static boolean isInt(String str) {
		if (null == str)
			return false;
		str = str.trim();
		Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
		return pattern.matcher(str).matches();
	}

	/**
	 * 判断是否为浮点数，包括double和float
	 * 
	 * @param str 传入的字符串
	 * @return 是浮点数返回true, 否则返回false
	 */
	public static boolean isDouble(String str) {
		if (null == str)
			return false;
		Pattern pattern = Pattern.compile("^[-\\+]?[.\\d]*$");
		return pattern.matcher(str).matches();
	}

	/**
	 * 判断输入的字符串是否为纯汉字
	 * 
	 * @param str 传入的字符串
	 * @return 如果是纯汉字返回true, 否则返回false
	 */
	public static boolean isChinese(String str) {
		if (null == str)
			return false;
		Pattern pattern = Pattern.compile("[\u0391-\uFFE5]+$");
		return pattern.matcher(str).matches();
	}

	/**
	 * 是否包含中文
	 *
	 * @param str 待检测字符串
	 * @return
	 */
	public static boolean isIncludeChinese(String str) {
		if (StringUtils.isEmpty(str)) {
			return false;
		}
		Pattern pattern = Pattern.compile("^\\s*\\S*[\\u0391-\\uFFE5]+\\s*\\S*$");
		Matcher matcher = pattern.matcher(str);
		return matcher.find();
	}

	/**
	 * 正则验证
	 * 
	 * @param Content 需要验证的内容
	 * @param regex 验证正则表达式
	 * @return
	 */
	public static boolean regValidate(String Content, String regex) {
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(Content);
		return matcher.matches();
	}

	/**
	 * 正则查找
	 * 
	 * @param Content 查找内容
	 * @param regex 匹配正则表达式
	 * @return
	 */
	public static String regFind(String Content, String regex) {
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(Content);
		if (matcher.find()) {
			return matcher.group();
		} else {
			return "";
		}
	}

	/**
	 * 正则查找
	 * 
	 * @param Content 查找内容
	 * @param regex 匹配正则表达式
	 * @param index 分组
	 * @return
	 */
	public static String regFind(String Content, String regex, int index) {
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(Content);
		if (matcher.find()) {
			return matcher.group(index);
		} else {
			return "";
		}
	}

	/**
	 * 正则查找
	 * 
	 * @param Content 内容
	 * @param regex 正则
	 * @return
	 */
	public static List<String> regFinds(String Content, String regex) {
		List<String> list = new ArrayList<String>();
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(Content);
		while (matcher.find()) {
			list.add(matcher.group());
		}
		return list;
	}

	/**
	 * 正则替换
	 * 
	 * @param Content  查找内容
	 * @param regex  匹配正则表达式
	 * @return
	 */
	public static String regReplace(String Content, String regex, String regStr) {
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(Content);
		return matcher.replaceAll(regStr);
	}

}
