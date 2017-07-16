package com.bdsoft.datamin.util;

/**
 * 数据库主键生成器
 * <p>
 *
 * @author   丁辰叶
 * @date	 2016-5-6
 * @version  1.0.0
 */
public class IdGenerator {

	/**
	 * 生成用户Code
	 * 
	 * @return
	 */
	public static String userCode() {
		return new StringBuffer().append(System.currentTimeMillis()).append(Utils.random(4)).toString();
	}

	/**
	 * 生成各个充值渠道订单号
	 * 
	 * @param agentname
	 *            充值渠道英文缩写
	 * @return
	 */
	public static String genOid(String agentname) {
		String oid = "";

		oid = new StringBuffer(agentname).append(System.currentTimeMillis()).append(Utils.random(3)).toString();

		return oid;
	}

	/**
	 * 生成的提款订单号
	 * 
	 * @return
	 */
	public static String drawOrderId() {
		return new StringBuffer("draw").append(System.currentTimeMillis()).append(Utils.random(3)).toString();
	}

	/**
	 * 生成纯数字随机密码
	 * 
	 * @param i
	 *            位数
	 * @return
	 */
	public static String genRandomPwd(int i) {
		if (i < 6) {
			i = 6;
		}
		return String.valueOf(Math.random()).substring(2).substring(0, i);
	}

	public static void main(String args[]) {
		System.out.println(IdGenerator.userCode());
		System.out.println(IdGenerator.genRandomPwd(8));
	}
}
