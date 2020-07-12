package com.jimmy.mapper.dairy;

import com.github.pagehelper.Page;
import com.jimmy.bean.Category;
import com.jimmy.bean.Tag;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * @author Jimmy He
 * @date 2020-06-15
 */

@Component
@Mapper
public interface TagMapper {
    @Insert("insert into tag (uid,createTime,updateTime,createUserId,updateUserID,name) values (#{uid},#{createTime},#{createTime},#{createUserId},#{createUserId},#{name})")
    public Integer newTag(String uid, Date createTime, String createUserId, String name);

    @Update("update tag set deleted=1,updateTime=#{updateTime},updateUserId=#{updateUserId} where uid=#{uid} and deleted=0")
    public Integer delTag(String uid,Date updateTime,String updateUserId);

    @Update("update tag set name=#{name},updateTime=#{updateTime},updateUserId=#{updateUserId} where uid=#{uid} and deleted=0")
    public Integer updateTag(String uid, String name,Date updateTime,String updateUserId);

    @Select("select * from tag where deleted=0 and name like '%${q}%' order by createTime desc")
    public Page<Tag> listtag(String q);

    @Select("select * from tag where name=#{name} and deleted=0")
    public List<Tag> getTagByName(String name);

    @Select("select * from tag where name=#{name} and uid!=#{uid} and deleted=0")
    public List<Tag> checkTagByName(String uid,String name);

    @Select("select * from tag where uid=#{uid} and deleted=0")
    public Tag getTagById(String uid);

    @Update("update tag set count=count+1 where uid=#{id}")
    public Integer addTagCount(String id);

    @Update("update tag set count=count-1 where uid=#{id}")
    public Integer subTagCount(String id);
}
