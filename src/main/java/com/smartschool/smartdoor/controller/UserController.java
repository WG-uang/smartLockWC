package com.smartschool.smartdoor.controller;

import com.alibaba.fastjson.JSONObject;
import com.smartschool.smartdoor.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.HashMap;

@RestController
@RequestMapping(value = "/dy")
public class UserController {
    @Resource
    private UserService userService;

    @Resource
    private RestTemplate restTemplate;
    private String jsonString = null;
    String msgTemp="";
    int status = 500;

    /**
     * 完善信息，先查是否存在，若存在则对比电话号码，一样则判断是同一个人，则修改，不一样提示该个人信息已经存在，
     *                       若不存在则添加
     * @params
     * @return
     */
    @RequestMapping(value = "/addUser",method = RequestMethod.POST)
    public String addUser(@RequestBody JSONObject jsonObject){
        try {
            HashMap hashMapTemp = userService.addUser(jsonObject);
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

    @RequestMapping(value = "/queryUserTocken",method = RequestMethod.POST)
    public String queryUserTocken(@RequestBody JSONObject jsonObject){
        try {
            HashMap hashMapTemp = userService.queryTTLTocken(jsonObject);
            if ((boolean)hashMapTemp.get("flag")) {
                return resultJsonClass("200",(String)hashMapTemp.get("msg"),hashMapTemp.get("ttlUser"));
            }else {
                return resultJsonClass("500",(String)hashMapTemp.get("msg"),"");
            }
        }catch (Exception e){
            e.printStackTrace();
            return resultJsonClass("500","异常错误","");
        }
    }

    /**
     * 用户审核接口
     */
    @RequestMapping(value = "/auditUser",produces = "application/json;charset=UTF-8",method = RequestMethod.POST)
    public String auditUser(@RequestBody JSONObject jsonObject){
        //用户审核，同意的话修改用户状态为审核成功，不同意的话修改用户状态为审核失败
        //先查ID 通过用户信息查用户ID
        try{
            HashMap hashMapTemp=userService.auditUser(jsonObject);
            if ((boolean)hashMapTemp.get("flag")) {
                return resultJsonClass("200","成功","");
            }else{
                return resultJsonClass("500",(String)hashMapTemp.get("msg"),"");
            }
        }catch (Exception e){
            e.printStackTrace();
            return resultJsonClass("500","异常错误","");
        }
    }
//    /**
//     * 添加通通锁用户---改成认证用户的时候同步添加
//     */
//    @RequestMapping(value = "/addTTLUser",produces = "application/json;charset=UTF-8",method = RequestMethod.POST)
//    public String addTTLUser(@RequestBody JSONObject jsonObject){
//        try{
//            HashMap hashMapTemp=userService.addTTLUserInfo(jsonObject);
//            if ((boolean)hashMapTemp.get("flag")) {
//                return resultJsonClass("200","成功","");
//            }else{
//                return resultJsonClass("500",(String)hashMapTemp.get("msg"),"");
//            }
//        }catch (Exception e){
//            return resultJsonClass("500","异常错误","");
//        }
//    }
    /**
     * 用户注册  只有一个唯一ID和用户状态
     */
    @RequestMapping(value = "/enrollUser",produces = "application/json;charset=UTF-8",method = RequestMethod.POST)
    public String enrollUser(@RequestBody JSONObject jsonObject){
        System.out.println(jsonObject.toJSONString());
        //GET https://api.weixin.qq.com/sns/jscode2session?appid=APPID&secret=SECRET&js_code=JSCODE&grant_type=authorization_code
        String appid ="wx18673b855ec60340";
        String secret ="b03cacf14d26779b9173da1c88bfdd1e";
        String js_code =(String)jsonObject.get("js_code");
        String grant_type ="authorization_code";
        String URL = "https://api.weixin.qq.com/sns/jscode2session?appid="+appid+"&secret="+secret+"&js_code="+js_code+"&grant_type=authorization_code";
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(URL,String.class);
        JSONObject jsonObjectGet = JSONObject.parseObject(responseEntity.getBody());
        System.out.println("jsonObjectGet="+jsonObjectGet.toString());
        String openId = String.valueOf(jsonObjectGet.get("openid"));
        System.out.println("enrollUser openId="+openId);
        try {
            HashMap hashMapTemp=userService.enrollUser(openId);
            if ((boolean)hashMapTemp.get("flag")) {
                System.out.println("1111111111");
                return resultJsonClass("200","成功",openId);
            }else{
                return resultJsonClass("500",(String)hashMapTemp.get("msg"),openId);
            }
        }catch (Exception e){
            e.printStackTrace();
            return resultJsonClass("500","异常错误","");
        }
    }
    /**
     * 查学院号
     */
    @RequestMapping(value = "/getCollegeName",produces = "application/json;charset=UTF-8")
    public String getCollegeName(){
        try {
            HashMap hashMapTemp;
            hashMapTemp = userService.getCollegeName();
            if ((boolean)hashMapTemp.get("flag")){
                return resultJsonClass("200","成功",hashMapTemp.get("tempCollegeName"));
            }else {
                return resultJsonClass("500",(String)hashMapTemp.get("msg"),"");
            }
        }catch (Exception e){
            e.printStackTrace();
            return resultJsonClass("500","异常错误","");
        }
    }
    /**selectUserById
     * 查班级号@RequestParam(value = "collegeName",required = true)
     */
    @RequestMapping(value = "/getClassInfo",method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    public String getClassInfo(@RequestBody JSONObject jsonObject){
        try {
            System.out.println(jsonObject.toJSONString());
            String collegeNameValue = String.valueOf(jsonObject.get("collegeName"));
            if (collegeNameValue.equals("") || collegeNameValue==null){
                return resultJsonClass("500","请求参数为空或null","");
            }else {
                HashMap hashMapTemp=userService.getClassInfo(collegeNameValue);
                JSONObject jsonObjectTemp = new JSONObject();
                if ((boolean)hashMapTemp.get("flag")){
                    return resultJsonClass("200","成功",hashMapTemp.get("stringList"));
                }else {
                    return resultJsonClass("500",(String)hashMapTemp.get("msg"),"");
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            return resultJsonClass("500","异常错误","");
        }
    }
    /**
     * 查楼栋号
     */
    @RequestMapping(value = "/getBuildingName",method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    public String getBuildingName(){
        try{
            System.out.println("getBuildingName");
            HashMap hashMapTemp = new HashMap();
            hashMapTemp = userService.getBuildingName();
            System.out.println("hashMapTemp="+hashMapTemp.toString());
            if ((boolean)hashMapTemp.get("flag")){
                return resultJsonClass("200","成功",hashMapTemp.get("stringList"));
            }else {
                return resultJsonClass("500",(String)hashMapTemp.get("msg"),"");
            }
        }catch (Exception e){
            e.printStackTrace();
            return resultJsonClass("500","异常错误","");
        }
    }
    /**
     * 查宿舍号
     */
    @RequestMapping(value = "/getDormitoryInfo",method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    public String getDormitoryInfo(@RequestBody JSONObject jsonObject){
        try {
            HashMap hashMapTemp = new HashMap();
            String buildingInfo = String.valueOf(jsonObject.get("buildingInfo"));
            if (buildingInfo.equals("") || buildingInfo==null){
                return resultJsonClass("500","请求参数为空或null","");
            }else {
                hashMapTemp=userService.getDormitoryInfo(buildingInfo);//buildingId
                if ((boolean)hashMapTemp.get("flag")){
                    return resultJsonClass("200","成功",hashMapTemp.get("stringList"));
                }else {
                    return resultJsonClass("500",(String) hashMapTemp.get("msg"),"");
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            return resultJsonClass("500","异常错误","");
        }
    }
    /**
     *查询用户状态
     */
    @RequestMapping(value = "/getUserStatus",method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    public String getUserState(@RequestBody JSONObject jsonObject){
        try{
            System.out.println(jsonObject.toJSONString());
            String openId = String.valueOf(jsonObject.get("openId"));
            if (openId.equals("") || openId==null){
                return resultJsonClass("500","请求参数为空或null","");
            }else {
                HashMap hashMapTemp = userService.getUserState(openId);
                if ((boolean) hashMapTemp.get("flag")) {
                    return resultJsonClass("200", "成功", hashMapTemp.get("stringList"));
                } else {
                    return resultJsonClass("500", (String) hashMapTemp.get("msg"), "");
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            return resultJsonClass("500","异常错误","");
        }
    }
    /**
     * 查用户信息
     */
    @RequestMapping(value = "/selectUserById",method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    public String selectUserById(@RequestBody JSONObject jsonObject){
        try{
            String openId = String.valueOf(jsonObject.get("openId"));
            if (openId.equals("") || openId==null){
                return resultJsonClass("500","请求参数为空或null","");
            }else {
                HashMap userTemp=userService.selectUserById(openId);
                if ((boolean)userTemp.get("flag")){
                    return resultJsonClass("200","成功",userTemp.get("tempUser"));
                }else {
                    return resultJsonClass("500",(String) userTemp.get("msg"),"");
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            return resultJsonClass("500","异常错误","");
        }
    }
    public String resultJsonClass(String status,String msg,Object data){
        JSONObject resultJsonTemp = new JSONObject();
        resultJsonTemp.put("status",status);
        resultJsonTemp.put("msg",msg);
        resultJsonTemp.put("data",data);
        return resultJsonTemp.toJSONString();
    }
}
