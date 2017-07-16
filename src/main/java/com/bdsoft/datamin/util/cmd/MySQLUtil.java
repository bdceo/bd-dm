package com.bdsoft.datamin.util.cmd;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class MySQLUtil {

	private static Runtime rt = Runtime.getRuntime();

	private static String MYSQL_START = "net start mysql";
	// private static String MYSQL_STOP = "net stop mysql";
	private static String MYSQL_TEST = "select now()";

	private static String propFile = "prop/jdbc.properties";
	private static Properties config = new Properties();
	private static DBCon dbCon = null;

	static {
		try {
			// 加载属性文件
			InputStream in = MySQLUtil.class.getClassLoader().getResourceAsStream(propFile);
			config.load(in);
			// 初始化连接配置
			dbCon = new DBCon(config);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws Exception {
		testMysqlCon();
		// startMysql();
	}

	// 启动mysql服务，需要保持同步
	public static synchronized boolean start() {
		if (testMysqlCon()) {
			return true;
		}
		boolean op = true;
		System.out.println("准备启动MySQL服务：" + MYSQL_START);
		try {
			Process pro = rt.exec(MYSQL_START);
			if (pro != null) {
				BufferedReader in = new BufferedReader(new InputStreamReader(pro.getInputStream(), "GBK"));
				BufferedReader errIn = new BufferedReader(new InputStreamReader(pro.getErrorStream(), "GBK"));

				String line = null;
				while ((line = errIn.readLine()) != null) {
					System.out.println(line);
					op = false;
					break;
				}

				if (!op) {
					System.out.println("启动失败");
					return op;
				}

				while ((line = in.readLine()) != null) {
					System.out.println(line);
				}
				System.out.println("启动成功");
			}
		} catch (Exception e) {
			System.err.println("重启mysql失败:" + e.getMessage());
		}
		return op;
	}

	// 测试mysql服务连通性
	public static boolean testMysqlCon() {
		if (dbCon == null) {
			dbCon = new DBCon(config);
			if (dbCon == null) {
				System.out.println("连接配置丢失，严重错误！！！");
				return false;
			}
		}
		System.out.println("测试MySQL服务：" + MYSQL_TEST);
		boolean test = true;

		try {
			Class.forName("com.mysql.jdbc.Driver");
			DriverManager.getConnection(dbCon.getUrl(), dbCon.getUser(), dbCon.getPwd());
		} catch (Exception e) {
			test = false;
			System.out.println("获取连接失败:" + e.getMessage());
		}
		System.out.println("测试MySQL服务 - " + (test ? "正常" : "失败"));
		return test;
	}

	public static Connection getCon() {
		Connection con = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection(dbCon.getUrl(), dbCon.getUser(), dbCon.getPwd());
		} catch (Exception e) {
			System.out.println("获取连接失败:" + e.getMessage());
		}
		return con;
	}

	// 连接配置
	static class DBCon {
		String driver;
		String url;
		String user;
		String pwd;

		public DBCon(Properties config) {
			this.driver = config.getProperty("jdbc.driverClassName");
			this.url = config.getProperty("jdbc.url");
			this.user = config.getProperty("jdbc.username");
			this.pwd = config.getProperty("jdbc.password");
		}

		public String getDriver() {
			return driver;
		}

		public String getUrl() {
			return url;
		}

		public String getUser() {
			return user;
		}

		public String getPwd() {
			return pwd;
		}

	}
}
