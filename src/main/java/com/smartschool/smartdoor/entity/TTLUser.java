package com.smartschool.smartdoor.entity;

public class TTLUser {
    private  String  id;
    private  String  ttlUserName;
    private  String  ttlPassword;
    private  String  ttlTocken;

    public TTLUser(){}

    public TTLUser(String id, String ttlUserName, String ttlPassword, String ttlTocken) {
        this.id = id;
        this.ttlUserName = ttlUserName;
        this.ttlPassword = ttlPassword;
        this.ttlTocken = ttlTocken;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTtlUserName() {
        return ttlUserName;
    }

    public void setTtlUserName(String ttlUserName) {
        this.ttlUserName = ttlUserName;
    }

    public String getTtlPassword() {
        return ttlPassword;
    }

    public void setTtlPassword(String ttlPassword) {
        this.ttlPassword = ttlPassword;
    }

    public String getTtlTocken() {
        return ttlTocken;
    }

    public void setTtlTocken(String ttlTocken) {
        this.ttlTocken = ttlTocken;
    }

    @Override
    public String toString() {
        return "TTLUser{" +
                "id='" + id + '\'' +
                ", ttlUserName='" + ttlUserName + '\'' +
                ", ttlPassword='" + ttlPassword + '\'' +
                ", ttlTocken='" + ttlTocken + '\'' +
                '}';
    }
}
