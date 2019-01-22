package com.bdsoft.datamin.fetch.hshc;

/**
 * 日志收集对象
 */
public class LogItem {

    // 时间
    private String dt;
    // 机器
    private String ip;
    // 地址
    private String url;
    // 客户端
    private String ua;

    public LogItem(String dt, String ip, String url, String ua) {
        this.dt = dt;
        this.ip = ip;
        this.url = url;
        this.ua = ua;
    }

    @Override
    public String toString() {
        return new StringBuilder(dt).append(",").
                append(ip).append(",").
                append(url).append(",").
                append(ua.replaceAll(",", "")).append("\n").toString();
    }

    public String getDt() {
        return dt;
    }

    public void setDt(String dt) {
        this.dt = dt;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUa() {
        return ua;
    }

    public void setUa(String ua) {
        this.ua = ua;
    }
}
