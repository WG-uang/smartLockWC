package com.smartschool.smartdoor.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.smartschool.smartdoor.dao.WorkDao;
import com.smartschool.smartdoor.entity.HolidayRawInfo;
import com.smartschool.smartdoor.service.WorkService;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

@Service
public class WorkServiceImpl implements WorkService{
    @Autowired
    private WorkDao workDao;

    @Override
    public HashMap selectClockRuleByBuildingId(String id) throws Exception{
        HashMap hashMapTemp = new HashMap();
        return hashMapTemp;
    }

    @Override
    public HashMap selectClockRuleByClassId(String id) throws Exception {
        HashMap hashMapTemp = new HashMap();
        return hashMapTemp;
    }

    @Override
    //插入节假日数据
    public HashMap addDataDate(JSONObject jsonObject) throws Exception {
        boolean flag=false;
        String msg="";
        HashMap hashMapTemp = new HashMap();
        int year = Integer.valueOf(String.valueOf(jsonObject.get("year")));//年份
        int m=1;//月份计数
        while (m<13)
        {
            String month=String.valueOf(m);
            if (month.length()<2){
                month="0"+month;
            }
            Calendar cal=Calendar.getInstance();//获得当前日期对象
            cal.clear();//清除信息
            cal.set(Calendar.YEAR,year);
            cal.set(Calendar.MONTH,m-1);//1月从0开始
            int count=cal.getActualMaximum(Calendar.DAY_OF_MONTH) ;//每月天数
            int week=cal.get(Calendar.DAY_OF_WEEK);
//            System.out.println("count="+count+"_week="+week);
//            System.out.printf("\t\t\t%d年%d月\n\n",year,m);
//            System.out.print("日\t一\t二\t三\t四\t五\t六\n");
            int resultNum=0;//用于记录插入时错误数据量
            for (int dayNum=0;dayNum<count;dayNum++){
                String dayNum1= String.valueOf(dayNum+1);
                if (dayNum1.length()<2){
                    dayNum1="0"+dayNum1;
                }
                String date=year+"-"+month+"-"+dayNum1;//日期
                int weekNameStart=week-1;//月初第一天属于周几
                String weekName ="";
                if((dayNum+weekNameStart)%7==0){
                    weekName="周日";
                }else if((dayNum+weekNameStart)%7==1){
                    weekName="周一";
                }else if((dayNum+weekNameStart)%7==2){
                    weekName="周二";
                }else if((dayNum+weekNameStart)%7==3){
                    weekName="周三";
                }else if((dayNum+weekNameStart)%7==4){
                    weekName="周四";
                }else if((dayNum+weekNameStart)%7==5){
                    weekName="周五";
                }else if((dayNum+weekNameStart)%7==6){
                    weekName="周六";
                }
                HolidayRawInfo holidayRawInfo = new HolidayRawInfo();
                if(weekName.equals("周六")||weekName.equals("周日")){
                    holidayRawInfo.setHoliday("true");
                }else{
                    holidayRawInfo.setHoliday("false");
                }
                holidayRawInfo.setDate(date);
                holidayRawInfo.setWeek(weekName);
                Integer result = workDao.addHoliday(holidayRawInfo);
                if (!result.equals(1)) {
                    resultNum=resultNum+1;
                }
//                System.out.println("data="+date+"--weekName="+weekName);
            }
            m++;
            if(resultNum>0){
                msg="维护节假日失败";
            }else {
                flag=true;
            }
        }
        hashMapTemp.put("flag",flag);
        hashMapTemp.put("msg",msg);
        return hashMapTemp;
    }

    @Override
    //插入节假日数据
    public HashMap addHoliday(JSONObject jsonObject)  throws Exception{
        boolean flag=false;
        String msg="";
        HashMap hashMapTemp = new HashMap();
        //String syncAddress = "http://timor.tech/api/holiday/year";
        String url = (String) jsonObject.get("url");
        OkHttpClient client = new OkHttpClient();
        Response response;
        //解密数据
        String rsa = null;
        Request request = new Request.Builder().url(url).get()
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .build();
        try {
            response = client.newCall(request).execute();
            rsa = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Map map = JSONObject.parseObject(rsa, Map.class);
        if (map != null) {
            Integer code = (Integer) map.get("code");
            if (code == 0) {
                JSONObject holidayJson = (JSONObject) map.get("holiday");
                int updateResultNum=0;
                int addResultNum=0;
                for (String holidayJsonKeyset : holidayJson.keySet()) {
                    HolidayRawInfo holidayRawInfo = new HolidayRawInfo();
                    holidayRawInfo.setHoliday(String.valueOf(JSONObject.parseObject(String.valueOf(holidayJson.get(holidayJsonKeyset))).get("holiday")));
                    holidayRawInfo.setDate(String.valueOf(JSONObject.parseObject(String.valueOf(holidayJson.get(holidayJsonKeyset))).get("date")));
                    holidayRawInfo.setName(String.valueOf(JSONObject.parseObject(String.valueOf(holidayJson.get(holidayJsonKeyset))).get("name")));
                    Integer queryResult = workDao.queryHoliday(String.valueOf(JSONObject.parseObject(String.valueOf(holidayJson.get(holidayJsonKeyset))).get("date")));
                    if(queryResult>0){
                        Integer updateResult = workDao.updateHoliday(holidayRawInfo);
                        if (!updateResult.equals(1)) {
                            updateResultNum=updateResultNum+1;
                        }
                    }else {
                        Integer addResult = workDao.addHoliday(holidayRawInfo);
                        if (!addResult.equals(1)) {
                            addResultNum=addResultNum+1;
                        }
                    }
                }
                if(updateResultNum>0||addResultNum>0){
                    msg="维护节假日失败";
                }else {
                    flag=true;
                }
            }
        }
        hashMapTemp.put("flag",flag);
        hashMapTemp.put("msg",msg);
        return hashMapTemp;
    }
}
