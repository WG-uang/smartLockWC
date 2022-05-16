package com.smartschool.smartdoor.entity;

import javax.xml.crypto.Data;

public class ClockRule {
    private String id;
    private String clock_id;
    private String equipment_id;
    private String equipment_name;
    private Data clock_time;
    private String type;

    public ClockRule(String id, String clock_id, String equipment_id, Data clock_time, String type) {
        this.id = id;
        this.clock_id = clock_id;
        this.equipment_id = equipment_id;
        this.clock_time = clock_time;
        this.type = type;
    }

    public ClockRule(String id, String clock_id, String equipment_id, String equipment_name, Data clock_time, String type) {
        this.id = id;
        this.clock_id = clock_id;
        this.equipment_id = equipment_id;
        this.equipment_name = equipment_name;
        this.clock_time = clock_time;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClock_id() {
        return clock_id;
    }

    public void setClock_id(String clock_id) {
        this.clock_id = clock_id;
    }

    public String getEquipment_id() {
        return equipment_id;
    }

    public void setEquipment_id(String equipment_id) {
        this.equipment_id = equipment_id;
    }

    public String getEquipment_name() {
        return equipment_name;
    }

    public void setEquipment_name(String equipment_name) {
        this.equipment_name = equipment_name;
    }

    public Data getClock_time() {
        return clock_time;
    }

    public void setClock_time(Data clock_time) {
        this.clock_time = clock_time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "ClockRule{" +
                "id='" + id + '\'' +
                ", clock_id='" + clock_id + '\'' +
                ", equipment_id='" + equipment_id + '\'' +
                ", equipment_name='" + equipment_name + '\'' +
                ", clock_time=" + clock_time +
                ", type='" + type + '\'' +
                '}';
    }
}
