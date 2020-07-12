package com.jimmy.service.customer;

import com.jimmy.bean.Article;
import com.jimmy.bean.Like;
import com.jimmy.bean.User;
import com.jimmy.exception.CustomException;
import com.jimmy.mapper.blog.ArticleMapper;
import com.jimmy.mapper.customer.LikeMapper;
import com.jimmy.mapper.customer.MessageMapper;
import com.jimmy.servlet.RequestContext;
import com.jimmy.utils.CommonUtils;
import com.jimmy.utils.SnowFlake;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @author Jimmy He
 * @date 2020-06-13
 */

@Service
@Transactional
public class LikeService {

    @Autowired
    LikeMapper likeMapper;

    @Autowired
    ArticleMapper articleMapper;

    @Autowired
    MessageMapper messageMapper;

    @Autowired
    SnowFlake mySnowFlake;

    @Autowired
    @Qualifier("bCryptPasswordEncoder")
    BCryptPasswordEncoder encoder;

    public Integer newFavour(String articleId) throws CustomException {
        User user = RequestContext.get().getUser();
        if (user == null) {
            throw new CustomException("请先登录");
        }

        Article articleById = articleMapper.getArticleById(articleId);
        if (articleById == null) {
            throw new CustomException("参数错误");
        }

        Like like = likeMapper.getFavourUid(articleId, user.getUid());
        if(like!=null){
            throw new CustomException("已经赞过改文章");
        }

        String title = articleById.getTitle();

        Date datetime = Calendar.getInstance().getTime();
        String uid = String.valueOf(mySnowFlake.nextId());
        String userId = user.getUid();

        // make message inform
        messageMapper.sendMessage(String.valueOf(mySnowFlake.nextId()),datetime,"有人赞了《"+title+"》",user.getNickname()+"称赞了你的文章",articleById.getCreateUserId(),user.getNickname(),user.getAvatar());

        likeMapper.addLikeCount(articleId);

        return likeMapper.newFavour(uid, datetime, userId, user.getNickname(), articleId, title);
    }

    public Integer cancelFavour(String articleId) throws CustomException {
        User user = RequestContext.get().getUser();
        if (user == null) {
            throw new CustomException("请先登录");
        }

        Article articleById = articleMapper.getArticleById(articleId);
        if (articleById == null) {
            throw new CustomException("参数错误");
        }

        Like like = likeMapper.getFavourUid(articleId, user.getUid());
        if (like == null) {
            throw new CustomException("尚未赞该文章");
        }

        Date createTime = like.getCreateTime();
        Date updateTIme = Calendar.getInstance().getTime();

        long minute = CommonUtils.dateSubToMinute(createTime, updateTIme);
        if (minute > 2) {
            throw new CustomException("超过限定时间");
        }

        likeMapper.subLikeCount(articleId);
        return likeMapper.cancelFavour(like.getUid(), updateTIme, user.getUid());
    }

    public List listLike(String articleId){
        List<Like> likes = likeMapper.listLike(articleId);

        ArrayList<Map> likeList = new ArrayList<>();
        if (likes.size()!=0){
            for (Like like : likes) {
                HashMap<String, String> likeMap = new HashMap<>();
                String userName = like.getUserName();
                String userAvatar = like.getUserAvatar();
                String datetime = CommonUtils.formatDate(like.getCreateTime());
                likeMap.put("userName",userName);
                likeMap.put("userAvatar",userAvatar);
                likeMap.put("datetime",datetime);
                likeList.add(likeMap);
            }
        }
        return likeList;
    }
}
