package com.bdsoft.datamin.fetch.hshc;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bdsoft.datamin.util.BDFileUtil;
import com.bdsoft.datamin.util.http.BDHttpParam;
import com.bdsoft.datamin.util.http.BDHttpUtil;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * log 收集：http-header
 */
public class HshcLogFetcher {

    /**
     * 收集日志请求头信息
     */
    public static void main(String[] args) {
        // 初始参数
        String keyword = "请求路径";
        Integer pageNum = 1;
        Integer pageTotal = 200;
        LocalDateTime endTime = LocalDateTime.now();
        LocalDateTime startTime = endTime.minusHours(HshcConfig.HOUR_SEP);

        int total = 0;
        int max = 1_0000_0000;
        while (total < max) {
            // 查询参数
            LogParam logParam = new LogParam();
            logParam.setKeyWord(keyword);
            logParam.setPageNum((pageNum - 1) * HshcConfig.PAGE_SIZE);
            logParam.setStartTime(startTime.format(HshcConfig.DT_FORMAT));
            logParam.setEndTime(endTime.format(HshcConfig.DT_FORMAT));

            // 抓取日志
            String src = fetchLog(logParam);
            try {
                // 解析
                JSONObject jsonData = JSONObject.parseObject(src);
                parseLog(jsonData);

                // 计算分页数
                if (pageNum == 1) {
                    int logTotal = jsonData.getIntValue("totalCount");
                    pageTotal = logTotal / HshcConfig.PAGE_SIZE + 1;
                    System.out.println(String.format("计算分页：logTotal=%d, pageTotal=%d", logTotal, pageTotal));
                }
                pageNum++;

                // 重新计算开始结束时间
                if (pageNum >= pageTotal) {
                    pageNum = 1;
                    pageTotal = 200;
                    endTime = endTime.minusHours(HshcConfig.HOUR_SEP);
                    startTime = startTime.minusHours(HshcConfig.HOUR_SEP);
                    System.out.println(String.format("计算时间段：start=%s, end=%s",
                            startTime.format(HshcConfig.DT_FORMAT), endTime.format(HshcConfig.DT_FORMAT)));
                }
            } catch (Exception e) {
                System.out.println("-----------------会话过期---------------");
                break;
            }
            try {
                Thread.sleep(new Random().nextInt(5_000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void parseLog(JSONObject jsonData) {
        List<LogItem> logs = new ArrayList<>(jsonData.size());
        JSONArray logItems = jsonData.getJSONArray("logResultVoList");
        for (int i = 0; i < logItems.size(); i++) {
            JSONObject item = logItems.getJSONObject(i);
            String log = item.getString("logMessage");
            System.out.println("开始解析日志：" + log);
            if (log.indexOf("-> header:") > 0) {
                String logHeader = log.split("header:")[1];
                JSONObject headerJson = JSONObject.parseObject(logHeader);
                logs.add(new LogItem(item.getString("logTime"), item.getString("serverIp"),
                        headerJson.getString("requestURI"), headerJson.getString("userAgent")));
            } else {
                String logContent = log.split("url:")[1];
                String url = logContent.substring(0, logContent.indexOf(",ip"));
                String ua = logContent.split("userAgent:")[1];
                logs.add(new LogItem(item.getString("logTime"), item.getString("serverIp"), url, ua));
            }
        }

        // 记录日志到csv
        recordLogToCsv(logs);
    }

    public static void recordLogToCsv(List<LogItem> logs) {
        StringBuilder csv = new StringBuilder(logs.size() * 100);
        for (LogItem log : logs) {
            csv.append(log);
        }
        BDFileUtil.appendWrite("d:/download/wxweb-log.csv", csv.toString().getBytes());
    }

    public static String fetchLog(LogParam logParam) {
        BDHttpParam param = BDHttpParam.init();
        param.addCommon("projectName", logParam.getProjectName());
        param.addCommon("env", logParam.getEnv());
        param.addCommon("pageNum", logParam.getPageNum().toString());
        param.addCommon("pageSize", logParam.getPageSize().toString());
        param.addCommon("orderBy", logParam.getOrderBy().toString());
        param.addCommon("orderType", logParam.getOrderType().toString());
        param.addCommon("isException", logParam.getIsException().toString());
        param.addCommon("startTime", logParam.getStartTime());
        param.addCommon("endTime", logParam.getEndTime());
        param.addCommon("keyWord", logParam.getKeyWord());

        param.addCookie("HSHC_USER_COOKIE", "02a8545f-4211-4eef-809b-7ae7e0951f63");

        return BDHttpUtil.sendGet(HshcConfig.LOG_API, param);
    }


}
