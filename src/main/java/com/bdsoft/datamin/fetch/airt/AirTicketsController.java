package com.bdsoft.datamin.fetch.airt;


import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bdsoft.datamin.fetch.airt.feed.AirStationFeed;
import com.bdsoft.datamin.util.http.NetUtil;

/**
 * 功能
 *
 * @version 1.0
 * @auth 丁辰叶
 * @date 2017/11/3 10:42
 */
public class AirTicketsController {

    private static Logger log = LoggerFactory.getLogger(AirTicketsController.class);

    public static void main(String[] args) {
        airStationShortCode();
    }

    /**
     * 机场简称代码信息
     */
    public static void airStationShortCode() {
        int per = 50;
        int page = 1;
        int total = 4;
        String baseUrl = "http://www.6qt.net/index.asp?Field=Country&keyword=%D6%D0%B9%FA";
        String pageTmp = "&MaxPerPage=%d&page=%d";

        // 初始化url
        List<String> urls = new ArrayList<>(100);
        urls.add("http://www.6qt.net/index.asp?Field=City&keyword=%CF%E3%B8%DB");// 香港
        urls.add("http://www.6qt.net/index.asp?Field=City&keyword=%B0%C4%C3%C5");// 澳门

        // 分页加载
        for (int i = page; i <= total; i++) {
            urls.add(baseUrl + String.format(pageTmp, per, i));
        }

        // 开始抓取
        urls.forEach(url -> {
            fetchShortCode(url);
        });

    }

    /**
     * 抓取提取
     */
    public static void fetchShortCode(String url) {
        String html = NetUtil.getHtmlSrc(url, NetUtil.CHARSET_GB2312);
        Document doc = Jsoup.parse(html);

        Elements trs = doc.select("tr[onclick*=location].tdbg");
        log.info("开始抓取：{}，待提取：{}行数据", url, trs.size());

        // 遍历机场
        for (Element tr : trs) {
            AirStationFeed feed = new AirStationFeed();
            Element td = tr.select("td").first();
            feed.setCityName(td.text());

            td = td.nextElementSibling();
            feed.setStationCode3(td.text());

            td = td.nextElementSibling();
            feed.setCountryName(td.text());

            td = td.nextElementSibling();
            feed.setCountryCode(td.text());

            td = td.nextElementSibling();
            feed.setStationCode4(td.text());

            td = td.nextElementSibling();
            feed.setStationName(td.text());

            td = td.nextElementSibling();
            feed.setCityEnName(td.text());

            log.info("城市={}, 三字代码={}, 国家={}, 国家代码={}, 四字代码={}, 机场名称={}, 英文名称={}", feed.getCityName()
                    , feed.getStationCode3(), feed.getCountryName(), feed.getCountryCode()
                    , feed.getStationCode4(), feed.getStationName(), feed.getCityEnName());
        }
    }


}
