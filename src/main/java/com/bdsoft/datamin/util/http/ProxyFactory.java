package com.bdsoft.datamin.util.http;

import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import com.bdsoft.datamin.util.cmd.Ping4java;

public class ProxyFactory {

	// TODO DB维护代理配置，状态失效后，DB状态更新

	// 代理数据库及缓存
	private static Map<String, ProxyEnt> PROXY_DB = new HashMap<String, ProxyEnt>();
	private static List<ProxyEnt> PROXY_CACHE = new ArrayList<ProxyEnt>();

	// 哪些网站需要使用代理抓取
	private static Map<String, Boolean> PROXY_HOST = new HashMap<String, Boolean>();
	private static boolean INIT_OK = false; // 初始化标示

	private static ProxyFactory CACHE = new ProxyFactory();

	private ProxyFactory() {
		init();
	}

	public static synchronized ProxyFactory getInstance() {
		if (CACHE == null) {
			CACHE = new ProxyFactory();
		}
		return CACHE;
	}

	public static void main(String[] args) {
		getInstance().setProxy("");
	}

	// 设置代理
	public void setProxy(String url) {
		// 随机从缓存中获取代理服务配置
		int size = PROXY_CACHE.size();
		int index = new Random().nextInt(size);
		ProxyEnt pe = PROXY_CACHE.get(index);
		System.out.println("使用代理：" + pe);
		// 设置代理
		System.setProperty("http.proxySet", "true");
		System.setProperty("http.proxyHost", pe.getHost());

		System.setProperty("http.proxyPort", pe.getPort() + "");
		// 设置代理验证
		if (pe.isAuth()) {
			Authenticator.setDefault(new BDProxyAuth(pe));
		}
	}

	// 设置不需要代理的主机或网站
	private static void setNonProxyHosts() {
		StringBuffer hosts = new StringBuffer();
		hosts.append("localhost");
		// hosts.append("|");
		System.setProperty("http.nonProxyHosts", hosts.toString());
	}

	// 内部类，实现Http代理验证
	private static class BDProxyAuth extends Authenticator {

		private String auNname;
		private String auPwd;

		public BDProxyAuth(ProxyEnt pe) {
			super();
			this.auNname = pe.getAuthUser();
			this.auPwd = pe.getAuthPwd();
		}

		protected PasswordAuthentication getPasswordAuthentication() {
			return new PasswordAuthentication(this.auNname,
					this.auPwd.toCharArray());
		}

	}

	// 检查代理状态
	public static void check() {
		new CheckProxy().start();
	}

	// 独立线程检测
	public static class CheckProxy extends Thread {
		public void run() {
			if (INIT_OK) {
				Set<String> tmp = new HashSet<String>();
				tmp.addAll(PROXY_DB.keySet());
				for (String host : tmp) {
					boolean ok = Ping4java.ping(host);
					if (!ok) {
						System.out.println("代理失效：" + host + "："
								+ PROXY_DB.get(host).getPort());
						PROXY_DB.remove(host);
					}
				}
				PROXY_CACHE.clear();
				PROXY_CACHE.addAll(PROXY_DB.values());
				System.out.println("DB有效代理：" + PROXY_DB.size());
				System.out.println("缓存有效代理：" + PROXY_CACHE.size());
				System.out.println("Http代理检测完毕");
			}
		}
	}

	public static void init() {
		INIT_OK = false;
		// TODO 读取DB或文件，初始化可用代理缓存

		// 初始化 - 代理数据库
		// ProxyEnt p1 = new ProxyEnt(
		// "ec2-54-250-156-36.ap-northeast-1.compute.amazonaws.com", 3128);
		// PROXY_DB.put(p1.getHost(), p1);
		ProxyEnt p2 = new ProxyEnt("69.197.132.80", 3128);
		PROXY_DB.put(p2.getHost(), p2);
		ProxyEnt p3 = new ProxyEnt("199.15.254.103", 8089);
		PROXY_DB.put(p3.getHost(), p3);
		// ProxyEnt p4 = new ProxyEnt("192.3.156.131", 3128);
		// PROXY_DB.put(p4.getHost(), p4);
		// ProxyEnt p5 = new ProxyEnt("204.12.223.173", 7808);
		// PROXY_DB.put(p5.getHost(), p5);
		// ProxyEnt p6 = new ProxyEnt("168.63.172.74", 3128);
		// PROXY_DB.put(p6.getHost(), p6);
		// ProxyEnt p7 = new ProxyEnt("110.77.202.187", 3128);
		// PROXY_DB.put(p7.getHost(), p7);
		ProxyEnt p8 = new ProxyEnt("116.228.55.217");
		PROXY_DB.put(p8.getHost(), p8);
		ProxyEnt p9 = new ProxyEnt("115.25.216.6");
		PROXY_DB.put(p9.getHost(), p9);
		ProxyEnt p10 = new ProxyEnt("221.176.14.72");
		PROXY_DB.put(p10.getHost(), p10);
		ProxyEnt p11 = new ProxyEnt("180.250.87.132", 8080);
		PROXY_DB.put(p11.getHost(), p11);
		// ProxyEnt p12 = new
		// ProxyEnt("ec2-54-208-75-204.compute-1.amazonaws.com");
		// PROXY_DB.put(p12.getHost(), p12);
		ProxyEnt p13 = new ProxyEnt("im.lawson.com");
		PROXY_DB.put(p13.getHost(), p13);
		ProxyEnt p14 = new ProxyEnt("ip249.50-31-113.static.steadfastdns.net",
				7808);
		PROXY_DB.put(p14.getHost(), p14);
		// ProxyEnt p15 = new ProxyEnt("9005.hostingsharedbox.com", 8089);
		// PROXY_DB.put(p15.getHost(), p15);
		// ProxyEnt p16 = new ProxyEnt("9005.hostingsharedbox.com", 7808);
		// PROXY_DB.put(p16.getHost(), p16);
		// ProxyEnt p17 = new ProxyEnt("9005.hostingsharedbox.com", 3128);
		// PROXY_DB.put(p17.getHost(), p17);
		ProxyEnt p18 = new ProxyEnt("74.221.210.119", 3128);
		PROXY_DB.put(p18.getHost(), p18);
		ProxyEnt p19 = new ProxyEnt("74.221.210.119", 8089);
		PROXY_DB.put(p19.getHost(), p19);
		// ProxyEnt p20 = new ProxyEnt("112.25.137.201", 8000);
		// PROXY_DB.put(p20.getHost(), p20);
		ProxyEnt p21 = new ProxyEnt("205.164.41.101", 3128);
		PROXY_DB.put(p21.getHost(), p21);
		ProxyEnt p22 = new ProxyEnt("199.15.254.103", 3128);
		PROXY_DB.put(p22.getHost(), p22);
		// ProxyEnt p23 = new ProxyEnt("198.50.241.160", 3128);
		// PROXY_DB.put(p23.getHost(), p23);

		// 初始化 - 缓存，对外调用使用
		PROXY_CACHE.addAll(PROXY_DB.values());

		// 初始化 - 不要使用代理的主机或网站
		setNonProxyHosts();

		// Ping校验 - 测试代理稳定性
		check();

		INIT_OK = true;
	}
}
