package com.jimmy.service.customer;

import com.github.pagehelper.Page;
import com.jimmy.bean.Article;
import com.jimmy.bean.Comment;
import com.jimmy.bean.Dairy;
import com.jimmy.bean.User;
import com.jimmy.exception.CustomException;
import com.jimmy.mapper.blog.ArticleMapper;
import com.jimmy.mapper.customer.CommentMapper;
import com.jimmy.mapper.customer.MessageMapper;
import com.jimmy.servlet.RequestContext;
import com.jimmy.utils.SnowFlake;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author Jimmy He
 * @date 2020-06-13
 */

@Service
@Transactional
public class CommentService {
    @Autowired
    MessageMapper messageMapper;

    @Autowired
    SnowFlake mySnowFlake;

    @Autowired
    ArticleMapper articleMapper;

    @Autowired
    @Qualifier("bCryptPasswordEncoder")
    BCryptPasswordEncoder encoder;

    @Autowired
    CommentMapper commentMapper;

    public Integer newComment(String articleId, String content, String fartherId,String kind) throws CustomException {
        User user = RequestContext.get().getUser();

        if (content.equals("")) {
            throw new CustomException("请填写内容");
        }
        String title;
        String receiverId;
        if (kind.equals("1")){
            Article articleById = articleMapper.getArticleById(articleId);
            if (articleById == null) {
                throw new CustomException("参数错误");
            }

            receiverId = articleById.getCreateUserId();
            title = articleById.getTitle();
        }
        else{
            Dairy dairyById = articleMapper.getDairyById(articleId);
            if (dairyById == null) {
                throw new CustomException("参数错误");
            }
            receiverId = dairyById.getCreateUserId();
            title = dairyById.getTitle();
        }


        Date datetime = Calendar.getInstance().getTime();
        String uid = String.valueOf(mySnowFlake.nextId());

        String fatherContent ="";
        if (!fartherId.equals("")) {
            Comment commentById = commentMapper.getCommentById(fartherId);
            if (commentById == null) {
                throw new CustomException("参数错误");
            }
            fatherContent = commentById.getContent();
            messageMapper.sendMessage(String.valueOf(mySnowFlake.nextId()),datetime,"你有新的回复，",
                    user.getNickname()+"回复:"+content,commentById.getCreateUserId(),user.getNickname(),user.getAvatar());
        }

        // inform message


        messageMapper.sendMessage(String.valueOf(mySnowFlake.nextId()), datetime, "你的《" + title + "》有一条新评论", user.getNickname()
                + "回复：" + content, receiverId,user.getNickname(),user.getAvatar());

        commentMapper.addCommentCount(articleId);

        return commentMapper.newComment(uid, datetime, user.getUid(), articleId, content, fartherId, user.getNickname(), user.getAvatar(), receiverId,fatherContent);

    }



    public Integer delComment(String uid) throws CustomException {
        User user = RequestContext.get().getUser();

        Comment commentById = commentMapper.getCommentById(uid);
        if (commentById == null) {
            throw new CustomException("参数错误");
        }

        commentMapper.delFartherContent(uid);

        Date datetime = Calendar.getInstance().getTime();
        return commentMapper.delComment(uid, datetime, user.getUid());
    }


    public Page<Comment> listComment(String articleId) {
        return commentMapper.listComment(articleId);
    }

    public Page<Comment> listDairyComment(String dairyId){
        return commentMapper.listDairyComment(dairyId);
    }




    public Page<Comment> listMyComment(){
        User user = RequestContext.get().getUser();

        return commentMapper.listMyComment(user.getUid());
    }

}
