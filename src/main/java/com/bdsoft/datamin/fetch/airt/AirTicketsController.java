package com.bdsoft.datamin.fetch.airt;

/**
 * 功能
 *
 * @version 1.0
 * @auth 丁辰叶
 * @date 2017/11/3 10:42
 */
public class AirTicketsController {


    public static void main(String[] args) {
        airStation();
    }

    /**
     * 机场简称代码信息
     */
    public static void airStation() {
        int per = 50;
        int page = 1;
        int total = 4;
        String baseUrl = "http://www.6qt.net/index.asp?Field=Country&keyword=%D6%D0%B9%FA";
        String pageTmp = "&MaxPerPage=%d&page=%d";

        for (int i = page; i <= total; i++) {
            String url = String.format(pageTmp, per, i);
            System.out.println(baseUrl + url);
        }

    }


}
