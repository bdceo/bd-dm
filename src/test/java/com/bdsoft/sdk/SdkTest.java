package com.bdsoft.sdk;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Created by Administrator on 2019/1/22.
 */
public class SdkTest {

    public static void main(String[] args) {
        String log="请求路径为 url:/hshcwxweb/rushBuy/activityPhoto,ip:117.136.27.190,refer:https://servicewechat.com/wxdc1a7649d96b3964/42/page-frame.html,userAgent:Mozilla/5.0 (Linux; Android 7.1.1; OPPO R11s Build/NMF26X; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/55.0.2883.91 Mobile Safari/537.36 MicroMessenger/7.0.1380(0x27000038) Process/appbrand0 NetType/4G Language/zh_CN";

        String logContent = log.split("url:")[1];
        String url = logContent.substring(0, logContent.indexOf(",ip"));
        String ua = logContent.split("userAgent:")[1];
        System.out.println(String.format("url=%s, ua=%s", url, ua));

        System.out.println(ua.replaceAll(",",""));
    }
}
