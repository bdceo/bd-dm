package com.bdsoft.datamin.util;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.quartz.Scheduler;

import com.bdsoft.datamin.fetch.douban.DoubanController;

/**
 * 升级Spring版本后，CronTriggerBean实现需要重新调试
 *
 * @author   丁辰叶
 * @date	 2016-1-27
 * @version  1.0.0
 */
public class JobUtil {

	// Job容器名
	private static final String QUARTZ_FACTORY = "quartzFactory";
	// Job默认组名
	public static final String QUARTZ_GROUP = Scheduler.DEFAULT_GROUP;
	// Job调度器
	private static Scheduler scheduler = null;

	// 所有Job触发器引用
	private static Map<Integer, String> trigMap = new ConcurrentHashMap<Integer, String>();

	// 所有job-id定义
	public static Integer JOB_BOOK_ID = 1;
	public static Integer JOB_BUYLINK_ID = 2;
	public static Integer JOB_REVIEWS_ID = 3;
	public static Integer JOB_REVIEWD_ID = 4;
	public static Integer JOB_LATESTBOOK_ID = 5;
	public static Integer JOB_CHARTBOOK_ID = 6;
	public static Integer JOB_TAGBOOK_ID = 7;

	// 初始化job容器内触发器的定义
	static {
		trigMap.put(JOB_BOOK_ID, "douBookTrigger");
		trigMap.put(JOB_BUYLINK_ID, "douBuyLinkTrigger");
		trigMap.put(JOB_REVIEWS_ID, "douReviewsTrigger");
		trigMap.put(JOB_REVIEWD_ID, "douReviewdTrigger");
		trigMap.put(JOB_LATESTBOOK_ID, "douLatestBookTrigger");
		trigMap.put(JOB_CHARTBOOK_ID, "douChartBookTrigger");
		trigMap.put(JOB_TAGBOOK_ID, "douTagBookTrigger");
	}

	public static String getTriggerName(int tg) {
		return trigMap.get(new Integer(tg));
	}

	// 获取Job调度器
	public synchronized static Scheduler getScheduler() {
		if (scheduler == null) {
			scheduler = (Scheduler) BDSpringUtil.getBean(
					QUARTZ_FACTORY);
		}
		return scheduler;
	}

	// 获取Job触发器
//	public static CronTriggerBean getCronTrigger(int tg) {
//		return (CronTriggerBean) LogicFactory.getContext().getBean(
//				getTriggerName(tg));
//	}

	/**
	 * 暂停触发器指定时间，自动恢复
	 * 
	 * @param delay
	 *            暂停时间
	 * @param jodId
	 *            触发器编号
	 */
	public static void pauseTrigger(long delay, int jodId) {
		String trgName = getTriggerName(jodId);
		try {

//			// 暂停触发器
//			getScheduler().pauseTrigger(trgName, QUARTZ_GROUP);
//			// 设置触发器延时
//			CronTriggerBean ctb = getCronTrigger(jodId);
//			ctb.setStartTime(new Date(delay));
//			// 重新调度触发器
//			getScheduler().rescheduleJob(trgName, QUARTZ_GROUP, ctb);

			System.out.println("\tjob-->" + trgName + "推迟执行。");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 默认延迟30分钟
	 * 
	 * @return
	 */
	public static Long getDelay() {
		return new Date().getTime() + DoubanController.JOB_PAUSE;
	}
}
