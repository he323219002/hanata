package com.jimmy.mapper.blog;

import com.github.pagehelper.Page;
import com.jimmy.bean.Article;
import com.jimmy.bean.Category;
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
 * @date 2020-06-11
 */
@Component
@Mapper
public interface CategoryMapper {

    @Insert("insert into category (uid,createTime,updateTime,createUserId,updateUserID,name) values (#{uid},#{createTime},#{createTime},#{createUserId},#{createUserId},#{name})")
    public Integer newCategory(String uid, Date createTime, String createUserId, String name);

    @Update("update category set deleted=1,updateTime=#{updateTime},updateUserId=#{updateUserId} where uid=#{uid} and deleted=0")
    public Integer delCategory(String uid,Date updateTime,String updateUserId);

    @Update("update category set name=#{name},updateTime=#{updateTime},updateUserId=#{updateUserId} where uid=#{uid} and deleted=0")
    public Integer updateCategory(String uid, String name,Date updateTime,String updateUserId);

    @Select("select * from category where deleted=0 and name like '%${field}%'  order by createTime desc")
    public Page<Category> listCategory(String field);

    @Select("select * from article where categoryId=#{id} and deleted=0 and state=1 and open=1")
    public Page<Article> getArticleByCategoryId(String id);

    @Select("select * from article where categoryId=#{id} and deleted=0")
    public Page<Article> getArticleByCategoryIdBackend(String id);

    @Select("select * from category where name=#{name} and deleted=0")
    public List<Category> getCategoryByName(String name);

    @Select("select * from category where name=#{name} and uid!=#{uid} and deleted=0")
    public List<Category> checkCategoryByName(String uid,String name);

    @Select("select * from category where uid=#{uid} and deleted=0")
    public Category getCategoryById(String uid);

    @Update("update category set count=count+1 where uid=#{id}")
    public Integer addCategoryCount(String id);
    
    @Update("update category set count=count-1 where uid=#{id}")
    public Integer subCategoryCount(String id);
}
