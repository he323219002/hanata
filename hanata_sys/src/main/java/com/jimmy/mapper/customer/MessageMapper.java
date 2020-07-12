package com.jimmy.mapper.customer;

import com.github.pagehelper.Page;
import com.jimmy.bean.Message;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * @author Jimmy He
 * @date 2020-06-13
 */

@Component
@Mapper
public interface MessageMapper {
    @Insert("insert into message (uid,createTime,createUserId,title,content,userId,username,avatar) values (#{uid},#{datetime},1,#{title},#{content},#{userId},#{username},#{avatar})")
    public Integer sendMessage(String uid, Date datetime,String title,String content,String userId,String username,String avatar);

    @Update("update message set isRead=1,updateTime=#{datetime} where uid=#{uid}")
    public Integer readMessage(String uid,Date datetime);

    @Select("select title,content,isRead,createTime from message where userId=#{userId} and uid=#{uid}")
    public Message showMessage(String uid,String userId);

    @Select("select * from message where userId=#{userId} order by createTime desc")
    public Page<Message> listMessage(String userId);

    @Select("select * from message where userId=#{userId} and isRead=0 order by createTime desc")
    public Page<Message> listUnreadMessage(String userId);

    @Select("select count(1) from message where userId=#{userId} and isRead=0}")
    public Integer unReadMessage(String userId);


    @Update("update message set isRead=1 where userId=#{userId} and isRead=0")
    public Integer readAll(String userId);
}
