package com.bdsoft.datamin.fetch.airt.jd.feed;

import java.util.List;

/**
 * 航班详情
 *
 * @version 1.0
 * @auth 丁辰叶
 * @date 2017/11/10 9:43
 */
public class JDFlightFeed {

    private Integer addDay; // 几日：0,1
    private String airTime;// 航班飞行时间

    private String airways;// 航空公司代号：CZ
    private String airwaysCn; // 航空公司中文：南方航空

    private String flightNo;// 航班号：CZ3142

    private String arrCity;// 到达机场代码：CSX
    private String arrAirdrome;// 到达机场：黄花机场
    private String arrTerminal;// 到达航站楼：T2
    private String arrDate;// 到达日期：2017-11-11
    private String arrTime;// 到达时间：0015
    private Integer arrTimeType;// 到达时间类型

    private String depCity;// 起飞机场代码：PEK
    private String depAirdrome;// 起飞机场：首都机场
    private String depTerminal;// 起飞航站楼：T2
    private String depDate;// 起飞日期：2017-11-10
    private String depTime;// 起飞时间：2135
    private Integer depTimeType;// 起飞时间类型

    private List<JDFlightClassFeed> bingoClassInfoList; // 机票信息

    private String planeStyle;// 机型编号：321
    private String planeStyleCN;// 机型名称：中型机
    private Integer planeStyleType;// 机型：2

    public JDFlightFeed() {
    }

    public Integer getAddDay() {
        return addDay;
    }

    public void setAddDay(Integer addDay) {
        this.addDay = addDay;
    }

    public String getAirTime() {
        return airTime;
    }

    public void setAirTime(String airTime) {
        this.airTime = airTime;
    }

    public String getAirways() {
        return airways;
    }

    public void setAirways(String airways) {
        this.airways = airways;
    }

    public String getAirwaysCn() {
        return airwaysCn;
    }

    public void setAirwaysCn(String airwaysCn) {
        this.airwaysCn = airwaysCn;
    }

    public String getFlightNo() {
        return flightNo;
    }

    public void setFlightNo(String flightNo) {
        this.flightNo = flightNo;
    }

    public String getArrCity() {
        return arrCity;
    }

    public void setArrCity(String arrCity) {
        this.arrCity = arrCity;
    }

    public String getArrAirdrome() {
        return arrAirdrome;
    }

    public void setArrAirdrome(String arrAirdrome) {
        this.arrAirdrome = arrAirdrome;
    }

    public String getArrTerminal() {
        return arrTerminal;
    }

    public void setArrTerminal(String arrTerminal) {
        this.arrTerminal = arrTerminal;
    }

    public String getArrDate() {
        return arrDate;
    }

    public void setArrDate(String arrDate) {
        this.arrDate = arrDate;
    }

    public String getArrTime() {
        return arrTime;
    }

    public void setArrTime(String arrTime) {
        this.arrTime = arrTime;
    }

    public Integer getArrTimeType() {
        return arrTimeType;
    }

    public void setArrTimeType(Integer arrTimeType) {
        this.arrTimeType = arrTimeType;
    }

    public String getDepCity() {
        return depCity;
    }

    public void setDepCity(String depCity) {
        this.depCity = depCity;
    }

    public String getDepAirdrome() {
        return depAirdrome;
    }

    public void setDepAirdrome(String depAirdrome) {
        this.depAirdrome = depAirdrome;
    }

    public String getDepTerminal() {
        return depTerminal;
    }

    public void setDepTerminal(String depTerminal) {
        this.depTerminal = depTerminal;
    }

    public String getDepDate() {
        return depDate;
    }

    public void setDepDate(String depDate) {
        this.depDate = depDate;
    }

    public String getDepTime() {
        return depTime;
    }

    public void setDepTime(String depTime) {
        this.depTime = depTime;
    }

    public Integer getDepTimeType() {
        return depTimeType;
    }

    public void setDepTimeType(Integer depTimeType) {
        this.depTimeType = depTimeType;
    }

    public List<JDFlightClassFeed> getBingoClassInfoList() {
        return bingoClassInfoList;
    }

    public void setBingoClassInfoList(List<JDFlightClassFeed> bingoClassInfoList) {
        this.bingoClassInfoList = bingoClassInfoList;
    }

    public String getPlaneStyle() {
        return planeStyle;
    }

    public void setPlaneStyle(String planeStyle) {
        this.planeStyle = planeStyle;
    }

    public String getPlaneStyleCN() {
        return planeStyleCN;
    }

    public void setPlaneStyleCN(String planeStyleCN) {
        this.planeStyleCN = planeStyleCN;
    }

    public Integer getPlaneStyleType() {
        return planeStyleType;
    }

    public void setPlaneStyleType(Integer planeStyleType) {
        this.planeStyleType = planeStyleType;
    }
}
