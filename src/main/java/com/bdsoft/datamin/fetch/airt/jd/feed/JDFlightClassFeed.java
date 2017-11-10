package com.bdsoft.datamin.fetch.airt.jd.feed;

import java.math.BigDecimal;

/**
 * 机票详情
 *
 * @version 1.0
 * @auth 丁辰叶
 * @date 2017/11/10 10:14
 */
public class JDFlightClassFeed {

    private Integer airCompanyFlag;// 是否航空公司
    private String sourceId;// 销售渠道：621906-京东，138669-国航，188126-商家优选，615927-南方航空，138669-东方航空

    private Boolean fastIssueSwitch;// 是否极速出票
    private Integer fastIssueTime;// 出票时间：5

    private Integer classLevel;// 仓位等级：1-经济舱，2-头等舱，3-商务舱
    private String classNo;// 仓位代码：B，Y
    private String classNoCn;// 仓位名称：经济舱
    private String classText;// 仓位描述：经济舱全价机票

    private BigDecimal discount;// 折扣：9.0，10.0
    private BigDecimal price;// 价格：1339
    private BigDecimal originalPrice;// 原价：1339
    private BigDecimal venderPrice;// 供应商价格：1339

    private BigDecimal reducePrice;// 促销减：10

    private Integer luggage;// 限行李重量：20
    private String luggageText;// 行李描述：免费托运20KG
    private BigDecimal mile;// 累计里程：1.25

    private String seatType;// 座位类型
    private String seatNum;// 剩余票数

    public JDFlightClassFeed() {
    }


    public Integer getAirCompanyFlag() {
        return airCompanyFlag;
    }

    public void setAirCompanyFlag(Integer airCompanyFlag) {
        this.airCompanyFlag = airCompanyFlag;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public Boolean getFastIssueSwitch() {
        return fastIssueSwitch;
    }

    public void setFastIssueSwitch(Boolean fastIssueSwitch) {
        this.fastIssueSwitch = fastIssueSwitch;
    }

    public Integer getFastIssueTime() {
        return fastIssueTime;
    }

    public void setFastIssueTime(Integer fastIssueTime) {
        this.fastIssueTime = fastIssueTime;
    }

    public Integer getClassLevel() {
        return classLevel;
    }

    public void setClassLevel(Integer classLevel) {
        this.classLevel = classLevel;
    }

    public String getClassNo() {
        return classNo;
    }

    public void setClassNo(String classNo) {
        this.classNo = classNo;
    }

    public String getClassNoCn() {
        return classNoCn;
    }

    public void setClassNoCn(String classNoCn) {
        this.classNoCn = classNoCn;
    }

    public String getClassText() {
        return classText;
    }

    public void setClassText(String classText) {
        this.classText = classText;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(BigDecimal originalPrice) {
        this.originalPrice = originalPrice;
    }

    public BigDecimal getVenderPrice() {
        return venderPrice;
    }

    public void setVenderPrice(BigDecimal venderPrice) {
        this.venderPrice = venderPrice;
    }

    public BigDecimal getReducePrice() {
        return reducePrice;
    }

    public void setReducePrice(BigDecimal reducePrice) {
        this.reducePrice = reducePrice;
    }

    public Integer getLuggage() {
        return luggage;
    }

    public void setLuggage(Integer luggage) {
        this.luggage = luggage;
    }

    public String getLuggageText() {
        return luggageText;
    }

    public void setLuggageText(String luggageText) {
        this.luggageText = luggageText;
    }

    public BigDecimal getMile() {
        return mile;
    }

    public void setMile(BigDecimal mile) {
        this.mile = mile;
    }

    public String getSeatType() {
        return seatType;
    }

    public void setSeatType(String seatType) {
        this.seatType = seatType;
    }

    public String getSeatNum() {
        return seatNum;
    }

    public void setSeatNum(String seatNum) {
        this.seatNum = seatNum;
    }
}
