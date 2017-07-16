package com.bdsoft.datamin.util.cmd;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Ping4java {

	private static final Runtime rt = Runtime.getRuntime();

	public static void main(String[] args) throws Exception {
		String param = null;

		param = "localhost";
		// param = "jd.com";
		param = "168.63.172.74";

		boolean ok = ping(param);
		System.out.println(ok);
		
		
	}

	/**
	 * 模拟控制台Ping命令
	 * 
	 * @param param
	 *            参数[域名|ip]
	 * @return
	 */
	public static boolean ping(String host) {
		// String defCharSet = Charset.defaultCharset().displayName();
		// System.out.println("defaultCharsetName:" + defCharSet);
		BufferedReader in = null;
		String cmd = formPing(host);
		try {
			Process p = rt.exec(cmd);
			if (p != null) {
				System.out.println("\n\n"+cmd);
				// 从控制台读数据，默认是GBK，可以看windows控制台的编码设置
				in = new BufferedReader(new InputStreamReader(
						p.getInputStream(), "GBK"));
				String line = null;
				while ((line = in.readLine()) != null) {
					System.out.println(line);
					if (line.startsWith("来自")) {
						return true;
					}
				}
			}
			return false;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
		}
		return false;
	}

	private static String formPing(String host) {
		// 请求3次，每次5秒超时
		return new StringBuffer("ping ").append(host).append(" -n 3 -w 5000")
				.toString();
	}

}
