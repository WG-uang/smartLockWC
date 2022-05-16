package com.smartschool.smartdoor.dao;

import com.smartschool.smartdoor.entity.ClockInLog;
import com.smartschool.smartdoor.entity.ClockRule;
import com.smartschool.smartdoor.entity.HolidayRawInfo;
import com.smartschool.smartdoor.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;

@Mapper
@Repository
public interface WorkDao {
    /**
     * 查打卡规则通过楼栋号
     */
    @Select("SELECT * FROM clock_rule_info WHERE clock_id IN (SELECT building_id FROM USER WHERE id=#{id})")
    ArrayList<ClockRule> selectClockRuleByBuildingId(String id);

    /**
     * 查打卡规则通过班级号
     */
    @Select("SELECT * FROM clock_rule_info WHERE clock_id IN (SELECT class_id FROM USER WHERE id=#{id})")
    ArrayList<ClockRule> selectClockRuleByClassId(String id);

    /**
     * 插入节假日表
     * @param holidayRawInfo
     * @return
     */
    @Insert("INSERT INTO holiday(holiday,name,date,week) VALUES (#{holiday},#{name},#{date},#{week})")
    Integer addHoliday(HolidayRawInfo holidayRawInfo);
    /**
     * 修改节假日表
     * @param holidayRawInfo
     * @return
     */
    @Update("update holiday set holiday=#{holiday},name=#{name} where date=#{date}")
    Integer updateHoliday(HolidayRawInfo holidayRawInfo);
    /**
     * 查询节假日表
     * @param date
     * @return
     */
    @Select("select count(1) from holiday where date=#{date}")
    Integer queryHoliday(String date);
}
