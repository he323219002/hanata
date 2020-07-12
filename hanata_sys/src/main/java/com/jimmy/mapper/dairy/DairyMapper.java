package com.jimmy.mapper.dairy;


import com.github.pagehelper.Page;
import com.jimmy.bean.Article;
import com.jimmy.bean.Dairy;
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
public interface DairyMapper {

    @Insert("insert into dairy (uid,createTime,createUserId,title,content,tagId,tagName,preId,preName,open,commentOpen,coverPic,state,picId) " +
            "values (#{uid},#{createTime},#{userId},#{title},#{content},#{tagId},#{tagName},#{preId},#{preName},#{open},#{commentOpen},#{coverPic},#{state},#{picId})")
    public Integer newDairy(String uid, Date createTime, String userId, String title, String content, String tagId, String tagName, String preId, String preName, String open, String commentOpen,String coverPic,String state,String picId);


    @Update("update dairy set deleted=1,updateTime=#{updateTime},updateUserId=#{userId} where uid=#{uid}")
    public Integer delDairy(String uid, Date updateTime, String userId);

    @Update("update dairy set title=#{title},content=#{content},tagId=#{tagId},tagName=#{tagName},open=#{open},commentOpen=#{commentOpen},updateTime=#{updateTime},updateUserId=#{userId},coverPic=#{coverPic},state=#{state} where uid=#{uid} and deleted=0")
    public Integer updateDairy(String uid,String title,String content,String tagId,String tagName,String open,String commentOpen,Date updateTime,String userId,String coverPic,String state);

    @Select("select * from dairy where deleted=0 and title like '%${q}%' order by createTime desc")
    public Page<Dairy> listBackendDairy(String q);

    @Select("select * from dairy where deleted=0 and state=#{state} and title like '%${q}%' order by createTime desc")
    public Page<Dairy> listBackendStateDairy(String q,String state);

    @Select("select * from dairy where deleted=0 and open=1 and state=1 order by createTime desc")
    public Page<Dairy> listDairy();


    @Select("select * from dairy where deleted=0 and title like '%${q}%' and tagId=#{tagId} order by createTime desc")
    public Page<Dairy> listBackendTagDairy(String q,String tagId);

    @Select("select * from dairy where deleted=0 and title like '%${q}%' and state=#{state} and tagId=#{tagId} order by createTime desc")
    public Page<Dairy> listBackendStateTagDairy(String q,String tagId,String state);

    @Select("select * from dairy where deleted=0 and open=1 and state=1 and tagId=#{tagId} order by createTime desc")
    public Page<Dairy> listTagDairy(String tag);


    @Select("select * from dairy where title=#{title} and deleted=0")
    public List<Dairy> getDairyByTitle(String title);

    @Select("select * from dairy where title=#{title} and uid!=#{uid} and deleted=0")
    public List<Dairy> checkDairyByTitle(String uid,String title);

    @Select("select * from dairy where uid=#{uid} and deleted=0")
    public Dairy getDairyById(String uid);


    @Select("select * from dairy where deleted=0 order by createTime desc limit 1")
    public Dairy findLastDairy();


    @Update("update dairy set preId=#{preId},preName=#{preName} where uid=#{uid}")
    public Integer changePre(String uid,String preId,String preName);

    @Update("update dairy set nextId=#{nextId},nextName=#{nextName} where uid=#{uid}")
    public Integer changeNext(String uid,String nextId,String nextName);

    @Update("update dairy set viewCount=viewCount+1 where uid=#{uid}")
    public Integer addViewCount(String uid);
}
