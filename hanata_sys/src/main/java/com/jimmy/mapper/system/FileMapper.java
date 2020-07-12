package com.jimmy.mapper.system;

import com.jimmy.bean.File;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author Jimmy He
 * @date 2020-06-15
 */

@Component
@Mapper
public interface FileMapper {
    @Insert("insert into file (uid,createTime,createUserId,filePath,fileName,contentType) values (#{uid},#{createTime},#{createUserId},#{filePath},#{fileName},#{contentType})")
    public Integer uploadFile(String uid, Date createTime, String createUserId,String filePath, String fileName,String contentType);

    @Select("select * from file where uid=#{uid}")
    public File getFileById(String id);
}
