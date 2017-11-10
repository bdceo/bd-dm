package com.bdsoft.datamin.fetch.airt.jd.feed;

import java.util.List;

/**
 * 机票接口-响应结构
 *
 * @version 1.0
 * @auth 丁辰叶
 * @date 2017/11/10 9:43
 */
public class JDFlightData {

    private Object captchaInfo;
    private String desc;
    private List<JDFlightFeed> flights; // 航班列表
    private String interval;
    private Integer isFinished; // 是否使用
    private String queryDate;
    private String queryuuid;
    private String resultCode;

    public JDFlightData() {
    }

    public boolean isFinish() {
        return isFinished == 1;
    }

    public Object getCaptchaInfo() {
        return captchaInfo;
    }

    public void setCaptchaInfo(Object captchaInfo) {
        this.captchaInfo = captchaInfo;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public List<JDFlightFeed> getFlights() {
        return flights;
    }

    public void setFlights(List<JDFlightFeed> flights) {
        this.flights = flights;
    }

    public String getInterval() {
        return interval;
    }

    public void setInterval(String interval) {
        this.interval = interval;
    }

    public Integer getIsFinished() {
        return isFinished;
    }

    public void setIsFinished(Integer isFinished) {
        this.isFinished = isFinished;
    }

    public String getQueryDate() {
        return queryDate;
    }

    public void setQueryDate(String queryDate) {
        this.queryDate = queryDate;
    }

    public String getQueryuuid() {
        return queryuuid;
    }

    public void setQueryuuid(String queryuuid) {
        this.queryuuid = queryuuid;
    }

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }
}
