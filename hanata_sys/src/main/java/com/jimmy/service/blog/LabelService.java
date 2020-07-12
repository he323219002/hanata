package com.jimmy.service.blog;

import com.github.pagehelper.Page;
import com.jimmy.bean.Label;
import com.jimmy.bean.User;
import com.jimmy.exception.AlreadyExistsException;
import com.jimmy.exception.CustomException;

import com.jimmy.mapper.blog.ArticleLabelMapper;
import com.jimmy.mapper.blog.LabelMapper;
import com.jimmy.servlet.RequestContext;
import com.jimmy.utils.SnowFlake;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.*;

/**
 * @author Jimmy He
 * @date 2020-06-09
 */

@Service
public class LabelService {
    @Autowired
    LabelMapper labelMapper;

    @Autowired
    SnowFlake mySnowFlake;

    @Autowired
    ArticleLabelMapper articleLabelMapper;

    @Autowired
    @Qualifier("bCryptPasswordEncoder")
    BCryptPasswordEncoder encoder;

    public Integer addLabel(String name) throws CustomException, AlreadyExistsException {
        if(name.equals("")){
            throw new CustomException("参数必填");
        }

        User user = RequestContext.get().getUser();
        if (!user.getRole().equals("0")) {
            throw new CustomException("权限不足");
        }

        List<Label> labelByName = labelMapper.getLabelByName(name);
        if (labelByName.size() != 0) {
            throw new AlreadyExistsException("名称已存在");
        }

        Date datetime = Calendar.getInstance().getTime();
        String uid = String.valueOf(mySnowFlake.nextId());
        String userId = user.getUid();
        return labelMapper.addLabel(uid, datetime, userId, name);
    }


    public Integer updateLable(String uid, String name) throws CustomException, AlreadyExistsException {
        if(uid.equals("")||name.equals("")){
            throw new CustomException("参数必填");
        }

        User user = RequestContext.get().getUser();
        if (!user.getRole().equals("0")) {
            throw new CustomException("权限不足");
        }

        Label labelById = labelMapper.getLabelById(uid);
        if (labelById == null) {
            throw new CustomException("参数错误");
        }

        List<Label> labels = labelMapper.checkLabelName(uid, name);
        if (labels.size() != 0) {
            throw new AlreadyExistsException("名称已存在");
        }

        Date datetime = Calendar.getInstance().getTime();
        String userId = RequestContext.get().getUser().getUid();

        articleLabelMapper.updateALLabel(userId,datetime,name,uid);
        return labelMapper.updateLabel(uid, name, datetime, userId);
    }


    @Transactional
    public Integer delLabel(String uid) throws CustomException {
        if(uid.equals("")){
            throw new CustomException("参数必填");
        }


        Label labelById = labelMapper.getLabelById(uid);
        if (labelById == null) {
            throw new CustomException("参数错误");
        }


        User user = RequestContext.get().getUser();
        if (!user.getRole().equals("0")) {
            throw new CustomException("权限不足");
        }

        Date datetime = Calendar.getInstance().getTime();
        String userId = RequestContext.get().getUser().getUid();

        articleLabelMapper.delALByLabelId(datetime,userId,uid);
        return labelMapper.delLabel(uid, datetime, userId);
    }


    public Page<Label> listLabel(String q) {
        return labelMapper.listLabel(q);
    }

    public Integer getLabelByIdList(List<String> idList){
        String SIdList = StringUtils.strip(idList.toString(), "[]");
        if(idList.size()==0){
            return 0;
        }
        return labelMapper.getLabelByIdList(SIdList);

    }

}
