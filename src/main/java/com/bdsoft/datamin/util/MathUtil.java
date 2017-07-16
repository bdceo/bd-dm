/**
 * MathUtil.java
 * com.bdsoft.datamin.util
 * Copyright (c) 2016, 北京微课创景教育科技有限公司版权所有.
*/

package com.bdsoft.datamin.util;

import java.math.BigDecimal;

/**
 * 数学加减乘除
 *
 * @author   丁辰叶
 * @date	 2016-5-5
 * @version  1.0.0
 */
public class MathUtil {
	
	/**
	 * 两个double数值相加
	 * 
	 * @param a
	 * @param b
	 * @return 精度由其一参数确定
	 */
	public static double sum(double a, double b) {
		BigDecimal bd1 = new BigDecimal(Double.toString(a));
		BigDecimal bd2 = new BigDecimal(Double.toString(b));
		return bd1.add(bd2).doubleValue();
	}

	/**
	 * 两个double数值相减
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	public static double sub(double a, double b) {
		BigDecimal bd1 = new BigDecimal(Double.toString(a));
		BigDecimal bd2 = new BigDecimal(Double.toString(b));
		return bd1.subtract(bd2).doubleValue();
	}

	/**
	 * 两个double数值相乘
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	public static double mul(double a, double b) {
		BigDecimal bd1 = new BigDecimal(Double.toString(a));
		BigDecimal bd2 = new BigDecimal(Double.toString(b));
		return bd1.multiply(bd2).doubleValue();
	}
}
