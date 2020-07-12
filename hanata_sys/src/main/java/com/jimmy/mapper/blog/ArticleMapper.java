package com.jimmy.mapper.blog;

import com.github.pagehelper.Page;
import com.jimmy.bean.Article;
import com.jimmy.bean.Dairy;
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
 * @date 2020-06-10
 */
@Component
@Mapper
public interface ArticleMapper {

    @Insert("insert into article (uid,createTime,updateTime,createUserId,updateUserId,title,content,categoryId,categoryName,preId,preName,open,commentOpen,state) " +
            "values (#{uid},#{createTime},#{createTime},#{userId},#{userId},#{title},#{content},#{categoryId},#{categoryName},#{preId},#{preName},#{open},#{commentOpen},#{state})")
    public Integer newArticle(String uid, Date createTime, String userId, String title, String content, String categoryId, String categoryName, String preId, String preName,String open,String commentOpen,String state);

    @Update("update article set deleted=1,updateTime=#{updateTime},updateUserId=#{userId} where uid=#{uid}")
    public Integer delArticle(String uid, Date updateTime, String userId);

    @Update("update article set preId=#{preId},preName=#{preName} where uid=#{uid}")
    public Integer changePre(String uid,String preId,String preName);

    @Update("update article set nextId=#{nextId},nextName=#{nextName} where uid=#{uid}")
    public Integer changeNext(String uid,String nextId,String nextName);

    @Update("update article set title=#{title},content=#{content},categoryId=#{categoryId},categoryName=#{categoryName},open=#{open},commentOpen=#{commentOpen},updateTime=#{updateTime},updateUserId=#{userId},state=#{state} where uid=#{uid} and deleted=0")
    public Integer updateArticle(String uid,String title,String content,String categoryId,String categoryName,String open,String commentOpen,Date updateTime,String userId,String state);

    @Select("select * from article where deleted=0 and open=1 and categoryId=#{categoryId} and state=1 order by createTime desc")
    public Page<Article> listCategoryArticle(String categoryId);


    @Select("select * from article where deleted=0 and categoryId=#{categoryId} and title like '%${field}%' order by createTime desc")
    public Page<Article> listBackendCategoryArticle(String field,String categoryId);

    @Select("select * from article where deleted=0 and categoryId=#{categoryId} and state=#{state} and title like '%${field}%' order by createTime desc")
    public Page<Article> listBackendStateCategoryArticle(String field,String categoryId,String state);

    @Select("select * from article where deleted=0 and open=1 and state=1 order by createTime desc")
    public Page<Article> listArticle();


    @Select("select * from article where deleted=0 and title like '%${field}%' order by createTime desc")
    public Page<Article> listBackendArticle(String field);

    @Select("select * from article where deleted=0 and state=#{state} and title like '%${field}%' order by createTime desc")
    public Page<Article> listBackendStateArticle(String field, String state);


    @Select("select * from article where uid in (select articleId from article_label where deleted=0 and open=1 and labelId=#{labelId} and state=1 order by createTime desc)")
    public Page<Article> listLabelArticle(String labelId);

    @Select("select * from article where uid in (select articleId from article_label where deleted=0 and labelId=#{labelId} and title like '%${field}%' order by createTime desc)")
    public Page<Article> listBackendLabelArticle(String labelId,String field);

    @Select("select * from article where uid in (select articleId from article_label where deleted=0 and labelId=#{labelId} and state=#{state} and title like '%${field}%' order by createTime desc)")
    public Page<Article> listBackendStateLabelArticle(String labelId,String field,String state);

    @Select("select * from article where title=#{title} and deleted=0")
    public List<Article> getArticleByTitle(String title);

    @Select("select * from article where title=#{title} and uid!=#{uid} and deleted=0")
    public List<Article> checkArticleByTitle(String uid,String title);

    @Select("select * from article where uid=#{uid} and deleted=0")
    public Article getArticleById(String uid);


    @Select("select * from article where deleted=0 order by createTime desc limit 1")
    public Article findLastArticle();

    @Update("update article set viewCount=viewCount+1 where uid=#{uid}")
    public Integer addViewCount(String uid);

    @Update("update article set categoryId=\"\" where categoryId=#{uid} and deleted=0")
    public Integer clearCategory(String uid);

    @Select("select * from dairy where uid=#{uid} and deleted=0")
    public Dairy getDairyById(String uid);


}
