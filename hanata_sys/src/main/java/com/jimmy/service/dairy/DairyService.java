package com.jimmy.service.dairy;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;
import com.github.pagehelper.Page;
import com.jimmy.bean.*;
import com.jimmy.exception.AlreadyExistsException;
import com.jimmy.exception.CustomException;
import com.jimmy.mapper.dairy.DairyMapper;
import com.jimmy.mapper.dairy.TagMapper;
import com.jimmy.mapper.system.FileMapper;
import com.jimmy.service.system.ESService;
import com.jimmy.servlet.RequestContext;
import com.jimmy.utils.SnowFlake;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author Jimmy He
 * @date 2020-06-15
 */

@Service
@Transactional
public class DairyService {
    @Autowired
    DairyMapper dairyMapper;

    @Autowired
    FileMapper fileMapper;

    @Autowired
    TagMapper tagMapper;


    @Autowired
    SnowFlake mySnowFlake;

    @Autowired
    ESService esService;

    @Autowired
    @Qualifier("bCryptPasswordEncoder")
    BCryptPasswordEncoder encoder;

    public Integer newDairy(String title, String content, String tagId, String open, String commentOpen, String picId, String state)
            throws CustomException, AlreadyExistsException, IOException {
        User user = RequestContext.get().getUser();
        if (!user.getRole().equals("0")) {
            throw new CustomException("权限不足");
        }

        if (title.equals("")) {
            throw new CustomException("标题不能为空");
        }

        if (content.equals("")) {
            throw new CustomException("请输入内容");
        }

        List<Dairy> dairyByTitle = dairyMapper.getDairyByTitle(title);
        if (dairyByTitle.size() != 0) {
            throw new AlreadyExistsException("名称已存在");
        }

        if (picId.equals("")) {
            throw new CustomException("请上传封面");
        }

        File fileById = fileMapper.getFileById(picId);
        if (fileById == null) {
            throw new CustomException("参数错误");
        }
        String filePath = fileById.getFilePath();


        String tagName = "";
        if (!tagId.equals("")) {
            Tag tagById = tagMapper.getTagById(tagId);
            if (tagById == null) {
                throw new CustomException("参数错误1");
            }
            tagName = tagById.getName();
        }


        // set prepage and next page
        Dairy lastDairy = dairyMapper.findLastDairy();
        String preId = "";
        String preName = "";

        Date datetime = Calendar.getInstance().getTime();
        String uid = String.valueOf(mySnowFlake.nextId());
        String userId = user.getUid();

        if (lastDairy != null) {
            dairyMapper.changeNext(lastDairy.getUid(), uid, title);
            preId = lastDairy.getUid();
            preName = lastDairy.getTitle();
        }

        tagMapper.addTagCount(tagId);

        // add es data
        ESModel esModel = new ESModel(uid, datetime, title, content, "0", open, state, "2");
        esService.newDocument(esModel);

        return dairyMapper.newDairy(uid, datetime, userId, title, content, tagId, tagName, preId, preName, open, commentOpen, filePath, state, picId);

    }

    public Integer delDairy(String uid) throws CustomException, IOException {
        User user = RequestContext.get().getUser();
        if (!user.getRole().equals("0")) {
            throw new CustomException("权限不足");
        }

        Dairy dairyById = dairyMapper.getDairyById(uid);
        if (dairyById == null) {
            throw new CustomException("参数错误");
        }

        Date datetime = Calendar.getInstance().getTime();
        String userId = user.getUid();

        // set prepage and next page
        String preId = dairyById.getPreId();
        String nextId = dairyById.getNextId();

        Dairy preDairy = dairyMapper.getDairyById(preId);
        Dairy nextDairy = dairyMapper.getDairyById(nextId);

        //first article
        if (preId.equals("")) {
            if (!nextId.equals("")) {
                dairyMapper.changePre(nextId, "", "");
            }
        }
        // middle article
        else if (!nextId.equals("")) {
            dairyMapper.changeNext(preDairy.getUid(), dairyById.getNextId(), dairyById.getNextName());
            dairyMapper.changePre(nextDairy.getUid(), dairyById.getPreId(), dairyById.getPreName());
        }
        // last
        else {
            dairyMapper.changeNext(preDairy.getUid(), "", "");
        }


        // category count - 1
        String tagId = dairyById.getTagId();
        if (!tagId.equals("")) {
            tagMapper.subTagCount(tagId);
        }


        // del es
        ESModel esModel = new ESModel(uid, datetime, dairyById.getTitle(), dairyById.getContent(), "1", dairyById.getOpen(), dairyById.getState(), "2");
        esService.updateDocument(uid, esModel);

        return dairyMapper.delDairy(uid, datetime, userId);
    }

    public Integer updateDairy(String uid, String title, String content, String tagId, String open, String commentOpen, String picId, String state)
            throws CustomException, AlreadyExistsException {
        User user = RequestContext.get().getUser();
        if (!user.getRole().equals("0")) {
            throw new CustomException("权限不足");
        }

        Dairy dairyById = dairyMapper.getDairyById(uid);
        if (dairyById == null) {
            throw new CustomException("参数错误");
        }

        if (title.equals("")) {
            throw new CustomException("标题不能为空");
        }

        if (content.equals("")) {
            throw new CustomException("请输入内容");
        }

        if (picId.equals("")) {
            throw new CustomException("参数错误1");
        }

        File fileById = fileMapper.getFileById(picId);
        if (fileById == null) {
            throw new CustomException("参数错误2");
        }
        String filePath = fileById.getFilePath();

        List<Dairy> dairies = dairyMapper.checkDairyByTitle(uid, title);
        if (dairies.size() != 0) {
            throw new AlreadyExistsException("名称已存在");
        }

        Date datetime = Calendar.getInstance().getTime();
        String userId = user.getUid();

        String categoryName = "";

        if (!tagId.equals("")) {
            String preTagId = dairyById.getTagId();
            if (!tagId.equals(preTagId)) {
                Tag tagById = tagMapper.getTagById(tagId);
                if (tagById == null) {
                    throw new CustomException("参数错误");
                }
                categoryName = tagById.getName();
                tagMapper.addTagCount(tagId);
                tagMapper.subTagCount(preTagId);
            }
        }

        // update pre name and next name
        String preId = dairyById.getPreId();
        if (!preId.equals("")) {
            dairyMapper.changeNext(preId, uid, title);
        }
        String nextId = dairyById.getNextId();
        if (!nextId.equals("")) {
            dairyMapper.changePre(nextId, uid, title);
        }


        Integer count = dairyMapper.updateDairy(uid, title, content, tagId, categoryName, open, commentOpen, datetime, userId, filePath, state);

        // update es
        ESModel esModel = new ESModel(uid, datetime, title, content, "0", open, state, "2");
        try {
            esService.updateDocument(uid, esModel);
        } catch (IOException e) {
            throw new CustomException("io异常");
        }

        return count;
    }

    public Map showDairy(String uid) throws CustomException {
        Dairy dairyById = dairyMapper.getDairyById(uid);
        if (dairyById == null) {
            throw new CustomException("参数错误");
        }

        if (dairyById.getState().equals("0")) {
            if (RequestContext.get() == null) {
                throw new CustomException("暂无权限");
            }
            if (!dairyById.getCreateUserId().equals(RequestContext.get().getUser().getUid())) {
                throw new CustomException("暂无权限");
            }
        }


        SimplePropertyPreFilter filter = new SimplePropertyPreFilter("uid", "createTime", "title", "content",
                "tagId", "tagName", "likeCount", "commentCount", "preId", "preName", "nextId", "nextName", "open", "coverPic",
                "commentOpen", "state", "picId");
        Map<String, Object> res = JSONObject.parseObject(JSONObject.toJSONString(dairyById, filter));
        Long createTime = (Long) res.get("createTime");

        String datetime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(createTime));

        res.put("createTime", datetime);

        dairyMapper.addViewCount(uid);

        return res;
    }

    public Page<Dairy> listDairy(String admin, String q, String tagId, String state) throws CustomException {
        if (admin.equals("0")) {
            if (tagId.equals("")) {
                return dairyMapper.listDairy();
            } else {
                return dairyMapper.listTagDairy(tagId);
            }
        }
        RequestContext requestContext = RequestContext.get();
        if (requestContext == null) {
            throw new CustomException("暂无权限");
        }
        if (tagId.equals("")) {
            if (state.equals("")) {
                return dairyMapper.listBackendDairy(q);
            }
            return dairyMapper.listBackendStateDairy(q, state);
        } else {
            if (state.equals("")) {
                return dairyMapper.listBackendTagDairy(q, tagId);
            }
            return dairyMapper.listBackendStateTagDairy(q, tagId, state);
        }
    }

}
