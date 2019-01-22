package com.bdsoft.datamin.fetch.hshc;

import java.time.format.DateTimeFormatter;


public class HshcConfig {

    // 抓取小时间隔
    public static final Integer HOUR_SEP = 5;

    // 分页条数
    public static final Integer PAGE_SIZE = 50;

    // 时间格式化
    public static final DateTimeFormatter DT_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // 日志
    public static final String LOG_API = "http://log.huashenghaoche.com/hshclog/historyLog/searchHistoryLog.do";

}


