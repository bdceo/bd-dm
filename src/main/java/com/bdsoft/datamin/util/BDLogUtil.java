/**
 * BDLogUtil.java
 * com.bdsoft.datamin.util
 * Copyright (c) 2016, 北京微课创景教育科技有限公司版权所有.
*/

package com.bdsoft.datamin.util;

import ch.qos.logback.ext.spring.LogbackConfigurer;

/**
 * 日志初始化工具
 *
 * @author   丁辰叶
 * @date	 2016-7-8
 * @version  1.0.0
 */
public class BDLogUtil {

	private static String LOG_XML = "classpath:xml/logback.xml";

	public static void init() {
		try {
			LogbackConfigurer.initLogging(LOG_XML);
		} catch (Exception e) {
			throw new RuntimeException("日志初始化失败...");
		}
	}
}
