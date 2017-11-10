package com.bdsoft.datamin.fetch.airt.jd.feed;

/**
 * 京东-响应数据结构
 *
 * @version 1.0
 * @auth 丁辰叶
 * @date 2017/11/10 9:38
 */
public class JDResult<T> {

    private int code;
    private T data;

    public JDResult() {
    }

    public boolean isOk() {
        return code == 200;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
