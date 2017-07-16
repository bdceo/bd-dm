package com.bdsoft.datamin.fetch.www;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.bdsoft.datamin.util.StringUtil;

public class FetchFilter {

	public static int USER_DEF = 0;
	public static int USER_HUNTER = 1;
	public static int USER_HR = 2;
	public static int USER_CMP = 3;
	public static int USER_HRD = 4;

	private static String[] HUNTER_KEYS = new String[] { "猎头", "hunter", "举贤" };
	private static String[] HR_KEYS = new String[] { "hr" };
	private static String[] CMP_KEYS = new String[] { "官网", "官方" };

	public int parseUserType(String content) {
		if (StringUtil.isEmpty(content)) {
			return USER_DEF;
		} else if (isHunter(content)) {
			return USER_HUNTER;
		} else if (isHr(content)) {
			return USER_HR;
		} else if (isCmp(content)) {
			return USER_CMP;
		} else {
			return USER_DEF;
		}
	}

	public boolean isHunter(String con) {
		con = con.toLowerCase();
		boolean flag = false;
		for (String key : HUNTER_KEYS) {
			if (con.contains(key)) {
				flag = true;
				break;
			}
		}
		return flag;
	}

	public boolean isHr(String con) {
		con = con.toLowerCase();
		boolean flag = false;
		for (String key : HR_KEYS) {
			if (con.contains(key)) {
				flag = true;
				break;
			}
		}
		return flag;
	}

	public boolean isCmp(String con) {
		con = con.toLowerCase();
		boolean flag = false;
		for (String key : CMP_KEYS) {
			if (con.contains(key)) {
				flag = true;
				break;
			}
		}
		return flag;
	}

	private static FetchFilter spf = new FetchFilter();

	public static FetchFilter getInst() {
		if (spf == null) {
			spf = new FetchFilter();
		}
		return spf;
	}

	private FetchFilter() {
	}

	public static void main(String[] args) {
		String c = "dd[at]qq.com的发生sdf#ss.cc硕大的房顶上ray[at]papa.me额为"
				+ "丰富zhuqian at sina.cn空间来看bjzhaojing35@ sina.com水电费的萨菲bdceo{@}ss.oo";
		String m = pickEmail(c);
		System.out.println(m);
	}

	/**
	 * 提取email，分号隔开
	 * 
	 * @param content
	 * @return
	 */
	public static String pickEmail(String content) {
		content = content.replaceAll("\\[at\\]", "@");
		content = content.replaceAll("\\[@\\]", "@");
		content = content.replaceAll("\\{at\\}", "@");
		content = content.replaceAll("\\{@\\}", "@");
		content = content.replaceAll("#", "@");
		content = content.replaceAll(" at ", "@");
		content = content.replaceAll("@ ", "@");
		Pattern p = Pattern.compile("[\\w[.-]]+@[\\w[.-]]+\\.[\\w]+");
		Matcher m = p.matcher(content);
		StringBuffer sb = new StringBuffer("");
		while (m.find()) {
			String ml = m.group();
			if (sb.toString().contains(ml)) {
				continue;
			}
			sb.append(m.group());
			sb.append(";");
		}
		String mail = sb.toString();
		if (mail.length() > 0) {
			mail = mail.substring(0, mail.length() - 1);
		}
		return mail;
	}
}