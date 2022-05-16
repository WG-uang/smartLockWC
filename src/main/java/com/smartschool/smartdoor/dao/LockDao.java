package com.smartschool.smartdoor.dao;

import com.smartschool.smartdoor.entity.*;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Mapper
@Repository
public interface LockDao {
    /**
     * 通过openID 查对应的宿舍设备号
     */
    @Select("SELECT * FROM dormitory_info WHERE id IN (SELECT dormitory_id FROM USER WHERE id=#{id})")
    ArrayList<Dormitory> selectDormitoryIdById(String id);
    @Select("SELECT count(1) FROM dormitory_info WHERE id IN (SELECT dormitory_id FROM USER WHERE id=#{id})")
    Integer selectDormitoryCount(String id);
    /**
     * 通过openID 查用户名下设备信息
     */
    @Select("SELECT * FROM equipment WHERE userId=#{id}")
    ArrayList<Equipment> queryEquipmentIdById(String id);
    @Select("SELECT count(1) FROM equipment WHERE userId=#{id}")
    Integer queryEquipmentCount(String id);
    @Select("SELECT count(1) FROM equipment WHERE userId=#{openId} and id=#{lockId}")
    Integer EquipmentCountByIDopenId(@Param("openId") String openId,@Param("lockId") String lockId);
    @Select("SELECT count(1) FROM equipment WHERE userId=#{openId} and id=#{lockId} and name=#{name}")
    Integer EquipmentByIDopenIdName(@Param("openId") String openId,@Param("lockId") String lockId,@Param("name") String name);
    @Update("UPDATE equipment SET name=#{name},place=#{place},status=#{status} WHERE id=#{id} AND userId=#{userId}")
    Integer updateEquipment(Equipment equipment);
    /**
     * 查打卡时间
     */
    @Select("SELECT DISTINCT clock_time FROM clock_rule_info WHERE equipment_id=#{equipmentId}")
    ArrayList<String> selectClockTime(String equipmentId);
    @Select("SELECT count(1) FROM clock_rule_info WHERE equipment_id=#{equipmentId}")
    Integer clockTimeCount(String equipmentId);
    @Select("SELECT count(1) FROM clock_rule_info WHERE equipment_id=#{equipmentId} and clock_date=#{clockDate}")
    Integer clockTimeCountByIDAndDate(@Param("equipmentId") String equipmentId,@Param("clockDate") String clockDate);
    @Select("SELECT DISTINCT clock_time FROM clock_rule_info WHERE equipment_id=#{equipmentId} and clock_date=#{clockDate}")
    ArrayList<String> clockTimeByIDAndDate(@Param("equipmentId") String equipmentId,@Param("clockDate") String clockDate);

    /**
     *插入设备绑定记录
     */
    @Insert("INSERT INTO equipment(id,name,place,password,userId,status) VALUES (#{id},#{name},#{place},#{password},#{userId},#{status})")
    Integer addEquipment(Equipment equipment);
    /**
     * 增加打卡规则
     */
    @Insert("INSERT INTO clock_rule_info(clock_id,equipment_id,equipment_name,clock_date,clock_time,TYPE) VALUES(#{clockId},#{equipmentId},#{equipmentName},#{clockDate},#{clockTime},#{type})")
    Integer addClockTimeRule(ClockRuleInfo clockRuleInfo);
    /**
     * 查打卡时间
     */
    @Select("SELECT * FROM clock_rule_info WHERE equipment_id=#{equipmentId}")
    ArrayList<ClockRuleInfo> selectClockRule(String equipmentId);
    /**
     *查设备编号  lockId
     */
    @Select("SELECT DISTINCT equipment_id FROM dormitory_info where building_area=#{building_area} AND building_id=#{building_id} AND id=#{id}")
    String selectEquipmentId(@Param("building_area") String building_area,@Param("building_id") String building_id,@Param("id") String id);
    @Select("SELECT count(1) FROM dormitory_info where building_area=#{building_area} AND building_id=#{building_id} AND id=#{id}")
    Integer equipmentIdCount(@Param("building_area") String building_area,@Param("building_id") String building_id,@Param("id") String id);
    /**
     *插入打卡流水表
     */
    @Insert("INSERT INTO clock_in_log(id,equipmentId,clockDate,TIME,place,ruleTime) VALUES (#{id},#{equipmentId},#{clockDate},#{time},#{place},#{ruleTime})")
    Integer addClockInLog(ClockInLog clockInLog);
    /**
     *查询打卡流水表
     */
    @Select("SELECT * FROM clock_in_log WHERE id=#{id}")
    ClockInLog queryClockInLog(String  id);
    /**
     *查询某一时间段内打卡流水表笔数
     */
    @Select("SELECT count(1) FROM clock_in_log WHERE id=#{id} and clockDate=#{clockDate} and time between #{startRuleTime} and #{endRuleTime}")
    Integer ClockInLogByTime(@Param("id") String id,@Param("clockDate") String clockDate,@Param("startRuleTime") String startRuleTime,@Param("endRuleTime") String endRuleTime);
    /**
     *查询某用户打卡流水表笔数
     */
    @Select("SELECT count(1) FROM clock_in_log WHERE id=#{id}")
    Integer clockInLogCount(String  id);
    /**
     *插入开锁记录表
     */
    @Insert("INSERT INTO unlock_log(id,equipment_id,unlock_time,unlock_date,equipment_name,equipment_place,user_types,user_method) VALUES (#{id},#{equipment_id},#{unlock_time},#{unlock_date},#{equipment_name},#{equipment_place},#{user_types},#{user_method})")
    Integer addUnlockLog(UnlockLog unlockLog);

}
