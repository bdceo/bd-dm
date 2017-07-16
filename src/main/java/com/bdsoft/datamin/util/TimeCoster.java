package com.bdsoft.datamin.util;

import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Hours;
import org.joda.time.Minutes;
import org.joda.time.Seconds;

/**
 * 耗时统计
 * 
 * @author	丁辰叶
 * @date	2014-9-19
 */
public class TimeCoster {

	private ConcurrentHashMap<String, TimeCoster.Tcer> tcMap = new ConcurrentHashMap<String, TimeCoster.Tcer>();

	private static TimeCoster instance = new TimeCoster();

	private TimeCoster() {
	}

	public static TimeCoster getInstance() {
		return instance;
	}

	/**
	 * 开始计时
	 * 
	 * @param key 唯一键
	 * @param msg 统计结束时的消息头
	 */
	public void start(String key, String msg) {
		if (key == null || key.trim().isEmpty()) {
			throw new RuntimeException("耗时统计-键(key)非空");
		}
		if (tcMap.containsKey(key)) {
			System.out.println(String.format("耗时统计-%s-正在计时中...", key));
			return;
		}
		tcMap.put(key, new Tcer(msg));
	}

	/**
	 * 统计耗时
	 * 
	 * @param key 唯一键
	 */
	public void end(String key) {
		Tcer tcer = tcMap.remove(key);
		synchronized (tcer) {
			System.out.println(tcer.over());
		}
	}

	/**
	 * 计时器
	*/
	class Tcer {

		private String msg;
		private DateTime start;

		public Tcer() {
			this.msg = String.format("TC#%d", new Random().nextInt(10000));
			this.start = new DateTime();
		}

		public Tcer(String msg) {
			this();
			if (msg.isEmpty()) {
				return;
			}
			this.msg = msg;
		}

		public String over() {
			DateTime end = new DateTime();
			int d = Days.daysBetween(start, end).getDays();
			int h = Hours.hoursBetween(start, end).getHours();
			int m = Minutes.minutesBetween(start, end).getMinutes() % 60;
			int s = Seconds.secondsBetween(start, end).getSeconds() % 60;
			return String.format(msg + " 总耗时：%d天 %d时 %d分 %d秒", d, h, m, s);
		}
	}

	public static void main(String[] args) {
		int r = new Random().nextInt(1000);
		System.out.println(r);
	}

}
