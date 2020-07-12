package com.jimmy.mapper.blog;

import com.github.pagehelper.Page;
import com.jimmy.bean.Label;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * @author Jimmy He
 * @date 2020-06-09
 */

@Component
@Mapper
public interface LabelMapper {

    @Insert("insert into label (uid,createTime,updateTime,createUserId,updateUserID,deleted,name,count) values (#{uid},#{createTime},#{createTime},#{createUserId},#{createUserId},0,#{name},0)")
    public Integer addLabel(String uid,Date createTime,String createUserId,String name);

    @Update("update label set deleted=1,updateTime=#{updateTime},updateUserId=#{updateUserId} where uid=#{uid} and deleted=0")
    public Integer delLabel(String uid,Date updateTime,String updateUserId);

    @Update("update label set name=#{name},updateTime=#{updateTime},updateUserId=#{updateUserId} where uid=#{uid} and deleted=0")
    public Integer updateLabel(String uid, String name,Date updateTime,String updateUserId);

    @Select("select * from label where deleted=0 and name like '%${q}%' order by createTime desc")
    public Page<Label> listLabel(String q);

    @Select("select * from label where name=#{name} and deleted=0")
    public List<Label> getLabelByName(String name);

    @Select("select * from label where name=#{name} and uid!=#{uid} and deleted=0")
    public List<Label> checkLabelName(String uid,String name);

    @Select("select * from label where uid=#{uid} and deleted=0")
    public Label getLabelById(String uid);

    @Select("select count(1) from label where uid in (${list})")
    public Integer getLabelByIdList(String list);

    @Update("update label set count=count-1 where uid in (${list})")
    public Integer subLabelCount(String list);

    @Update("update label set count=count+1 where uid in (${list})")
    public Integer addLabelCount(String list);
}
