package com.bdsoft.datamin.util.http;

import java.util.ArrayList;
import java.util.List;

/**
 * 功能
 *
 * @version 1.0
 * @auth 丁辰叶
 * @date 2017/11/3 18:32
 */
public class BDPager {

    private int total;// 总页数
    private int page = 1; // 当前页
    private int per; // 每页条数

    private String pageTmp; // 分页url

    public BDPager(int total, int page, int per, String pageTmp) {
        this.total = total;
        this.page = page;
        this.per = per;
        this.pageTmp = pageTmp;
    }

    public List<String> listPageUrl() {
        List<String> urls = new ArrayList<>(total);
        for (int i = page; i <= total; i++) {
            urls.add(String.format(pageTmp, per, i));
        }
        page = total;
        return urls;
    }

    public String getNextPage() {
        this.page++;
        return String.format(pageTmp, per, page);
    }

}
