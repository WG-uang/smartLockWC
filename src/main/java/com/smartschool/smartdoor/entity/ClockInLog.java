package com.smartschool.smartdoor.entity;

public class ClockInLog {
    private String id;
    private String equipmentId;
    private String time;
    private String clockDate;
    private String place;
    private String ruleTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEquipmentId() {
        return equipmentId;
    }

    public void setEquipmentId(String equipmentId) {
        this.equipmentId = equipmentId;
    }

    public String getClockDate() {
        return clockDate;
    }

    public void setClockDate(String clockDate) {
        this.clockDate = clockDate;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getRuleTime() {
        return ruleTime;
    }

    public void setRuleTime(String ruleTime) {
        this.ruleTime = ruleTime;
    }
    public ClockInLog() {}

    public ClockInLog(String id, String equipmentId,String clockDate, String time, String place, String ruleTime) {
        this.id = id;
        this.equipmentId = equipmentId;
        this.clockDate = clockDate;
        this.time = time;
        this.place = place;
        this.ruleTime = ruleTime;
    }

    @Override
    public String toString() {
        return "ClockInLog{" +
                "id='" + id + '\'' +
                ", equipmentId='" + equipmentId + '\'' +
                ", clockDate='" + clockDate + '\'' +
                ", time='" + time + '\'' +
                ", place='" + place + '\'' +
                ", ruleTime='" + ruleTime + '\'' +
                '}';
    }
}
