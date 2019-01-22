package com.bdsoft.datamin.fetch.hshc;

import java.net.URLEncoder;

public class LogParam {

    private String projectName = "hshcwxweb";
    private String env = "production";

    // 分页参数
    private Integer pageNum = 0;
    private Integer pageSize = HshcConfig.PAGE_SIZE;

    // 1-匹配度，2-日志时间
    private Integer orderBy = 2;
    // 0-降序，1-升序
    private Integer orderType = 0;

    // 是否异常日志
    private Integer isException = 0;

    // 起止时间
    private String startTime;
    private String endTime;

    // 关键词
    private String keyWord;

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getEnv() {
        return env;
    }

    public void setEnv(String env) {
        this.env = env;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(Integer orderBy) {
        this.orderBy = orderBy;
    }

    public Integer getOrderType() {
        return orderType;
    }

    public void setOrderType(Integer orderType) {
        this.orderType = orderType;
    }

    public Integer getIsException() {
        return isException;
    }

    public void setIsException(Integer isException) {
        this.isException = isException;
    }

    public String getStartTime() {
        return URLEncoder.encode(startTime);
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return URLEncoder.encode(endTime);
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getKeyWord() {
        return URLEncoder.encode(keyWord);
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }
}
