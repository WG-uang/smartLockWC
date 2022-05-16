package com.smartschool.smartdoor.service;

import com.alibaba.fastjson.JSONObject;
import com.smartschool.smartdoor.entity.User;

import java.util.ArrayList;
import java.util.HashMap;

public interface UserService {

//    HashMap addUser(User user);
    HashMap addUser(JSONObject jsonObject) throws Exception;
    HashMap queryTTLTocken(JSONObject jsonObject) throws Exception;
    boolean updateUser(User user) throws Exception;
    HashMap selectUserById(String id) throws Exception;
//    HashMap addTTLUserInfo(JSONObject jsonObject) throws Exception;
    HashMap getCollegeName() throws Exception;
    HashMap  getClassInfo(String collegeName) throws Exception;
    HashMap getBuildingName() throws Exception;
    HashMap  getDormitoryInfo(String buildingId) throws Exception;
    HashMap getUserState(String telephoneNumber) throws Exception;
    HashMap enrollUser(String id) throws Exception;
//    HashMap auditUser(String openID,String status) throws Exception;
    HashMap auditUser(JSONObject jsonObject) throws Exception;
}
