package com.smartschool.smartdoor.service;

import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;

public interface WorkService {
    HashMap selectClockRuleByBuildingId(String id)  throws Exception;
    HashMap selectClockRuleByClassId(String id) throws Exception;
    HashMap addHoliday(JSONObject jsonObject) throws Exception;
    HashMap addDataDate(JSONObject jsonObject) throws Exception;
}
