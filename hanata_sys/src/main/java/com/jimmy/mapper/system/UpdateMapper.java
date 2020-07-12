package com.jimmy.mapper.system;

import com.github.pagehelper.Page;
import com.jimmy.bean.UpdateRecord;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author Jimmy He
 * @date 2020-06-20
 */

@Component
@Mapper
public interface UpdateMapper {

    @Insert("insert into updateRecord (uid,createTime,createUserId,version,content) values (#{uid},#{createTime},#{createUserId},#{version},#{content})")
    public Integer newUpdate(String uid, Date createTime, String createUserId,String version,String content);

    @Update("update updateRecord set deleted=1,updateTime=#{updateTime},updateUserId=#{userId} where uid=#{uid}")
    public Integer delUpdate(String uid,Date updateTime,String userId);

    @Update("update updateRecord set version=#{version},content=#{content},updateTime=#{updateTime},updateUserId=#{userId} where uid=#{uid}")
    public Integer updateRecord(String uid,Date updateTime,String userId,String version,String content);

    @Select("select * from updateRecord where deleted=0 order by createTime desc")
    public Page<UpdateRecord> listUpdate();

    @Select("select * from updateRecord where uid=#{uid}")
    public UpdateRecord getRecordById(String uid);

}
