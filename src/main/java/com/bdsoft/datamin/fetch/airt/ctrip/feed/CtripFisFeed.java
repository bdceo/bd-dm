package com.bdsoft.datamin.fetch.airt.ctrip.feed;

import java.math.BigDecimal;
import java.util.List;

/**
 * 航班信息
 *
 * @author 丁辰叶
 * @version 1.0
 * @date 2017/11/15 16:35
 */
public class CtripFisFeed {

    private String acc;// 到达城市剑麻
    private String apc; // 到达机场代码
    private String acn; // 到达城市
    private String apbn;// 到达机场
    private String at;// 到达时间：2017-11-16 15:10:00

    private String dpc; // 起飞机场代码
    private String dcn; // 起飞城市
    private String dpbn;// 起飞机场
    private String dt;// 起飞时间：2017-11-16 12:55:00

    private BigDecimal lp;// 机票价格


    private List<CtripScsFeed> scs; // 机票信息


    private String confort;// 机型信息

}
