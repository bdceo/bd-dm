package com.bdsoft.fetch;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JdUrlReg {

	public static void main(String[] args) {
		// String reg = "([\\d]+,[\\d]+,[\\d]+)";
		String reg = "(.*/[\\d]+-[\\d]+-[\\d]+.html)";
		String url = "http://list.jd.com/6994-6999-7027.html";
		System.out.println(url.matches(reg));

		Pattern pat = Pattern.compile(".*list.jd.com/([\\d]+)-([\\d]+)-([\\d]+).html$");
		Matcher mat = pat.matcher(url);
		if (mat.find()) {
			System.out.println(mat.group(0));
			System.out.println(mat.group(1));
			System.out.println(mat.group(2));
			System.out.println(mat.group(3));
		}
	}

}
