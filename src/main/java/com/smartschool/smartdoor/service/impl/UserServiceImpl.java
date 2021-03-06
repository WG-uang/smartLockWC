package com.smartschool.smartdoor.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.smartschool.smartdoor.dao.UserDao;
import com.smartschool.smartdoor.entity.TTLUser;
import com.smartschool.smartdoor.entity.User;
import com.smartschool.smartdoor.service.UserService;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    public User checkParamUser(JSONObject jsonObject) throws Exception{
        User user = new User();
        for (String strTemp : jsonObject.keySet()){
            if (strTemp.equals("id")){
                user.setId((String)jsonObject.get("id"));
            }else if(strTemp.equals("name")){
                user.setName((String)jsonObject.get("name"));
            }else if(strTemp.equals("sex")){
                user.setSex((String)jsonObject.get("sex"));
            }else if(strTemp.equals("id_card")){
                user.setId_card((String)jsonObject.get("id_card"));
            }else if(strTemp.equals("workID")){
                user.setWorkID((String)jsonObject.get("workID"));
            }else if(strTemp.equals("dormitory_id")){
                user.setDormitory_id((String)jsonObject.get("dormitory_id"));
            }else if(strTemp.equals("class_id")){
                user.setClass_id((String)jsonObject.get("class_id"));
            }else if(strTemp.equals("building_id")){
                user.setBuilding_id((String)jsonObject.get("building_id"));
            }else if(strTemp.equals("telephone_number")){
                user.setTelephone_number((String)jsonObject.get("telephone_number"));
            }else if(strTemp.equals("college_id")){
                user.setCollege_id((String)jsonObject.get("college_id"));
            }
        }
        return user;
    }

    /**
     * ????????????????????????
     * @param user
     * @return
     */
    public HashMap checkParam(User user) throws Exception{
        System.out.println("checkParam");
        HashMap hashMap = new HashMap();
        if (user.getName()=="" ||user.getName()==null || user.getSex()=="" ||user.getSex()==null|| user.getTelephone_number()=="" ||user.getTelephone_number()==null){
            System.out.println("???????????????");
            hashMap.put("msg","???????????????");
            hashMap.put("checkFlag",false);
            return hashMap;
        }else if ((user.getWorkID()==null ||user.getWorkID()=="") && (user.getId_card()==null || user.getId_card()=="")){
            System.out.println("??????/???????????????????????????????????????");
            hashMap.put("msg","???????????????");
            hashMap.put("checkFlag",false);
            return hashMap;
        }else{
            hashMap.put("checkFlag",true);
            return hashMap;
        }
    }

    /**
     * ???????????????????????????
     * @return
     */
    public Boolean addTTLUser(String openId,String teleNumber,String ttlTocken) throws Exception{
        String ttlName=teleNumber;
        Integer resultNum;
        String ttlPassword=ttlName.substring(5,11);
        System.out.println("ttlName="+ttlName+"--ttlPassword="+ttlPassword);
        if(ttlTocken.equals("")||ttlTocken==null){
            ttlTocken="";
        }
        TTLUser ttlUser = new TTLUser();
        ttlUser.setId(openId);
        ttlUser.setTtlUserName(ttlName);
        ttlUser.setTtlPassword(ttlPassword);
        ttlUser.setTtlTocken(ttlTocken);
        Integer ttlUserCount = userDao.queryTTLUserCount(openId);
        if(ttlUserCount>0){
            resultNum = userDao.updateTTLUserInfo(ttlUser);
        }else {
            resultNum = userDao.addTTLUser(ttlUser);
        }
        if (resultNum.equals(1)){
            return  true;
        }else {
            return  false;
        }
    }

    @Override
    public HashMap addUser(JSONObject jsonObject) throws Exception {
        //?????????????????????????????????????????????????????????????????????????????????
        System.out.println("addUser");
        ArrayList<User> listUser = null;
        boolean flag=false;
        Integer count=0;
        String msg="";
        HashMap hashMap = new HashMap();
        ArrayList<String> arrTelNum = new ArrayList<>();
        User user = new User();
        //??????????????????
        user = checkParamUser(jsonObject);
        System.out.println("addUser="+user.toString());
        if(!(boolean)checkParam(user).get("checkFlag")){
            hashMap.put("msg",checkParam(user).get("msg"));
            hashMap.put("flag",checkParam(user).get("checkFlag"));
            return hashMap;
        }else {
            try {
                String college_id = userDao.getCollegeID(user.getCollege_id());//??????user.getCollege_id() ????????????
                user.setCollege_id(college_id);
                if (ifHaveAnd(user.getId())) {
                    //??????????????????openId?????????
                    User userTemp = userDao.selectUserById(user.getId());
                    if (userTemp != null) {
                        //????????????
                        String tempStatus = userTemp.getStatus();
                        if (tempStatus.equals("?????????")) {
                            msg = "??????????????????????????????????????????";
                        } else {
                            //?????????????????????????????????????????????
                            user.setStatus("?????????");
                            Integer checkResult = userDao.checkUser(user.getId(), user.getTelephone_number(), user.getId_card(), user.getWorkID());
                            if (checkResult > 0) {
                                msg = "???????????????/??????/??????????????????,??????????????????";
                            } else {
                                //?????????
                                Integer result = userDao.updateUserById(user);
                                if (result.equals(1)) {
                                    flag = true;
                                    Boolean booleanTemp = addTTLUser(user.getId(),user.getTelephone_number(),"");
                                    if (!booleanTemp){
                                        msg = "?????????????????????????????????";
                                    }else {
                                        msg = "??????????????????";
                                    }
                                } else {
                                    msg = "???????????????????????????";
                                }
                            }
                        }
                    } else {
                        //??????????????????????????????????????????????????????????????????
                        Integer checkResult = userDao.checkUser(user.getId(), user.getTelephone_number(), user.getId_card(), user.getWorkID());
                        if (checkResult > 0) {
                            msg = "????????????/??????/??????????????????,??????????????????";
                        } else {
                            //????????????????????????
                            user.setStatus("?????????");
                            System.out.println("userDao.addUser(user)="+user.toString());
                            Integer result = userDao.addUser(user);
                            if (result.equals(1)) {
                                flag = true;
                                msg = "??????????????????";
                                Boolean booleanTemp = addTTLUser(user.getId(),user.getTelephone_number(),"");
                                if (!booleanTemp){
                                    msg = "?????????????????????????????????";
                                }
                            } else {
                                msg = "???????????????????????????";
                            }
                        }
                    }
                } else {
                    msg = "????????????(openId)?????????";
                }
            } catch (Exception e) {
                System.out.println("---------------Exception---------------");
                e.printStackTrace();
            } finally {
                System.out.println("---------------finally---------------");
                System.out.println("---------------msg:---------------"+msg);
                hashMap.put("msg", msg);
                hashMap.put("flag", flag);
                return hashMap;
            }
        }
    }

    @Override
    public HashMap queryTTLTocken(JSONObject jsonObject) throws Exception {
        boolean flag=false;
        String msg="";
        TTLUser ttlUser = null;
        HashMap hashMap = new HashMap();
        if(checkParamJSONObject(jsonObject)){
            String openId = String.valueOf(jsonObject.get("openId"));
            ttlUser =  userDao.getTTLUserInfo(openId);
            if (ttlUser==null) {
                msg = "?????????????????????????????????";
            }else {
                flag=true;
            }
        }else {
            msg = "????????????????????????null";
        }
        hashMap.put("msg", msg);
        hashMap.put("flag", flag);
        hashMap.put("ttlUser", ttlUser);
        return hashMap;
    }


    @Override
    public boolean updateUser(User user) {
        return false;
    }

    @Override
    public HashMap selectUserById(String id) throws Exception{
        boolean flag=false;
        String msg="";
        HashMap hashMapTemp = new HashMap();
        System.out.println("selectUserById -- id="+id);
        User user =  userDao.selectUserById(id);
        if (user==null){
            msg="????????????";
        }else {
            String collegeName = userDao.getCollegeNameById(user.getCollege_id());
            user.setCollege_id(collegeName);
            flag=true;
        }
        hashMapTemp.put("flag",flag);
        hashMapTemp.put("msg",msg);
        hashMapTemp.put("tempUser",user);
        return hashMapTemp;
    }


//    @Override
//    public User selectUserByIdcard(String id_card) {
//        return null;
//    }

    @Override
    public HashMap getCollegeName() throws Exception{
        boolean flag=false;
        String msg="";
        ArrayList<String> tempCollegeName = null;
        HashMap hashMapTemp = new HashMap();
        Integer collegeCount = userDao.getCollegeCount();
        if (collegeCount>0){
            tempCollegeName = userDao.getCollegeName();
            flag=true;
        }else {
            msg="???????????????????????????";
        }
        hashMapTemp.put("flag",flag);
        hashMapTemp.put("msg",msg);
        hashMapTemp.put("tempCollegeName",tempCollegeName);
        return hashMapTemp;
    }

    @Override
    public HashMap getClassInfo(String collegeName) throws Exception{
        boolean flag=false;
        String msg="";
        ArrayList<String> stringList = new ArrayList<>();
        HashMap hashMapTemp = new HashMap();
        Integer classCount = userDao.getClassCount(collegeName);
        if (classCount>0){
            List<HashMap> hashMapList = userDao.getClassInfo(collegeName);
            for (HashMap hashmaplist : hashMapList){
                String resultTemp=hashmaplist.get("ID")+"_"+hashmaplist.get("NAME");
                stringList.add(resultTemp);
            }
            flag=true;
        }else {
            msg="????????????????????????????????????";
        }
        hashMapTemp.put("flag",flag);
        hashMapTemp.put("msg",msg);
        hashMapTemp.put("stringList",stringList);
        return hashMapTemp;
    }

    @Override
    public HashMap getBuildingName() throws Exception{
        boolean flag=false;
        String msg="";
        ArrayList<HashMap> tempBuildingInfo = new ArrayList<>();
        ArrayList<String> stringList = new ArrayList<>();
        HashMap hashMapTemp = new HashMap();
        Integer buildingCount = userDao.getBuildingCount();
        if (buildingCount>0){
            stringList = userDao.getBuildingName();
            flag=true;
        }else {
            msg="?????????????????????????????????";
        }
        hashMapTemp.put("flag",flag);
        hashMapTemp.put("msg",msg);
        hashMapTemp.put("stringList",stringList);
        return hashMapTemp;
    }

    @Override
    public HashMap getDormitoryInfo(String buildingId) throws Exception{
        boolean flag=false;
        String msg="";
        ArrayList<String> stringList = new ArrayList<>();
        HashMap hashMapTemp = new HashMap();
        Integer dormitoryCount = userDao.getDormitoryCount(buildingId);
        if (dormitoryCount>0){
            stringList = userDao.getDormitoryName(buildingId);
            flag=true;
        }else {
            msg="????????????????????????????????????";
        }
        hashMapTemp.put("flag",flag);
        hashMapTemp.put("msg",msg);
        hashMapTemp.put("stringList",stringList);
        return hashMapTemp;
    }

    @Override
    public HashMap getUserState(String openId) throws Exception{
        boolean flag=false;
        String msg="";
        HashMap strTempHash = new HashMap();
        HashMap hashMapTemp = new HashMap();
        Integer userStatusCount = userDao.getUserStatusCount(openId);
        if (userStatusCount>0){
            strTempHash = userDao.getUserStatus(openId);
            System.out.println("1111="+strTempHash.toString());
//            for (String strTemp : stringList){
//                System.out.println("strTemp="+strTemp);
//            }
            if(strTempHash.size()!=0){
                flag=true;
            }else {
                msg="?????????????????????";
            }
        }else {
            msg="???????????????????????????";
        }
        hashMapTemp.put("flag",flag);
        hashMapTemp.put("msg",msg);
        hashMapTemp.put("stringList",strTempHash);
        return hashMapTemp;
    }

    @Override
    public HashMap enrollUser(String id) throws Exception{
        boolean flag=false;
        String msg="";
        HashMap hashMapTemp = new HashMap();
        if(id.equals("") || id==null){
            msg="??????openId?????????null";
        }else {
            Integer userIDCount = userDao.selectCountByUserId(id);
            if (userIDCount>0){
                //??????????????????????????????????????????
                flag=true;
            }else {
                String status="?????????";
                Integer result = userDao.enrollUser(id,status);
                if (result.equals(1)){
                    flag=true;
                }else {
                    msg="??????openId??????";
                }
            }
        }
        hashMapTemp.put("msg",msg);
        hashMapTemp.put("flag",flag);
        return hashMapTemp;
    }

    @Override
    public HashMap auditUser(JSONObject jsonObject) throws Exception{
        boolean flag=false;
        String msg="";
        HashMap hashMapTemp = new HashMap();
        if(checkParamJSONObject(jsonObject)){
            String status =String.valueOf(jsonObject.get("status"));//????????????;
            String openId =String.valueOf(jsonObject.get("openId"));//??????ID;
            Integer updateStatusRes = userDao.updateStatusById(openId,status);
            if (updateStatusRes.equals(1)) {
                flag = true;
            } else {
                msg = "????????????????????????";
            }
        }else {
            msg = "????????????????????????null";
        }
        hashMapTemp.put("msg",msg);
        hashMapTemp.put("flag",flag);
        return hashMapTemp;
    }


    private Boolean ifHaveOr(String stringTemp){
        if(stringTemp==null || stringTemp==""){
            //??????????????????
            return false;
        }else {
            return true;
        }
    }
    private Boolean ifHaveAnd(String stringTemp){
        if(stringTemp!=null && stringTemp!=""){
            //???????????????
            return true;
        }else {
            return false;
        }
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
