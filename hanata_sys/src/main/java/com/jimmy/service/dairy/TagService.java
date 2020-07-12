package com.jimmy.service.dairy;

import com.github.pagehelper.Page;
import com.jimmy.bean.Tag;
import com.jimmy.bean.User;
import com.jimmy.exception.AlreadyExistsException;
import com.jimmy.exception.CustomException;
import com.jimmy.mapper.dairy.TagMapper;
import com.jimmy.servlet.RequestContext;
import com.jimmy.utils.SnowFlake;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author Jimmy He
 * @date 2020-06-15
 */

@Service
public class TagService {

    @Autowired
    TagMapper tagMapper;

    @Autowired
    SnowFlake mySnowFlake;

    @Autowired
    @Qualifier("bCryptPasswordEncoder")
    BCryptPasswordEncoder encoder;

    public Integer newTag(String name) throws CustomException, AlreadyExistsException {
        User user = RequestContext.get().getUser();
        if (!user.getRole().equals("0")) {
            throw new CustomException("权限不足");
        }

        List<Tag> tagByName = tagMapper.getTagByName(name);
        if (tagByName.size() != 0) {
            throw new AlreadyExistsException("名称已存在");
        }

        Date datetime = Calendar.getInstance().getTime();
        String uid = String.valueOf(mySnowFlake.nextId());
        String userId = user.getUid();
        return tagMapper.newTag(uid, datetime, userId, name);
    }

    public Integer delTag(String uid) throws CustomException {

        Tag tagById = tagMapper.getTagById(uid);
        if (tagById == null) {
            throw new CustomException("参数错误");
        }

        User user = RequestContext.get().getUser();
        if (!user.getRole().equals("0")) {
            throw new CustomException("权限不足");
        }

        Date datetime = Calendar.getInstance().getTime();
        String userId = user.getUid();

        return tagMapper.delTag(uid, datetime, userId);
    }

    public Integer updateTag(String uid, String name) throws CustomException, AlreadyExistsException {
        User user = RequestContext.get().getUser();
        if (!user.getRole().equals("0")) {
            throw new CustomException("权限不足");
        }

        Tag tagById = tagMapper.getTagById(uid);
        if (tagById == null) {
            throw new CustomException("参数错误");
        }

        List<Tag> tags = tagMapper.checkTagByName(uid, name);
        if (tags.size() != 0) {
            throw new AlreadyExistsException("名称已存在");
        }

        Date datetime = Calendar.getInstance().getTime();
        String userId = user.getUid();
        return tagMapper.updateTag(uid, name, datetime, userId);
    }

    public Page<Tag> listTag(String q) {
        return tagMapper.listtag(q);
    }

}
