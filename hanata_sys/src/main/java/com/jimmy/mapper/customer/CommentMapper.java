package com.jimmy.mapper.customer;

import com.github.pagehelper.Page;
import com.jimmy.bean.Comment;
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
public interface CommentMapper {

    @Insert("insert into comment (uid,createTime,createUserId,articleId,content,fartherId,userName,userAvatar,receiverId,fartherContent) values (#{uid},#{datetime},#{userId},#{articleId},#{content},#{fartherId},#{userName},#{userAvatar},#{receiverId},#{fatherContent})")
    public Integer newComment(String uid, Date datetime,String userId,String articleId,String content,String fartherId,String userName,String userAvatar,String receiverId,String fatherContent);

    @Update("update comment set deleted=1,updateTime=#{updateTime},updateUserId=#{updateUserId} where uid=#{uid} and receiverId=#{updateUserId}")
    public Integer delComment(String uid,Date updateTime,String updateUserId);

    @Select("select * from comment where articleId=#{articleId} order by createTime desc")
    public Page<Comment> listComment(String articleId);

    @Select("Select * from comment where articleId=#{dairyId} order by createTime desc")
    public Page<Comment> listDairyComment(String dairyId);

    @Select("select * from comment where uid=#{uid} and deleted=0")
    public Comment getCommentById(String uid);

    @Update("update article set commentCount=commentCount+1 where uid=#{uid}")
    public Integer addCommentCount(String uid);

    @Select("select * from comment where receiverId=#{receiverId} and deleted=0")
    public Page<Comment> listMyComment(String receiverId);

    @Select("select uid from comment where fartherId=#{id}")
    public List<String> listSonComment(String id);

    @Update("update comment set fartherContent=\"该评论已删除\" where fartherId=#{id}")
    public Integer delFartherContent(String id);

}
