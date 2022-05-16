package com.smartschool.smartdoor.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.smartschool.smartdoor.dao.LockDao;
import com.smartschool.smartdoor.dao.UserDao;
import com.smartschool.smartdoor.entity.ClockInLog;
import com.smartschool.smartdoor.entity.ClockRuleInfo;
import com.smartschool.smartdoor.entity.Equipment;
import com.smartschool.smartdoor.entity.UnlockLog;
import com.smartschool.smartdoor.service.LockService;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import javax.xml.bind.SchemaOutputResolver;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

@Service
public class LockServiceImpl implements LockService {

    @Autowired
    private LockDao lockDao;
    @Autowired
    private UserDao userDao;
    @Resource
    private RestTemplate restTemplate;

    @Override
    public HashMap selectEquipmentIdById(String id) throws Exception{
        boolean flag=false;
        String msg="";
        ArrayList<Equipment> equipmentList = new ArrayList<>();
        ArrayList<HashMap> dataList = new ArrayList<>();
        ArrayList<String> clockTimeList = new ArrayList<>();
        HashMap hashMapTemp = new HashMap();
        Integer equipmentCount =  lockDao.queryEquipmentCount(id);
        if (equipmentCount > 0) {
            equipmentList =  lockDao.queryEquipmentIdById(id);
            int tempI =0;
            for (Equipment equipment : equipmentList){
                HashMap resultMap = new HashMap();
                //设备ID
                String lockId = equipment.getId();
                //微信小程序显示的锁名
                String lockName = equipment.getPlace();//A区A2栋101室
                HashMap lockNameListTemp = new HashMap();//按前端要求这么传值
                lockNameListTemp.put("text",lockName);
                lockNameListTemp.put("value",tempI);//自增加1
                resultMap.put("lockName",lockNameListTemp);
                resultMap.put("lockId",lockId);
                tempI=tempI+1;//自增加1
                Integer clockTimeCount = lockDao.clockTimeCount(lockId);
                if (clockTimeCount>0){
                    clockTimeList=lockDao.selectClockTime(lockId);
                    HashMap timeListTemp = new HashMap();
                    for (int i=0;i<clockTimeList.size();i++){
                        String startTime="start"+i;
                        String endTime="end"+i;
                        timeListTemp.put(startTime,clockTimeList.get(i).split("-")[0]);//时间段以 - 拆分时间
                        timeListTemp.put(endTime,clockTimeList.get(i).split("-")[1]);//时间段以 - 拆分时间
                    }
                    //寝室楼打卡时间
                    resultMap.put("clock_time",timeListTemp);
                }
                dataList.add(resultMap);
            }
            flag=true;
        }else {
            msg="该用户没有宿舍绑定设备";
        }
        hashMapTemp.put("flag",flag);
        hashMapTemp.put("msg",msg);
        hashMapTemp.put("dataList",dataList);
        return hashMapTemp;
    }
    @Override
    public HashMap addClockIn(JSONObject jsonObject) throws Exception {
        String ruleTime = "";
        HashMap hashMapTemp = new HashMap();
        boolean flag=false;
        String msg="";
        if(!checkParam(jsonObject)) {
            msg = "参数校验不通过，有参数为空或NULL";
        }else {
            String openId = (String) jsonObject.get("openId");//用户编号
            String lockId = (String) jsonObject.get("lockId");//锁编号
            System.out.println("lockId=" + lockId);
            System.out.println("openId=" + openId);
            String lockName = (String) jsonObject.get("lockName");//锁名
            String clockTime = (String) jsonObject.get("clockTime");//打卡时间
            if (jsonObject.get("ruleTime") != null && !jsonObject.get("ruleTime").equals("")) {
                ruleTime = (String) jsonObject.get("ruleTime");//规则时间段
                System.out.println("ruleTime=" + ruleTime);
            }
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            System.out.println(simpleDateFormat.format(new Date()));
            //
            //增加校验--是否已经打卡 ruleTime=10:00-11:00
            String startRuleTime = ruleTime.split("-")[0] + ":00";
            String endRuleTime = ruleTime.split("-")[1] + ":00";
            String sysDate = simpleDateFormat.format(new Date());//系统日期
            System.out.println("startRuleTime=" + startRuleTime);
            System.out.println("endRuleTime=" + endRuleTime);
            Integer clockCount = lockDao.ClockInLogByTime(openId, sysDate, startRuleTime, endRuleTime);
            System.out.println("clockCount=" + clockCount);
            if (clockCount > 0) {
                msg = "这个时间段已打过卡，勿重复打卡";
            } else {
                ClockInLog clockInLog = new ClockInLog();
                clockInLog.setId(openId);
                clockInLog.setPlace(lockName);
                clockInLog.setClockDate(simpleDateFormat.format(new Date()));
                clockInLog.setTime(clockTime);
                clockInLog.setRuleTime(ruleTime);
                clockInLog.setEquipmentId(lockId);
                Integer addResult = lockDao.addClockInLog(clockInLog);
                if (addResult.equals(1)) {
                    msg = "打卡成功";
                    flag = true;
                } else {
                    msg = "增加打卡记录失败，请稍后再试";
                }
            }
        }
        hashMapTemp.put("flag",flag);
        hashMapTemp.put("msg",msg);
        return hashMapTemp;
    }

    @Override
    public HashMap queryClockIn(String openId) throws Exception {
        boolean flag=false;
        String msg="";
        HashMap hashMapTemp = new HashMap();
        if (openId.equals("") || openId==null){
            msg="参数校验不通过，有参数为空或NULL";
        }else {
            Integer clockInLogCount =  lockDao.clockInLogCount(openId);
            if (clockInLogCount>0){
                ClockInLog clockInLog =lockDao.queryClockInLog(openId);
                flag=true;
            }else {
                msg="该用户无打卡记录";
            }
        }
        hashMapTemp.put("flag",flag);
        hashMapTemp.put("msg",msg);
        hashMapTemp.put("tempUser","user");
        return hashMapTemp;
    }

    @Override
    public HashMap addUnlockLog(JSONObject jsonObject) throws Exception {
        boolean flag = false;
        String msg = "";
        HashMap hashMapTemp = new HashMap();
        if(!checkParam(jsonObject)) {
            msg = "参数校验不通过，有参数为空或NULL";
        }else {
            String openId = (String) jsonObject.get("openId");//用户编号
            String lockId = (String) jsonObject.get("lockId");//锁编号
            String lockName = (String) jsonObject.get("lockName");//锁名
            String unlockTime = (String) jsonObject.get("unlockTime");//打卡时间
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");//系统日期
            String sysDate = simpleDateFormat.format(new Date());//系统日期
            UnlockLog unlockLog = new UnlockLog();
            unlockLog.setId(openId);
            unlockLog.setEquipment_id(lockId);
            unlockLog.setUnlock_date(sysDate);
            unlockLog.setUnlock_time(unlockTime);
            unlockLog.setEquipment_place(lockName);
            Integer addResult = lockDao.addUnlockLog(unlockLog);
            if (addResult.equals(1)) {
                msg = "开锁成功";
                flag = true;
            } else {
                msg = "增加开锁记录失败，请稍后再试";
            }
        }
        hashMapTemp.put("flag",flag);
        hashMapTemp.put("msg",msg);
        return hashMapTemp;
    }

    /**
     * 获取锁的电量
     * @return
     */
    public HashMap getDLOfLock(String lockId) throws Exception{
        //上传参数
        HashMap hashMapTemp = new HashMap();
        boolean flag=false;
        String msg="";
        String client_id = "9a0a15e6d85f442ab138caa038bae71c";
        String accessToken = getTokenOfLock();
        Long date = System.currentTimeMillis();
        String URL = "https://api.ttlock.com/v3/lock/detail?clientId=" + client_id + "&accessToken=" + accessToken + "&lockId=" + lockId + "&date=" + date;
        System.out.println("URL=" + URL);
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(URL, String.class);
        JSONObject jsonObjectGet = JSONObject.parseObject(responseEntity.getBody());
        //锁电量
        Integer electricQuantity= (Integer) jsonObjectGet.get("electricQuantity");
        msg = "开锁成功";
        flag = true;
        hashMapTemp.put("flag",flag);
        hashMapTemp.put("msg",msg);
        hashMapTemp.put("electricQuantity",electricQuantity);
        return hashMapTemp;
    }

    @Override
    public HashMap addClockRuleInfo(JSONObject jsonObject) throws Exception{
        boolean flag = false;
        String msg = "";
        HashMap hashMapTemp = new HashMap();
        if(!checkParam(jsonObject)){
            msg = "参数校验不通过，有参数为空或NULL";
            hashMapTemp.put("flag",flag);
            hashMapTemp.put("msg",msg);
            return hashMapTemp;
        }else {
            String lockId = (String) jsonObject.get("lockId");
            String lockName = (String) jsonObject.get("lockName");//锁名
            String startDate = (String) jsonObject.get("startDate");//开始日期
            String endDate = (String) jsonObject.get("endDate");//终止日期
            String startTime = (String) jsonObject.get("startTime");//开始时间
            String endTime = (String) jsonObject.get("endTime");//终止时间
            System.out.println("startDate="+startDate+"--endDate="+endDate);
            //判断这个锁有没有设置重叠的时间段打卡规则
            if (startDate.equals(endDate)){
                    hashMapTemp = addOneTimeClockRule(flag,msg,hashMapTemp,lockName,lockId,startDate,startTime,endTime);
            }else {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date tmpStart = sdf.parse(startDate);//起始日期
                Date tmpEnd = sdf.parse(endDate);//结束日期
                Calendar dd = Calendar.getInstance();
                dd.setTime(tmpStart);
                while (tmpStart.getTime() <= tmpEnd.getTime()) {
                    // 天数加上1
                    dd.add(Calendar.DAY_OF_MONTH, 1);
                    tmpStart = dd.getTime();
                    System.out.println("addClockRuleInfo startDate="+startDate);
                    hashMapTemp = addOneTimeClockRule(flag,msg,hashMapTemp,lockName,lockId,startDate,startTime,endTime);
                    if (!(boolean)hashMapTemp.get("ifcontinue")){
                        msg="设置该时间段打卡规则失败";
                        break;
                    }else {
                        startDate = sdf.format(dd.getTime());
                        System.out.println("tmp.toString()="+startDate);
                    }
                }
            }
            return hashMapTemp;
        }
    }
    //设置打卡规则
    private HashMap addOneTimeClockRule(boolean flag,String msg,HashMap hashMapTemp,String lockName,String lockId,String startDate,String startTime,String endTime) throws Exception{
        ArrayList<String> tempStringList = new ArrayList<>();
        boolean ifcontinue=true;
        DateFormat df = new SimpleDateFormat("HH:mm");//创建日期转换对象HH:mm:ss为时分秒，年月日为yyyy-MM-dd
        Date startTime1 = null;
        Date endTime1 = null;
        Integer tempClockNum = lockDao.clockTimeCountByIDAndDate(lockId,startDate);
        if(tempClockNum>0){
            System.out.println("库中有记录，判断是否冲突");
            tempStringList = lockDao.clockTimeByIDAndDate(lockId,startDate);//数据库里存的时间段
            for (String tempString : tempStringList){
                try {
                    System.out.println("tempString="+tempString);
                    String st = tempString.split("-")[0];
                    String et = tempString.split("-")[1];
                    startTime1 = df.parse(startTime);//起始时间
                    endTime1 = df.parse(endTime);//终止时间
                    System.out.println("st="+st+"--et="+et);
                    System.out.println("startTime1="+startTime1.getTime()+"--endTime1="+endTime1.getTime());
                    if ((endTime.compareTo(st)==-1)||(startTime.compareTo(et)==1)){
                        continue;
                    }else {
                        msg="该时间段与已存在的打卡规则时间冲突";
                        ifcontinue=false;
                        break;
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            //无时间冲突则增加规则
            if (ifcontinue){
                System.out.println("ifcontinue is true");
                hashMapTemp = addClockRuleMethod(flag,msg,hashMapTemp,lockName,lockId,startDate,startTime,endTime);
            }else {
                flag=false;
                hashMapTemp.put("flag",flag);
                hashMapTemp.put("msg",msg);
            }
        }else {
            System.out.println("库中没有记录，直接增加");
            hashMapTemp = addClockRuleMethod(flag,msg,hashMapTemp,lockName,lockId,startDate,startTime,endTime);
        }
        hashMapTemp.put("ifcontinue",ifcontinue);
        return hashMapTemp;
    }
    //增加打卡规则
    private HashMap addClockRuleMethod(boolean flag,String msg,HashMap hashMapTemp,String lockName,String lockId,String startDate,String startTime,String endTime) throws Exception{
        ClockRuleInfo clockRuleInfo = new ClockRuleInfo();
        clockRuleInfo.setClockId(lockName);
        clockRuleInfo.setEquipmentId(lockId);
        clockRuleInfo.setClockDate(startDate);
        System.out.println("addClockRuleMethod  startDate="+startDate);
        System.out.println("startTime+endTime="+startTime+"-"+endTime);
        clockRuleInfo.setClockTime(startTime+"-"+endTime);//时分没有秒
        Integer addresult = lockDao.addClockTimeRule(clockRuleInfo);
        if (addresult.equals(1)){
            flag=true;
            msg="增加打卡规则成功";
        }else {
            msg="增加打卡规则失败";
        }
        hashMapTemp.put("flag",flag);
        hashMapTemp.put("msg",msg);
        return hashMapTemp;
    }

    @Override
    public HashMap addEquipment(JSONObject jsonObject) throws Exception {
        //设备绑定
        HashMap hashMapTemp = new HashMap();
        boolean flag=false;
        String msg="";
        if(!checkParam(jsonObject)){
            msg = "参数校验不通过，有参数为空或NULL";
            hashMapTemp.put("flag",flag);
            hashMapTemp.put("msg",msg);
            return hashMapTemp;
        }else {
            String openId =  String.valueOf(jsonObject.get("openId"));
            String lockId = String.valueOf(jsonObject.get("lockId"));
            String lockName =  String.valueOf(jsonObject.get("lockName"));//锁名
            Equipment equipment =new Equipment();
            equipment.setId(lockId);
            equipment.setPlace(lockName);
            equipment.setName(lockName);
            equipment.setUserId(openId);
            equipment.setStatus("待审核");
            Integer CountResult = lockDao.EquipmentCountByIDopenId(openId,lockId);
            System.out.println("CountResult="+CountResult);
            if (CountResult>0){
                //更改设备名称--不存在则修改信息
                Integer IfChangeNameFlag = lockDao.EquipmentByIDopenIdName(openId,lockId,lockName);
                System.out.println("IfChangeNameFlag="+IfChangeNameFlag);
                if (IfChangeNameFlag.equals(0)){
                    //修改名称
                    System.out.println("openId="+openId+"-->lockId="+lockId+"-->lockName="+lockName);
                    Integer updateEquipment = lockDao.updateEquipment(equipment);
                    System.out.println("updateEquipment="+updateEquipment);
                    if (updateEquipment.equals(1)){
                        msg = "设备绑定成功";
                        flag = true;
                    }else {
                        msg = "修改已存在的设备名称失败";
                    }
                }else {
                    msg = "该设备已存在";
                    flag = true;
                }
            }else{
                Integer addResult = lockDao.addEquipment(equipment);
                if (addResult.equals(1)) {
                    msg = "设备绑定成功";
                    flag = true;
                } else {
                    msg = "设备绑定失败，请稍后再试";
                }
            }
            hashMapTemp.put("flag",flag);
            hashMapTemp.put("msg",msg);
            return hashMapTemp;
        }
    }

    public boolean checkParam(JSONObject jsonObject){
        boolean flag=true;
        for (String strTemp : jsonObject.keySet()){
            if (jsonObject.get(strTemp)==null && jsonObject.get(strTemp).equals("")){
                flag=false;
                break;
            }else {
                continue;
            }
        }
        return flag;
    }

    public String getTokenOfLock(){
        String url="https://api.ttlock.com/oauth2/token";
        LinkedMultiValueMap<String,String> request = new LinkedMultiValueMap<>();
        //上传参数
        request.set("client_id","9a0a15e6d85f442ab138caa038bae71c");
        request.set("client_secret","07614e70ea492e79351ac38c1c067ebc");
        request.set("username","13177850718");
        request.set("password","07b33e4e804901c37fb9bc43265ad9bd");
        String result = restTemplate.postForObject(url,request,String.class);
        JSONObject jsonObjectResult = JSONObject.parseObject(result);
        String access_token = (String)jsonObjectResult.get("access_token");//访问令牌
        return access_token;
    }
    private Boolean checkParamJSONObject(JSONObject jsonObject){
        Boolean flag =true;
        for (String tempStr : jsonObject.keySet()){
            if (jsonObject.get(tempStr).equals("") || jsonObject.get(tempStr)==null){
                flag =false;
                break;
            }else {
                continue;
            }
        }
        return flag;
    }
}
