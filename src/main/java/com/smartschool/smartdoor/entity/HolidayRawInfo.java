package com.smartschool.smartdoor.entity;

public class HolidayRawInfo {
    private String holiday;  //是否是节假日
    private String name;      //节假日名称
    private String date;      //日期
    private String week;

    public String getHoliday() {
        return holiday;
    }

    public void setHoliday(String holiday) {
        this.holiday = holiday;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public HolidayRawInfo(){}

    public HolidayRawInfo(String holiday, String name, String date, String week) {
        this.holiday = holiday;
        this.name = name;
        this.date = date;
        this.week = week;
    }
}
