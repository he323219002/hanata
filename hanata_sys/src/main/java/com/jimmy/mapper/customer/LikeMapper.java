package com.jimmy.mapper.customer;

import com.jimmy.bean.Like;
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
public interface LikeMapper {
    @Insert("insert into favour (uid,createTime,updateTime,createUserId,updateUserID,deleted,userName,articleId,title) values (#{uid},#{createTime},#{createTime},#{createUserId},#{createUserId},0,#{userName},#{articleId},#{title})")
    public Integer newFavour(String uid, Date createTime, String createUserId,String userName,String articleId,String title);

    @Update("update favour set deleted=1,updateTime=#{updateTime},updateUserId=#{userId} where uid=#{uid}")
    public Integer cancelFavour(String uid,Date updateTime,String userId);

    @Select("select * from favour where articleId=#{articleId} and createUserId=#{userId} and deleted=0")
    public Like getFavourUid(String articleId, String userId);

    @Update("update article set likeCount=likeCount+1 where uid=#{uid}")
    public Integer addLikeCount(String uid);

    @Update("update article set likeCount=likeCount-1 where uid=#{uid}")
    public Integer subLikeCount(String uid);

    @Select("select userName,userAvatar,createTime from favour where articleId=#{articleId} and deleted=0")
    public List<Like> listLike(String articleId);
}
