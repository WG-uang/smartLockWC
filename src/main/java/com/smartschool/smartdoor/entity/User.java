package com.smartschool.smartdoor.entity;

import org.springframework.stereotype.Repository;

public class User {
    private String id;
    private String name;
    private String sex;
    private String id_card;
    private String workID;
    private String dormitory_id;
    private String class_id;
    private String building_id;
    private String college_id;
    private String telephone_number;
    private String types;
    private String sp_name;
    private String priority;
    private String status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getId_card() {
        return id_card;
    }

    public void setId_card(String id_card) {
        this.id_card = id_card;
    }

    public String getWorkID() {
        return workID;
    }

    public void setWorkID(String workID) {
        this.workID = workID;
    }

    public String getDormitory_id() {
        return dormitory_id;
    }

    public void setDormitory_id(String dormitory_id) {
        this.dormitory_id = dormitory_id;
    }

    public String getClass_id() {
        return class_id;
    }

    public void setClass_id(String class_id) {
        this.class_id = class_id;
    }

    public String getBuilding_id() {
        return building_id;
    }

    public void setBuilding_id(String building_id) {
        this.building_id = building_id;
    }

    public String getCollege_id() {
        return college_id;
    }

    public void setCollege_id(String college_id) {
        this.college_id = college_id;
    }

    public String getTelephone_number() {
        return telephone_number;
    }

    public void setTelephone_number(String telephone_number) {
        this.telephone_number = telephone_number;
    }

    public String getTypes() {
        return types;
    }

    public void setTypes(String types) {
        this.types = types;
    }

    public String getSp_name() {
        return sp_name;
    }

    public void setSp_name(String sp_name) {
        this.sp_name = sp_name;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    public User(){}

    public User(String id, String name, String sex, String id_card, String workID, String dormitory_id, String class_id, String building_id, String college_id, String telephone_number, String types, String sp_name, String priority, String status) {
        this.id = id;
        this.name = name;
        this.sex = sex;
        this.id_card = id_card;
        this.workID = workID;
        this.dormitory_id = dormitory_id;
        this.class_id = class_id;
        this.building_id = building_id;
        this.college_id = college_id;
        this.telephone_number = telephone_number;
        this.types = types;
        this.sp_name = sp_name;
        this.priority = priority;
        this.status = status;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", sex='" + sex + '\'' +
                ", id_card='" + id_card + '\'' +
                ", workID='" + workID + '\'' +
                ", dormitory_id='" + dormitory_id + '\'' +
                ", class_id='" + class_id + '\'' +
                ", building_id='" + building_id + '\'' +
                ", college_id='" + college_id + '\'' +
                ", telephone_number='" + telephone_number + '\'' +
                ", types='" + types + '\'' +
                ", sp_name='" + sp_name + '\'' +
                ", priority='" + priority + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
