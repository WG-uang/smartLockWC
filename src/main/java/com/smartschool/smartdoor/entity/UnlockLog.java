package com.smartschool.smartdoor.entity;

public class UnlockLog {
    private String id;
    private String equipment_id;
    private String unlock_time;
    private String unlock_date;
    private String equipment_name;
    private String equipment_place;
    private String user_types;
    private String user_method;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEquipment_id() {
        return equipment_id;
    }

    public void setEquipment_id(String equipment_id) {
        this.equipment_id = equipment_id;
    }

    public String getUnlock_time() {
        return unlock_time;
    }

    public void setUnlock_time(String unlock_time) {
        this.unlock_time = unlock_time;
    }

    public String getUnlock_date() {
        return unlock_date;
    }

    public void setUnlock_date(String unlock_date) {
        this.unlock_date = unlock_date;
    }

    public String getEquipment_name() {
        return equipment_name;
    }

    public void setEquipment_name(String equipment_name) {
        this.equipment_name = equipment_name;
    }

    public String getEquipment_place() {
        return equipment_place;
    }

    public void setEquipment_place(String equipment_place) {
        this.equipment_place = equipment_place;
    }

    public String getUser_types() {
        return user_types;
    }

    public void setUser_types(String user_types) {
        this.user_types = user_types;
    }

    public String getUser_method() {
        return user_method;
    }

    public void setUser_method(String user_method) {
        this.user_method = user_method;
    }
    public UnlockLog(){

    }

    public UnlockLog(String id, String equipment_id, String unlock_time, String unlock_date, String equipment_name, String equipment_place, String user_types, String user_method) {
        this.id = id;
        this.equipment_id = equipment_id;
        this.unlock_time = unlock_time;
        this.unlock_date = unlock_date;
        this.equipment_name = equipment_name;
        this.equipment_place = equipment_place;
        this.user_types = user_types;
        this.user_method = user_method;
    }

    @Override
    public String toString() {
        return "unlockLog{" +
                "id='" + id + '\'' +
                ", equipment_id='" + equipment_id + '\'' +
                ", unlock_time='" + unlock_time + '\'' +
                ", unlock_date='" + unlock_date + '\'' +
                ", equipment_name='" + equipment_name + '\'' +
                ", equipment_place='" + equipment_place + '\'' +
                ", user_types='" + user_types + '\'' +
                ", user_method='" + user_method + '\'' +
                '}';
    }
}
