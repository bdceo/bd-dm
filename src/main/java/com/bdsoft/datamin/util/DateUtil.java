package com.bdsoft.datamin.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.commons.lang.StringUtils;

/**
 * 日期工具类
 *
 * @author   丁辰叶
 * @date	 2016-5-5
 * @version  1.0.0
 */
public class DateUtil {

	// 一秒，分钟，小时，天 对应的毫秒值
	public static final long SEC = 1000;
	public static final long SEC_ONE_MINUTE = 60 * SEC;
	public static final long SEC_ONE_HOUR = 60 * SEC_ONE_MINUTE;
	public static final long SEC_ONE_DAY = 24 * SEC_ONE_HOUR;

	/**
	 * 内部测试
	 */
	public static void main(String[] args) throws Exception {
		System.out.println(getWeek());
	}

	/**
	 * 解析字符串日期
	 * <p>
	 *
	 * @param strDate 字符串日期
	 * @param format 字符串日期格式
	 * @return
	 */
	public static Date parse(String strDate, String format) {
		if (StringUtils.isEmpty(strDate) || StringUtils.isEmpty(format)) {
			return null;
		}
		DateFormat df = new SimpleDateFormat(format);
		try {
			return df.parse(strDate);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 格式化日期
	 * <p>
	 *
	 * @param date 日期
	 * @param format 格式
	 * @return
	 */
	public static String format(Date date, String format) {
		if (date == null || StringUtils.isEmpty(format)) {
			return "";
		}
		DateFormat df = new SimpleDateFormat(format);
		try {
			return df.format(date);
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * 得到今天的星期
	 * 
	 * @return 今天的星期
	 */
	public static String getWeek() {
		return new SimpleDateFormat("E").format(new Date());
	}

	/**
	 * 根据日期获取周几
	 * <p>
	 *
	 * @param date 日期
	 * @return
	 */
	public static String getWeek(String strDate, String format) {
		Date d = parse(strDate, format);
		if (d == null) {
			return "未知";
		}
		String wk = "";
		Calendar c = Calendar.getInstance();
		c.setTime(d);
		int w = c.get(Calendar.DAY_OF_WEEK) - 1;
		switch (w) {
		case 0:
			wk = "日";
			break;
		case 1:
			wk = "一";
			break;
		case 2:
			wk = "二";
			break;
		case 3:
			wk = "三";
			break;
		case 4:
			wk = "四";
			break;
		case 5:
			wk = "五";
			break;
		case 6:
			wk = "六";
			break;
		}
		return wk;
	}

	/**
	 * 根据时间段，计算开始时间
	 * 
	 * @param period
	 *            时间段(1-今日，2-近三天，3-本周， 4-本月 ，5-最近6个月)
	 * @return 开始日期
	 */
	public static Calendar calcDate(int period) {
		Calendar calendar = Calendar.getInstance();
		if (period == 1) {// 当天
			calendar.add(Calendar.DATE, 0);
		}
		if (period == 2) {// 最近三天
			calendar.add(Calendar.DATE, -2);
		}
		if (period == 3) {// 本周
			int dday = calendar.get(Calendar.DAY_OF_WEEK) - 1;
			calendar.add(Calendar.DATE, (dday * -1));
		}
		if (period == 4) {// 本月
			int dday = calendar.get(Calendar.DAY_OF_MONTH);
			calendar.add(Calendar.DATE, (dday * -1));
		}
		if (period == 5) {// 最近六个月
			calendar.add(Calendar.MONTH, -5);
		}
		return calendar;
	}

	/**
	 * 本月最后一天
	 *
	 * @param date 当前时间
	 */
	public static Date getLastDayOfMonth(Date date) {
		Calendar lastDate = Calendar.getInstance();
		lastDate.setTime(date);
		lastDate.set(Calendar.DATE, 1);// 设为当前月的1号
		lastDate.add(Calendar.MONTH, 1);// 加一个月，变为下月的1号
		lastDate.add(Calendar.DATE, -1);// 减去一天，变为当月最后一天
		lastDate.set(Calendar.HOUR, 0);
		lastDate.set(Calendar.MINUTE, 0);
		lastDate.set(Calendar.SECOND, 0);
		return lastDate.getTime();
	}

	/**
	 * 本月第一天
	 *
	 * @param date 当前时间
	 */
	public static Date getFirstDayOfMonth(Date date) {
		Calendar firstDate = Calendar.getInstance();
		firstDate.setTime(date);
		firstDate.set(Calendar.DATE, 1);// 设为当前月的1号
		firstDate.set(Calendar.HOUR, 0);
		firstDate.set(Calendar.MINUTE, 0);
		firstDate.set(Calendar.SECOND, 0);
		return firstDate.getTime();
	}

	/**
	 * 得到一个日期是否是上午
	 * 
	 * @param date
	 *            日期
	 * @return 日期为上午时返回true
	 */
	public static boolean isAm(Date date) {
		boolean flag = false;
		SimpleDateFormat sdf = new SimpleDateFormat("H");
		if (sdf.format(date).compareTo("12") < 0) {
			flag = true;
		}
		return flag;
	}

	/**
	 * 计算短时间
	 *
	 * @param date 当前时间
	 * @return
	 */
	public static String shortDate(String date) {
		Date t1 = new Date();
		Date t2 = parse(date, "yy-MM-dd HH:mm:ss.SSS");
		if (t2 == null) {
			t2 = parse(date, "yyyy-MM-dd HH:mm:ss");
		}
		if (t2 == null) {
			return "--";
		}

		long ct = t1.getTime() - t2.getTime();

		if (ct < 60 * 1000) {
			return ct / 1000 + "秒前";
		}
		if (ct < 60 * 60 * 1000) {
			return ct / (60 * 1000) + "分钟前";
		}

		Calendar c1 = Calendar.getInstance();
		c1.setTime(t1);
		Calendar c2 = Calendar.getInstance();
		c2.setTime(t2);
		int cd = c1.get(Calendar.DATE) - c2.get(Calendar.DATE);
		if (cd == 0) {
			return "今天" + format(t2, "HH:mm");
		}
		if (cd == 1 || (ct < 24 * 60 * 60 * 1000)) {
			return "昨天" + format(t2, "HH:mm");
		}
		if (cd == 2 || (ct < 2 * 24 * 60 * 60 * 1000)) {
			return "前天" + format(t2, "HH:mm");
		}
		return format(t2, "HH:mm");
	}

	/**
	 * 根据出生日期获得年龄
	 * 
	 * @param birthday
	 * @return
	 */
	public static int getAge(Long birthday) {
		int age = 0;
		GregorianCalendar gc = new GregorianCalendar();
		GregorianCalendar now = (GregorianCalendar) gc.clone();
		gc.setTimeInMillis(birthday.longValue());
		age = now.get(Calendar.YEAR) - gc.get(Calendar.YEAR);

		if (age < 0) {
			age = 0;
		}
		return age;
	}

	/**
	* 得到昨天的日期yyyy-MM-dd
	* 
	* @return
	*/
	public static String yesterday() {
		Date date = new Date();
		date.setTime(date.getTime() - 86400000);
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		return simpleDateFormat.format(date);
	}

	/**
	 * 得到明天的日期yyyy-MM-dd
	 * 
	 * @return
	 */
	public static String tomorrow() {
		Date date = new Date();
		date.setTime(date.getTime() + 86400000);
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		return simpleDateFormat.format(date);
	}

	/**
	 * 计算耗时
	 * <p>
	 *
	 * @param msg 消息
	 * @param start 开始时间
	 */
	public static void cost(String msg, Date start) {
		long dif = System.currentTimeMillis() - start.getTime();
		System.out.println("\n>>" + msg + "\t耗时 " + dif + "毫秒. " + (dif / 1000) + "秒. " + (dif / 60000) + "分钟。");
	}
}
