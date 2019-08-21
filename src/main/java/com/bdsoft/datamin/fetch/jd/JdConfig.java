package com.bdsoft.datamin.fetch.jd;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 京东抓取配置
 */
public class JdConfig {

    public static final int JD_PRO_FETCHER = 10; // 抓取商品-线程

    public static final int JD_REV_FETCHER = 9; // 抓取评论-线程

    public static final int JD_REV_SUB_FETCHER = 14;// 抓取评论-最大子线程数

    // 每次读取多少个URL进入商品队列
    public static int MAX_LOAD = 2000;

    public static final String LOAD_ORDER = "asc";

    // 待抓评论队列超过该阀值，商品抓取线程挂起等待
    public static final int maxREVqsize = JD_REV_FETCHER * 2;

    // 商品队列小于该阀值，触发加载新的URL入队列{TODO 可以指定公式，动态定义}
    public static final int minItem = JD_PRO_FETCHER * 2;

    // mysql服务检测相关
    public static AtomicBoolean MYSQL_OK = new AtomicBoolean(true);

    public static AtomicBoolean MYSQL_HANDING = new AtomicBoolean(false);

    public static final long MYSQL_START_WAIT = 1000L * 5;// 重启耗时，线程挂起时间
}
