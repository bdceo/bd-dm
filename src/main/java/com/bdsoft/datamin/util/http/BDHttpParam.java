package com.bdsoft.datamin.util.http;

import com.google.common.collect.Maps;

import java.util.Map;

/**
 * 参数自定义传输封装
 */
public class BDHttpParam {

    // 通过cookie传输的参数名称定义
    public final static String COOKIE_TOKEN = "vkoweb";
    public final static String COOKIE_TERMINAL = "terminal";
    public final static String COOKIE_SID = "sid";

    // 通过http头自定义传输的参数名称定义
    public final static String HEADER_IP = "X-Forwarded-For";
    public final static String HEADER_CONTENT_TYPE = "Content-Type";
    public final static String HEADER_CONTENT_TYPE_FORM = "application/x-www-form-urlencoded";
    public final static String HEADER_CONTENT_TYPE_JSON = "application/json";

    // 默认字符集
    private String charset = NetUtil.CHARSET_UTF8;

    private Map<String, String> commonParams; // get/post 传参
    private Map<String, String> cookieParams; // cookie 传参
    private Map<String, String> headerParams; // header 传参

    /**
     * 私有初始化
     */
    private BDHttpParam() {
        commonParams = Maps.newHashMap();
        cookieParams = Maps.newHashMap();

        headerParams = Maps.newHashMap();
        headerParams.put("Accept-Encoding", "gzip");
        headerParams.put("User-Agent", NetUtil.BROWSER_UA);
        headerParams.put("Content-Type", HEADER_CONTENT_TYPE_FORM);
    }

    /**
     * 静态调用初始化
     */
    public static BDHttpParam init() {
        return new BDHttpParam();
    }

    public boolean hasCommon() {
        return !getCommonParams().isEmpty();
    }

    public boolean hasCookie() {
        return !getCookieParams().isEmpty();
    }

    public boolean hasHeader() {
        return !getHeaderParams().isEmpty();
    }

    public boolean sendWithForm() {
        return getHeaderParams().get(HEADER_CONTENT_TYPE).equalsIgnoreCase(HEADER_CONTENT_TYPE_FORM);
    }

    public boolean sendWithJson() {
        return getHeaderParams().get(HEADER_CONTENT_TYPE).equalsIgnoreCase(HEADER_CONTENT_TYPE_JSON);
    }

    public BDHttpParam addCommon(String key, String value) {
        getCommonParams().put(key, value);
        return this;
    }

    public BDHttpParam addCookie(String key, String value) {
        getCookieParams().put(key, value);
        return this;
    }

    public BDHttpParam addHeader(String key, String value) {
        getHeaderParams().put(key, value);
        return this;
    }

    public BDHttpParam setCharset(String charset) {
        this.charset = charset;
        return this;
    }

    public BDHttpParam setContentType(String contentType) {
        getHeaderParams().put(HEADER_CONTENT_TYPE, contentType);
        return this;
    }

    public Map<String, String> getCommonParams() {
        return commonParams;
    }

    public Map<String, String> getCookieParams() {
        return cookieParams;
    }

    public Map<String, String> getHeaderParams() {
        return headerParams;
    }

    public String getCharset() {
        return charset;
    }

    public String getContentType() {
        return getHeaderParams().get(HEADER_CONTENT_TYPE);
    }

}
