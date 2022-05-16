package com.smartschool.smartdoor.controller;

import com.alibaba.fastjson.JSONObject;
import com.smartschool.smartdoor.service.WorkService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;

@RestController
@RequestMapping(value = "/work")
public class WorkController {

    @Resource
    private WorkService workService;

    /**
     * 查询打卡规则-openid 查学生楼栋信息，然后通过楼栋号查设置的打卡规则时间
     */
    @RequestMapping("/selectClockRule")
    public void selectClockRule(@RequestBody JSONObject jsonObject){
        System.out.println(jsonObject.toJSONString());
        String openID = (String) jsonObject.get("id");
    }
    /**
     * 插入日期表
     */
    @RequestMapping(value = "/addData",method = RequestMethod.POST)
    public String addData(@RequestBody JSONObject jsonObject){
        try {
            HashMap hashMapTemp = workService.addDataDate(jsonObject);
            if ((boolean)hashMapTemp.get("flag")) {
                return resultJsonClass("200",(String)hashMapTemp.get("msg"),"");
            }else {
                return resultJsonClass("500",(String)hashMapTemp.get("msg"),"");
            }
        }catch (Exception e){
            e.printStackTrace();
            return resultJsonClass("500","异常错误","");
        }
    }
    /**
     * 维护节假日表
     */
    @RequestMapping(value = "/addHolidayData",method = RequestMethod.POST)
    public String addHolidayData(@RequestBody JSONObject jsonObject){
        try{
            HashMap hashMapTemp = workService.addHoliday(jsonObject);
            if ((boolean)hashMapTemp.get("flag")) {
                return resultJsonClass("200",(String)hashMapTemp.get("msg"),"");
            }else {
                return resultJsonClass("500",(String)hashMapTemp.get("msg"),"");
            }
        }catch (Exception e){
            e.printStackTrace();
            return resultJsonClass("500","异常错误","");
        }
    }

    //返回JSON响应对象
    public String resultJsonClass(String status,String msg,Object data){
        JSONObject resultJsonTemp = new JSONObject();
        resultJsonTemp.put("status",status);
        resultJsonTemp.put("msg",msg);
        resultJsonTemp.put("data",data);
        return resultJsonTemp.toJSONString();
    }
}
