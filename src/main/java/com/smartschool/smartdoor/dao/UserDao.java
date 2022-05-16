package com.smartschool.smartdoor.dao;

import com.smartschool.smartdoor.entity.TTLUser;
import com.smartschool.smartdoor.entity.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Mapper
@Repository
public interface UserDao {
    /**
     * 通过学院名查学院号
     * @param name
     * @return
     */
    @Select("SELECT DISTINCT id FROM college_info WHERE NAME=#{name}")
    String getCollegeID(String name);
    /**
     * 增加用户
     * @param user
     * @return
     */
    @Insert("insert into user(id,name,sex,id_card,workID,dormitory_id,class_id,building_id,college_id,telephone_number,types,sp_name,priority,status) values (#{id},#{name},#{sex},#{id_card},#{workID},#{dormitory_id},#{class_id},#{building_id},#{college_id},#{telephone_number},#{types},#{sp_name},#{priority},#{status})")
    Integer addUser(User user);

    /**
     * 增加通通锁用户信息
     * @param ttlUser
     * @return
     */
    @Insert("insert into ttlUser(id,ttlUserName,ttlPassword,ttlTocken) values (#{id},#{ttlUserName},#{ttlPassword},#{ttlTocken})")
    Integer addTTLUser(TTLUser ttlUser);
    /**
     * 查询通通锁用户信息数量
     * @param openID
     * @return
     */
    @Select("SELECT count(1) FROM ttluser WHERE id=#{openID}")
    Integer queryTTLUserCount(String openID);
    /**
     * 查询通通锁用户信息
     * @param openID
     * @return
     */
    @Select("SELECT * FROM ttluser WHERE id=#{openID}")
    TTLUser getTTLUserInfo(String openID);
    /**
     * 修改通通锁用户信息
     * @param
     * @return
     */
    @Update("update ttluser set ttlUserName=#{ttlUserName},ttlPassword=#{ttlPassword},ttlTocken=#{ttlTocken} where id=#{id}")
    Integer updateTTLUserInfo(TTLUser ttlUser);
    /**
     * 通过学号/工号修改用户
     * @param user
     * @return
     */
    @Update("update user set name=#{name},sex=#{sex},id_card=#{id_card},workID=#{workID},dormitory_id=#{dormitory_id},class_id=#{class_id},building_id=#{building_id},college_id=#{college_id},telephone_number=#{telephone_number},types=#{types},sp_name=#{sp_name},priority=#{priority},status=#{status} where id=#{id}")
    Integer updateUserById(User user);
    /**
     * 通过学号/工号修改用户状态
     * @param
     * @return
     */
    @Update("update user set status=#{status} where id=#{id}")
    Integer updateStatusById(@Param("id") String id,@Param("status") String status);

//    /**
//     * 通过身份证号修改用户
//     * @param user
//     * @return
//     */
//    @Update("update user set id=#{id},name=#{name},sex=#{sex},dormitory_id=#{dormitory_id},class_id=#{class_id},building_id=#{building_id},college_id=#{college_id},telephone_number=#{telephone_number},types=#{types},sp_name=#{sp_name},priority=#{priority},status=#{status} where id_card=#{id_card}")
//    Integer updateUserByIdcard(User user);

    /**
     * 通过学号/工号查询用户
     * @param id
     * @return
     */
    @Select("select id,name,sex,id_card,workID,dormitory_id,class_id,building_id,college_id,telephone_number,types,sp_name,priority,status from user where id=#{id}")
    User selectUserById(String id);

    /**
     * 通过学号/工号查 一共多少人
     * @param workId
     * @return
     */
    @Select("select count(1) from user where workId=#{workId}")
    Integer selectCountByWorkId(String workId);

    /**
     * 通过唯一ID查是否有数据
     */
    @Select("select count(1) from user where id=#{id}")
    Integer selectCountByUserId(String id);
    /**
     * 插入唯一ID并将状态设置为匿名
     */
    @Insert("insert into user(id,status) values (#{id},#{status})")
    Integer enrollUser(@Param("id") String id,@Param("status") String status);
    /**
     * 通过身份证号查用户
     * @param id_card
     * @return
     */
    @Select("select id,name,sex,id_card,dormitory_id,class_id,building_id,college_id,telephone_number,types,sp_name,priority,status from user where id_card=#{id_card}")
    ArrayList<User> selectUserByIdcard(String id_card);
    /**
     * 校验是否有不同微信openID用户 有相同手机号、身份证、学号
     */
    @Select({"<script>",
            "select count(1) from user where id!=#{id} and (telephone_number=#{telephone_number}",
            "<if test=\"id_card!=''\">",
                " or  id_card=#{id_card}",
            "</if>",
            "<if test=\"workID!=''\">",
                "or workID=#{workID} ",
            "</if>",
            ")",
            "</script>"})
    Integer checkUser(@Param("id") String id,@Param("telephone_number") String telephone_number,@Param("id_card") String id_card,@Param("workID") String workID);
    /**
     * 通过身份证号查一共多少人
     * @param id_card
     * @return
     */
    @Select("select count(1) from user where id_card=#{id_card}")
    Integer selectCountByIdcard(String id_card);
    /**
     * 查学院号
     */
    @Select("SELECT DISTINCT NAME FROM college_info")
    ArrayList<String>  getCollegeName();
    /**
     * 通过学院号查学院名
     */
    @Select("SELECT DISTINCT NAME FROM college_info where id=#{id}")
    String getCollegeNameById(String id);
    /**
     * 查学院数量
     */
    @Select("SELECT count(1) FROM college_info")
    Integer  getCollegeCount();
    /**
     * 查班级名
     */
    @Select("SELECT ID,NAME FROM class_info WHERE college_id IN (SELECT DISTINCT id FROM college_info WHERE NAME = #{collegeName})")
    List<HashMap> getClassInfo(String collegeName);
    /**
     * 查班级数量
     */
    @Select("SELECT count(1) FROM class_info WHERE college_id IN (SELECT DISTINCT id FROM college_info WHERE NAME = #{collegeName})")
    Integer getClassCount(String collegeName);

    /**
     * 查楼栋号
     */
    @Select("SELECT DISTINCT ID FROM school_building_info")
    ArrayList<String> getBuildingName();
    /**
     * 查楼栋数量
     */
    @Select("SELECT count(1) FROM school_building_info")
    Integer  getBuildingCount();

    /**
     * 查宿舍名
     */
    @Select("SELECT ID FROM dormitory_info WHERE building_id=#{buildingId}")
    ArrayList<String> getDormitoryName(@Param("buildingId") String buildingId);
    /**
     * 查宿舍数量
     */
    @Select("SELECT count(1) FROM dormitory_info WHERE building_id=#{buildingId}")
    Integer getDormitoryCount(@Param("buildingId") String buildingId);

    /**
     * 查用户状态
     */
    @Select("SELECT types,status FROM user WHERE  ID=#{openID}")
    HashMap getUserStatus(String openID);
    @Select("SELECT count(1) FROM user WHERE ID=#{openID}")
    Integer getUserStatusCount(String openID);
}
