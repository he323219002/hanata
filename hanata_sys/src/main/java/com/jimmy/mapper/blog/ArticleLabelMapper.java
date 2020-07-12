package com.jimmy.mapper.blog;

import com.jimmy.bean.ArticleLabel;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import javax.xml.crypto.Data;
import java.text.MessageFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author Jimmy He
 * @date 2020-06-11
 */

@Component
@Mapper
public interface ArticleLabelMapper {

    @Insert("insert into article_label (uid,createTime,updateTime,createUserId,updateUserID,deleted,articleId,title,labelId,labelName) values (#{uid},#{createTime},#{createTime},#{createUserId},#{createUserId},0,#{articleId},#{title},#{labelId},#{labelName}")
    public Integer newAL(String uid, Date createTime, String createUserId, String articleId, String title, String labelId, String labelName);

//    @Update("update article_label set deleted=1,updateTime=#{updateTime},updateUserId=#{updateUserId} where uid=#{uid}")
//    public Integer delAL(String uid, Date updateTime, String updateUserId);

    @Select("select labelId from article_label where articleId=#{id} and deleted=0")
    public List<String> getLabelIdByArticleID(String id);

    @Select("select uid from article_label where articleId=#{id} and deleted=0")
    public List<String> getALIdByArticleId(String id);

    @InsertProvider(type = Provider.class, method = "batchInsert")
    Integer batchInsert(List<Map> batchList);

    @Update("update article_label set deleted=1,updateTime=#{updateTime},updateUserId=#{userId} where articleId=#{uid}")
    public Integer delArticle(String uid,Date updateTime,String userId);

    @Update("update article_label set deleted=1,updateTime=#{updateTime},updateUserId=#{userId} where uid in (${ALId})")
    public Integer delAL(String ALId,Date updateTime,String userId);

    @Update("update article_label set deleted=1,updateTime=#{updateTime},updateUserId=#{userId} where labelId=#{labelId}")
    public Integer delALByLabelId(Date updateTime,String userId,String labelId);

    @Update("update article_label set updateTime=#{datetime},updateUserId=#{userId},title=#{title} where articleId=#{articleId} and deleted=0")
    public Integer updateALTitle(String userId,Date datetime,String title,String articleId);

    @Update("update article_label set updateTime=#{datetime},updateUserId=#{userId},labelName=#{labelName} where labelId=#{labelId} and deleted=0")
    public Integer updateALLabel(String userId,Date datetime,String labelName,String labelId);

    @Select("select count(1) from article_label where uid in (${ALId}) and deleted=0 and articleId=#{articleId} and createUserId=#{userId}")
    public Integer checkCount(String ALId,String articleId,String userId);

    @Select("select count(1) from article_label where articleId=#{articleId} and labelId in (${labelIdList}) and deleted=0")
    public Integer checkLabelExist(String articleId,String labelIdList);

    @Update("update label set count=count-1 where uid in (select labelId from article_label where uid in (${ALId}));")
    public Integer updateLabelCount(String ALId);

    @Select("select uid,labelName from article_label where articleId=#{id} and deleted=0")
    public List<ArticleLabel> getALByArticleId(String id);


    class Provider {
        // batch insert
        public String batchInsert(Map map) {
            List<Map> batchList = (List<Map>) map.get("list");


//            List<ArticleLabel> AL = (List<ArticleLabel>) map.get("list");
            StringBuilder builder = new StringBuilder();
            builder.append("INSERT INTO article_label (uid,createTime,createUserId,updateUserID,deleted,articleId,title,labelId,labelName) VALUES ");
            MessageFormat mf = new MessageFormat(
                    "({0},\"{1}\",{2},{3},{4},{5},{6},{7},{8})"
            );
            int i = 0;
            for (Map perMap : batchList) {
                builder.append(mf.format(new Object[]{"\"" + perMap.get("uid") + "\"", perMap.get("datetime"), "\"" + perMap.get("userId") + "\"", "\"" + perMap.get("userId") + "\"", "\"" + "0" + "\"", "\"" + perMap.get("articleId") + "\"", "\"" + perMap.get("title") + "\"", "\"" + perMap.get("labelId") + "\"", "\"" + perMap.get("labelName") + "\""}));
                if (i < batchList.size() - 1) {
                    builder.append(",");
                }
                i++;
            }
            return builder.toString();


        }

        /* 批量删除 */
        public String batchDelete(Map map) {
//            List<Student> students = (List<Student>) map.get("list");
//            StringBuilder sb = new StringBuilder();
//            sb.append("DELETE FROM student WHERE id IN (");
//            for (int i = 0; i < students.size(); i++) {
//                sb.append("'").append(students.get(i).getId()).append("'");
//                if (i < students.size() - 1)
//                    sb.append(",");
//            }
//            sb.append(")");
//            return sb.toString();
            return null;
        }
    }
}
