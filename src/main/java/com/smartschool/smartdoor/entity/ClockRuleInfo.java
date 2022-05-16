package com.smartschool.smartdoor.entity;

public class ClockRuleInfo {
    private String clockId;
    private String equipmentId;
    private String clockDate;
    private String clockTime;
    private String type;

    public String getClockId() {
        return clockId;
    }

    public void setClockId(String clockId) {
        this.clockId = clockId;
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


    public String getClockTime() {
        return clockTime;
    }

    public void setClockTime(String clockTime) {
        this.clockTime = clockTime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ClockRuleInfo(){}

    public ClockRuleInfo(String clockId, String equipmentId, String clockDate, String clockTime, String type) {
        this.clockId = clockId;
        this.equipmentId = equipmentId;
        this.clockDate = clockDate;
        this.clockTime = clockTime;
        this.type = type;
    }

    @Override
    public String toString() {
        return "ClockRuleInfo{" +
                "clockId='" + clockId + '\'' +
                ", equipmentId='" + equipmentId + '\'' +
                ", clockDate='" + clockDate + '\'' +
                ", clockTime='" + clockTime + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
