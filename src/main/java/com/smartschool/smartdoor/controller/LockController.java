package com.smartschool.smartdoor.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.smartschool.smartdoor.entity.Dormitory;
import com.smartschool.smartdoor.service.LockService;
import com.smartschool.smartdoor.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.*;

@RestController
@RequestMapping(value = "/lock")
public class LockController {
    //掌管锁的逻辑处理
    @Resource
    private RestTemplate restTemplate;
    @Resource
    private LockService lockService;
//    @Resource
//    private UserService userService;

    /**
     * 调用通通锁获取令牌
     */
    @RequestMapping(value = "/getTokenOfLock",method = RequestMethod.POST)
    public String getTokenOfLock(){
        String access_token ="";
        try {
            String url="https://api.ttlock.com/oauth2/token";
            LinkedMultiValueMap<String,String> request = new LinkedMultiValueMap<>();
            //上传参数
            request.set("client_id","9a0a15e6d85f442ab138caa038bae71c");
            request.set("client_secret","07614e70ea492e79351ac38c1c067ebc");
            request.set("username","13177850718");
            request.set("password","07b33e4e804901c37fb9bc43265ad9bd");
            String result = restTemplate.postForObject(url,request,String.class);
            JSONObject jsonObjectResult = JSONObject.parseObject(result);
            access_token = (String)jsonObjectResult.get("access_token");//访问令牌
            return access_token;
        }catch (Exception e){
            e.printStackTrace();
            return access_token;
        }
    }
    /**
     * 调用通通锁刷新令牌
     */
//    @RequestMapping(value = "/flushTokenOfLock",method = RequestMethod.POST)
//    public String flushTokenOfLock(@RequestBody String refresh_token){
//        String url="https://api.ttlock.com/oauth2/token";
//        LinkedMultiValueMap<String,String> request = new LinkedMultiValueMap<>();
//        //上传参数
//        request.set("client_id","9a0a15e6d85f442ab138caa038bae71c");
//        request.set("client_secret","07614e70ea492e79351ac38c1c067ebc");
//        request.set("grant_type","refresh_token");
//        request.set("refresh_token",refresh_token);
//        String result = restTemplate.postForObject(url,request,String.class);
//        System.out.println("result="+result);
//        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url,request,String.class);
//        System.out.println("responseEntity.getBody()="+responseEntity.getBody());
//        return "";
//    }
    /**
     * 增加蓝牙开锁记录
     */
    @RequestMapping(value = "/addUnlockLog",method = RequestMethod.POST)
    public String addUnlockLog(@RequestBody JSONObject jsonObject){
        try {
            System.out.println("addUnlockLog jsonObject="+jsonObject.toString());
            HashMap hashMapTemp = lockService.addUnlockLog(jsonObject);
            if ((boolean)hashMapTemp.get("flag")) {
                return resultJsonClass("200","成功","");
            }else {
                return resultJsonClass("500",(String)hashMapTemp.get("msg"),"");
            }
        }catch (Exception e){
            e.printStackTrace();
            return resultJsonClass("500","异常错误","");
        }
    }

    /**
     * 获取系统时间
     */
    @RequestMapping(value = "/getSysTime",method = RequestMethod.POST)
    public String getSysTime() {
        try{
            Long systime =System.currentTimeMillis();
            System.out.println("systime="+systime);
            return resultJsonClass("200","成功",systime);
        }catch (Exception e){
            e.printStackTrace();
            return resultJsonClass("500","异常错误","");
        }
    }
    /**
     *获取锁信息
     */
    @RequestMapping(value = "/getLockDetail",method = RequestMethod.POST)
    public String getLockDetail(@RequestBody JSONObject jsonObject) {
        try{
            System.out.println("getLockDetail");
            String openId = (String) jsonObject.get("openId");
            HashMap hashMapTemp = lockService.selectEquipmentIdById(openId);
            if ((boolean)hashMapTemp.get("flag")) {
                return resultJsonClass("200","成功",hashMapTemp.get("dataList"));
            }else {
                return resultJsonClass("500",(String) hashMapTemp.get("msg"),"");
            }
        }catch (Exception e){
            e.printStackTrace();
            return resultJsonClass("500","异常错误","");
        }
    }
    /**
     *获取锁的详细信息
     */
    @RequestMapping(value = "/getInfoOfLock",method = RequestMethod.POST)
    public String getInfoOfLock(@RequestBody JSONObject jsonObject) {
        try{
            System.out.println("getLockDetail");
            String lockId = (String) jsonObject.get("lockId");
            HashMap hashMapTemp = lockService.getDLOfLock(lockId);
            if ((boolean)hashMapTemp.get("flag")) {
                return resultJsonClass("200","成功",hashMapTemp.get("electricQuantity"));
            }else {
                return resultJsonClass("500",(String) hashMapTemp.get("msg"),"");
            }
        }catch (Exception e){
            e.printStackTrace();
            return resultJsonClass("500","异常错误","");
        }
    }
    /**
     *增加打卡记录
     */
    @RequestMapping(value = "/addClockInLog",method = RequestMethod.POST)
    public String addClockInLog(@RequestBody JSONObject jsonObject) {
        try {
            HashMap hashMapTemp = lockService.addClockIn(jsonObject);
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
     *查询打卡记录
     */
    @RequestMapping(value = "/queryClockInLog",method = RequestMethod.POST)
    public String queryClockInLog(@RequestBody JSONObject jsonObject) {
        try {
            String openId = (String) jsonObject.get("openId");//用户编号
            HashMap hashMapTemp = lockService.queryClockIn(openId);
            if ((boolean)hashMapTemp.get("flag")) {
                return resultJsonClass("200","成功","");
            }else {
                return resultJsonClass("500",(String)hashMapTemp.get("msg"),"");
            }
        }catch (Exception e){
            e.printStackTrace();
            return resultJsonClass("500","异常错误","");
        }
    }
    /**
     *设置打卡时间规则
     */
    @RequestMapping(value = "/addClockRule",method = RequestMethod.POST)
    public String addClockRule(@RequestBody JSONObject jsonObject) {
        try {
            HashMap hashMapTemp = lockService.addClockRuleInfo(jsonObject);
            if ((boolean)hashMapTemp.get("flag")) {
                return resultJsonClass("200","成功","");
            }else {
                return resultJsonClass("500",(String)hashMapTemp.get("msg"),"");
            }
        }catch (Exception e){
            e.printStackTrace();
            return resultJsonClass("500","异常错误","");
        }
    }
//    /**
//     * 增加设备-设备绑定--转到 changeLockList
//     */
//    @RequestMapping(value = "/addEquipment",method = RequestMethod.POST)
//    public String addEquipment(@RequestBody JSONObject jsonObject){
//        try {
//            HashMap hashMapTemp = lockService.addEquipment(jsonObject);
//            if ((boolean)hashMapTemp.get("flag")) {
//                return resultJsonClass("200",(String)hashMapTemp.get("msg"),"");
//            }else {
//                return resultJsonClass("500",(String)hashMapTemp.get("msg"),"");
//            }
//        }catch (Exception e){
//            e.printStackTrace();
//            return resultJsonClass("500","异常错误","");
//        }
//    }
    /**
     * 将Object强转成List
     */
    public static <T> List<T> changeList(Object object,Class<T> clazz){
        try{
            List<T> result = new ArrayList<>();
            if (object instanceof List<?>){
                for (Object o : (List<?>) object){
                    String string = JSONObject.toJSONString(o);
                    T t = JSONObject.parseObject(string ,clazz);
                    result.add(t);
                }
                return  result;
            }
            return null;
        }catch (Exception e){
            e.printStackTrace();
        }
        return  null;
    }
    /**
     *更改锁列表格式,增加用户锁列表
     */
    @RequestMapping(value = "changeLockList",method = RequestMethod.POST)
    public String changeLockList(@RequestBody JSONObject jsonObject){
        boolean flag =true;
        ArrayList<HashMap> tempArrayList = new ArrayList();
        ArrayList lockInfo = new ArrayList();
        try{
            JSONObject requestJson = new JSONObject();
            System.out.println("changeLockList jsonObject="+jsonObject.toString());
            JSONObject lockList =JSONObject.parseObject(JSON.toJSONString(jsonObject.get("lockList")));
            requestJson.put("openId",lockList.get("openId"));
            for (HashMap hashMapTemp : changeList(lockList.get("lockInfo"),HashMap.class)){
                requestJson.put("lockName",hashMapTemp.get("lockName"));
                requestJson.put("lockId",hashMapTemp.get("lockId"));
                lockInfo.add(hashMapTemp.get("lockName"));
                HashMap resultHashMap = lockService.addEquipment(requestJson);
                flag = (Boolean) resultHashMap.get("flag");
                if (!flag){
                    break;
                }
            }
            if (flag){
                //获取锁列表
//                lockList = (ArrayList) jsonObject.get("lockList");//用户编号
                int tempNum=0;
                for (Object tempName : lockInfo){
                    HashMap lockNameListTemp = new HashMap();//按前端要求这么传值
                    lockNameListTemp.put("text",tempName);
                    lockNameListTemp.put("value",tempNum);//自增加1
                    tempArrayList.add(lockNameListTemp);
                    tempNum=tempNum+1;
                }
                return resultJsonClass("200","成功",tempArrayList);
            }else {
                System.out.println("222222");
                return resultJsonClass("500","锁列表入库失败",tempArrayList);
            }
        }catch (Exception e){
            e.printStackTrace();
            return resultJsonClass("500","更改锁列表格式失败",tempArrayList);
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
