package com.jimmy.mapper;

import com.github.pagehelper.Page;
import com.jimmy.bean.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Jimmy He
 * @date 2020-06-05
 */

@Mapper
@Component
public interface UserMapper {
    @Select("select * from user where uid=#{uid}")
    public User getUserById(String id);

    @Insert("insert into user (uid,username,password,phone,mail,role) values (#{uid},#{username},#{password},#{phone},#{mail},1)")
    public Integer createAdmin(String uid,String username,String password,String phone,String mail);

    @Update("update user set password=#{password},updateTime=#{datetime},updateUserId=#{userId} where uid=#{userId} and isActive=1")
    public Integer updatePassword(String userId, Date datetime, String password);

    // check for repeat data
    @Select("select * from user where username=#{username} and deleted=0")
    public User getUserByUsername(String username);

    @Select("select * from user where phone=#{phone} and deleted=0")
    public User getUserByPhone(String Phone);

    @Select("select * from user where mail=#{mail} and deleted=0")
    public User getUserByMail(String mail);

    @Insert("insert into user (uid,username,password,nickname,avatar,mail,gender,createTime) values (#{uid},#{username},#{password},#{nickname},#{avatar},#{mail},#{gender},#{datetime})")
    public Integer registUser(String uid,String username,String password,String nickname,String avatar,String mail,String gender,Date datetime);


    @Select("select * from user where nickname=#{nickname} and deleted=0")
    public List<User> getUserByNickname(String nickname);

    @Select("select uid from user where mail=#{mail} and deleted=0")
    public String getUserIdByMail(String mail);

    @Select("select * from user where role=1 and (nickname like '%${q}%' or phone like '%${q}%' or username like '%${q}%' or mail like '%${q}%') order by lastLoginDate desc")
    public Page<User> listUser(String q);

    @Update("update user set lastLoginDate=#{datetime} where uid=#{userId}")
    public Integer updateLoginTime(String userId,Date datetime);

    @Update("update user set isActive=#{active} where uid=#{userId}")
    public Integer updateUserActive(String userId,String active);

    @Update("update user set nickname=#{nickname},mail=#{mail},avatar=#{avatar} where uid=#{uid}")
    public Integer updateUserById(String uid,String nickname,String mail,String avatar);

    @Select("select * from user where uid!=#{uid} and nickname=#{nickname}")
    public List<User> updateUserByNickname(String uid,String nickname);

    @Select("select * from user where uid!=#{uid} and mail=#{mail}")
    public List<User> updateUserByMail(String uid,String mail);
}
