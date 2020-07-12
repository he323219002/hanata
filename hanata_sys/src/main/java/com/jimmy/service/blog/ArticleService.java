package com.jimmy.service.blog;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;
import com.github.pagehelper.Page;
import com.jimmy.bean.*;
import com.jimmy.exception.AlreadyExistsException;
import com.jimmy.exception.CustomException;
import com.jimmy.mapper.blog.ArticleLabelMapper;
import com.jimmy.mapper.blog.ArticleMapper;
import com.jimmy.mapper.blog.CategoryMapper;
import com.jimmy.mapper.blog.LabelMapper;
import com.jimmy.service.system.ESService;
import com.jimmy.service.system.MailSendService;
import com.jimmy.servlet.PassToken;
import com.jimmy.servlet.RequestContext;
import com.jimmy.utils.CommonUtils;
import com.jimmy.utils.SnowFlake;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.mail.MessagingException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Jimmy He
 * @date 2020-06-11
 */

@Service
@Transactional
public class ArticleService {
    @Autowired
    ArticleMapper articleMapper;

    @Autowired
    LabelMapper labelMapper;

    @Autowired
    ArticleLabelMapper articleLabelMapper;

    @Autowired
    CategoryMapper categoryMapper;

    @Autowired
    LabelService labelService;

    @Autowired
    SnowFlake mySnowFlake;

    @Autowired
    ESService esService;

    @Autowired
    MailSendService mailSendService;

    @Autowired
    @Qualifier("bCryptPasswordEncoder")
    BCryptPasswordEncoder encoder;


    public Integer newArticle(String title, String content, String categoryId, List<String> labelList, String open, String commentOpen,String state)
            throws CustomException, AlreadyExistsException, IOException, MessagingException {
        User user = RequestContext.get().getUser();
        if (!user.getRole().equals("0")) {
            throw new CustomException("权限不足");
        }

        if (title.equals("")){
            throw new CustomException("标题不能为空");
        }

        if(content.equals("")){
            throw new CustomException("请输入内容");
        }

        List<Article> articleByTitle = articleMapper.getArticleByTitle(title);

        if (articleByTitle.size() != 0) {
            throw new AlreadyExistsException("名称已存在");
        }



        String categoryName = "";
        if (!categoryId.equals("")) {
            Category categoryById = categoryMapper.getCategoryById(categoryId);
            if (categoryById == null) {
                throw new CustomException("参数错误1");
            }
            categoryName = categoryById.getName();
        }


        if(labelList.size()!=0){
            Integer labelByIdList = labelService.getLabelByIdList(labelList);
            if (labelList.size() != labelByIdList) {
                throw new CustomException("参数错误2");
            }
        }


        // set prepage and next page
        Article lastArticle = articleMapper.findLastArticle();
        String preId = "";
        String preName = "";

        Date datetime = Calendar.getInstance().getTime();
        String uid = String.valueOf(mySnowFlake.nextId());
        String userId = user.getUid();

        if (lastArticle != null) {
            articleMapper.changeNext(lastArticle.getUid(), uid, title);
            preId = lastArticle.getUid();
            preName = lastArticle.getTitle();
        }

        // generate map for batch insert
        if (labelList.size() != 0) {
            List<Map> batchList = new ArrayList<>();
            for (String labelId : labelList) {

                Date now = Calendar.getInstance().getTime();
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String dateString = formatter.format(now);

                Label label = labelMapper.getLabelById(labelId);
                Map<String, String> batchMap = new HashMap<>();
                batchMap.put("uid", String.valueOf(mySnowFlake.nextId()));
                batchMap.put("datetime", dateString);
                batchMap.put("userId", userId);
                batchMap.put("articleId", uid);
                batchMap.put("title", title);
                batchMap.put("labelId", labelId);
                batchMap.put("labelName", label.getName());
                batchList.add(batchMap);
            }

            articleLabelMapper.batchInsert(batchList);
            String SIdList = StringUtils.strip(labelList.toString(), "[]");
            labelMapper.addLabelCount(SIdList);
        }


        categoryMapper.addCategoryCount(categoryId);

        // add es data
        ESModel esModel = new ESModel(uid, datetime, title, content, "0",open,state,"1");
        esService.newDocument(esModel);

        // send to subscribe
        int length = content.length();

        String precontent = length>30?content.substring(0,30):content;



        mailSendService.sendMail(title,precontent,uid);



        return articleMapper.newArticle(uid, datetime, userId, title, content, categoryId, categoryName, preId, preName, open, commentOpen,state);

    }


    public Integer delArticle(String uid) throws CustomException, IOException {
        User user = RequestContext.get().getUser();
        if (!user.getRole().equals("0")) {
            throw new CustomException("权限不足");
        }

        Article articleById = articleMapper.getArticleById(uid);
        if (articleById == null) {
            throw new CustomException("参数错误");
        }

        Date datetime = Calendar.getInstance().getTime();
        String userId = user.getUid();

        // set prepage and next page
        String preId = articleById.getPreId();
        String nextId = articleById.getNextId();

        Article preArticle = articleMapper.getArticleById(preId);
        Article nextArticle = articleMapper.getArticleById(nextId);

        //first article
        if (preId.equals("")) {
            if (!nextId.equals("")) {
                articleMapper.changePre(nextId, "", "");
            }
        }
        // middle article
        else if (!nextId.equals("")) {
            articleMapper.changeNext(preArticle.getUid(), articleById.getNextId(), articleById.getNextName());
            articleMapper.changePre(nextArticle.getUid(), articleById.getPreId(), articleById.getPreName());
        }
        // last
        else {
            articleMapper.changeNext(preArticle.getUid(), "", "");
        }


        // label count - 1
        List<String> labelIdList = articleLabelMapper.getLabelIdByArticleID(uid);
        if (labelIdList.size()!=0){
            String SIdList = StringUtils.strip(labelIdList.toString(), "[]");
            labelMapper.subLabelCount(SIdList);
        }


        // del foreign key relation
        articleLabelMapper.delArticle(uid, datetime, userId);

        // category count - 1
        String categoryId = articleById.getCategoryId();
        if (categoryId!=null) {
            categoryMapper.subCategoryCount(categoryId);
        }


        ESModel esModel = new ESModel(uid, datetime, articleById.getTitle(), articleById.getContent(), "1",articleById.getOpen(),articleById.getState(),"1");
        esService.updateDocument(uid,esModel);
        return articleMapper.delArticle(uid, datetime, userId);
    }



    public Integer updateArticle(String uid, String title, String content, String categoryId, String open, String commentOpen, List<String> delALList, List<String> labelIdList,String state)
            throws CustomException, AlreadyExistsException, IOException {
        User user = RequestContext.get().getUser();
        if (!user.getRole().equals("0")) {
            throw new CustomException("权限不足");
        }

        Article articleById = articleMapper.getArticleById(uid);
        if (articleById == null) {
            throw new CustomException("参数错误");
        }

        if (title.equals("")){
            throw new CustomException("标题不能为空");
        }

        if(content.equals("")){
            throw new CustomException("请输入内容");
        }

        List<Article> articles = articleMapper.checkArticleByTitle(uid,title);
        if(articles.size()!=0){
            throw new AlreadyExistsException("名称已存在");
        }

        Date datetime = Calendar.getInstance().getTime();
        String userId = user.getUid();

        String categoryName = "";

        if (!categoryId.equals("")) {
            String preCategoryId = articleById.getCategoryId();
            if (!categoryId.equals(preCategoryId)) {
                Category categoryById = categoryMapper.getCategoryById(categoryId);
                if (categoryById == null) {
                    throw new CustomException("参数错误");
                }
                categoryName = categoryById.getName();
                categoryMapper.addCategoryCount(categoryId);
                categoryMapper.subCategoryCount(preCategoryId);
            }
        }

        // delete
//        ArrayList<String> preALId = (ArrayList<String>) articleLabelMapper.getALIdByArticleId(uid);
        if(delALList.size()!=0){
            String SIdList = StringUtils.strip(delALList.toString(), "[]");
            Integer delCount = articleLabelMapper.checkCount(SIdList, uid, userId);
            if (delCount != delALList.size()) {
                throw new CustomException("参数错误");
            }
            articleLabelMapper.delAL(SIdList, datetime, userId);

            // -- sub label count
            articleLabelMapper.updateLabelCount(SIdList);
        }


        // new
        if (labelIdList.size()>0){
            List<Map> batchList = new ArrayList<>();
            String newLabelIdList = StringUtils.strip(labelIdList.toString(), "[]");

            Integer integer = articleLabelMapper.checkLabelExist(uid, newLabelIdList);
            if (integer!=0){
                throw new CustomException("请勿重复添加标签");
            }

            for (String labelId : labelIdList) {
                // [{labelid:xxxx}...]
                Date now = Calendar.getInstance().getTime();
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String dateString = formatter.format(now);

                Label label = labelMapper.getLabelById(labelId);
                Map<String, String> batchMap = new HashMap<>();
                batchMap.put("uid", String.valueOf(mySnowFlake.nextId()));
                batchMap.put("datetime", dateString);
                batchMap.put("userId", userId);
                batchMap.put("articleId", uid);
                batchMap.put("title", title);
                batchMap.put("labelId", labelId);
                batchMap.put("labelName", label.getName());
                batchList.add(batchMap);
            }
            articleLabelMapper.batchInsert(batchList);
            String SLabelIdList = StringUtils.strip(labelIdList.toString(), "[]");
            labelMapper.addLabelCount(SLabelIdList);
        }

        // update pre and next
        String preId = articleById.getPreId();
        if(!preId.equals("")){
            articleMapper.changeNext(preId,uid,title);
        }
        String nextId = articleById.getNextId();
        if(!nextId.equals("")){
            articleMapper.changePre(nextId,uid,title);
        }

        // update exists
        articleLabelMapper.updateALTitle(userId, datetime, title, uid);

        ESModel esModel = new ESModel(uid, datetime, title, content, "0",open,state,"1");
        esService.updateDocument(uid,esModel);

        Integer count = articleMapper.updateArticle(uid, title, content, categoryId, categoryName, open, commentOpen, datetime, userId,state);

        return count;
    }



    public Map showArticle(String uid) throws CustomException {
        Article articleById = articleMapper.getArticleById(uid);
        if (articleById == null) {
            throw new CustomException("参数错误");
        }
        if (articleById.getState().equals("0")){
            if (RequestContext.get()==null){
                throw new CustomException("暂无权限");

            }
            if (!articleById.getCreateUserId().equals(RequestContext.get().getUser().getUid())){
                throw new CustomException("暂无权限");
            }
        }

        SimplePropertyPreFilter filter = new SimplePropertyPreFilter("uid", /*"createTime",*/ "title", "content",
                "categoryId", "categoryName", "likeCount", "commentCount", "preId", "preName", "nextId", "nextName", "open","state",
                "commentOpen","createTime");
        Map<String, Object> res = JSONObject.parseObject(JSONObject.toJSONString(articleById, filter));
//        Date createTime = (Date) res.get("createTime");
//        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        String dateString = formatter.format(createTime);
//
//        res.put("createTime",dateString);

        List<ArticleLabel> alByArticleId = articleLabelMapper.getALByArticleId(uid);
        List<Map> labelList = new ArrayList<>();
        for (ArticleLabel articleLabel : alByArticleId) {
            HashMap<String, Object> labelMap = new HashMap<>();
            labelMap.put("uid",articleLabel.getUid());
            labelMap.put("labelName",articleLabel.getLabelName());
            labelList.add(labelMap);
        }
        res.put("label",labelList);

        articleMapper.addViewCount(uid);

        return res;
    }


    public Page<Article> listArticle(String admin,String field,String categoryId,String labelId,String state) throws CustomException {
        // judge if admin 0 no 1 yes
        if(admin.equals("0")){
            if (categoryId.equals("")&&labelId.equals("")){
                return articleMapper.listArticle();
            }
            else if (!categoryId.equals("")){
                return articleMapper.listCategoryArticle(categoryId);
            }
            else {
                return articleMapper.listLabelArticle(labelId);
            }

        }
        RequestContext requestContext = RequestContext.get();
        if (requestContext==null){
            throw new CustomException("暂无权限");
        }
        if (categoryId.equals("")&&labelId.equals("")){
            if (state.equals("")){
                return articleMapper.listBackendArticle(field);
            }
            return articleMapper.listBackendStateArticle(field,state);
        }
        else if (!categoryId.equals("")){
            if (state.equals("")){
                return articleMapper.listBackendCategoryArticle(field, categoryId);
            }
            return articleMapper.listBackendStateCategoryArticle(field,categoryId,state);
        }
        else {
            if (state.equals("")){
                return articleMapper.listBackendLabelArticle(labelId,field);
            }
            return articleMapper.listBackendStateLabelArticle(labelId,field,state);
        }
    }


    public Article getArticleById(String uid) throws CustomException {

        Article articleById = articleMapper.getArticleById(uid);
        if(articleById==null){
            throw new CustomException("参数错误");
        }
        return articleById;
    }

    public Dairy getDairyById(String uid) throws CustomException {
        Dairy dairyById = articleMapper.getDairyById(uid);
        if (dairyById==null){
            throw new CustomException("参数错误");
        }
        return dairyById;
    }

}
