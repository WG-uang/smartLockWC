package com.smartschool.smartdoor.service;

import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;

public interface LockService {

    HashMap selectEquipmentIdById(String id) throws Exception;

    //HashMap getLockListFromTTL(String access_token,String pageNo,String pageSize,String lockAlias,String groupId);
//    HashMap getLockListFromTTL(JSONObject jsonObject);
//    HashMap addClockIn(String openID,String lockId,String lockName,String clockTime,String ruleTime);
    HashMap addClockIn(JSONObject jsonObject) throws Exception;
    HashMap queryClockIn(String openID) throws Exception;
//    HashMap addUnlockLog(String openID,String lockId,String lockName,String unlockTime);
    HashMap getDLOfLock(String lockId) throws Exception;
    HashMap addUnlockLog(JSONObject jsonObject) throws Exception;
    HashMap addEquipment(JSONObject jsonObject) throws Exception;
    HashMap addClockRuleInfo(JSONObject jsonObject) throws Exception;
}
