package com.bdsoft.datamin.fetch.airt.feed;

/**
 * Created by bdceo on 2017/11/5.
 */
public class AirStationFeed {

    private String cityName;
    private String cityEnName;

    private String stationName;
    private String stationCode3;
    private String stationCode4;

    private String countryName;
    private String countryCode;

    @Override
    public String toString() {
        return "机场信息{" +
                "城市='" + cityName + '\'' +
                ", 城市英文='" + cityEnName + '\'' +
                ", 机场名称='" + stationName + '\'' +
                ", 3字码='" + stationCode3 + '\'' +
                ", 4字码='" + stationCode4 + '\'' +
                ", 国家='" + countryName + '\'' +
                ", 国家码='" + countryCode + '\'' +
                '}';
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCityEnName() {
        return cityEnName;
    }

    public void setCityEnName(String cityEnName) {
        this.cityEnName = cityEnName;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public String getStationCode3() {
        return stationCode3;
    }

    public void setStationCode3(String stationCode3) {
        this.stationCode3 = stationCode3;
    }

    public String getStationCode4() {
        return stationCode4;
    }

    public void setStationCode4(String stationCode4) {
        this.stationCode4 = stationCode4;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }
}
