package com.jimmy.service.blog;

import com.github.pagehelper.Page;
import com.jimmy.bean.Category;

import com.jimmy.bean.User;
import com.jimmy.exception.AlreadyExistsException;
import com.jimmy.exception.CustomException;
import com.jimmy.mapper.blog.ArticleMapper;
import com.jimmy.mapper.blog.CategoryMapper;

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
 * @date 2020-06-11
 */

@Service
public class CategoryService {
    @Autowired
    CategoryMapper categoryMapper;

    @Autowired
    ArticleMapper articleMapper;

    @Autowired
    SnowFlake mySnowFlake;

    @Autowired
    @Qualifier("bCryptPasswordEncoder")
    BCryptPasswordEncoder encoder;


    public Integer newCategory(String name) throws CustomException, AlreadyExistsException {
        if (name.equals("")){
            throw new CustomException("参数必填");
        }

        User user = RequestContext.get().getUser();
        if (!user.getRole().equals("0")) {
            throw new CustomException("权限不足");
        }

        List<Category> categoryByName = categoryMapper.getCategoryByName(name);
        if (categoryByName.size() != 0) {
            throw new AlreadyExistsException("名称已存在");
        }

        Date datetime = Calendar.getInstance().getTime();
        String uid = String.valueOf(mySnowFlake.nextId());
        String userId = user.getUid();
        return categoryMapper.newCategory(uid, datetime, userId, name);
    }

    public Integer updateCategory(String uid, String name) throws CustomException, AlreadyExistsException {
        if (uid.equals("")||name.equals("")){
            throw new CustomException("参数必填");
        }

        User user = RequestContext.get().getUser();
        if (!user.getRole().equals("0")) {
            throw new CustomException("权限不足");
        }

        Category categoryById = categoryMapper.getCategoryById(uid);
        if (categoryById == null) {
            throw new CustomException("参数错误");
        }

        List<Category> categories = categoryMapper.checkCategoryByName(uid, name);
        if (categories.size() != 0) {
            throw new AlreadyExistsException("名称已存在");
        }

        Date datetime = Calendar.getInstance().getTime();
        String userId = user.getUid();
        return categoryMapper.updateCategory(uid, name, datetime, userId);
    }


    public Integer delCategory(String uid) throws CustomException {

        if (uid.equals("")){
            throw new CustomException("参数必填");
        }

        Category categoryById = categoryMapper.getCategoryById(uid);
        if (categoryById == null) {
            throw new CustomException("参数错误");
        }

        User user = RequestContext.get().getUser();
        if (!user.getRole().equals("0")) {
            throw new CustomException("权限不足");
        }

        Date datetime = Calendar.getInstance().getTime();
        String userId = user.getUid();

        articleMapper.clearCategory(uid);

        return categoryMapper.delCategory(uid, datetime, userId);
    }


    public Page<Category> listCategory(String field) {
        return categoryMapper.listCategory(field);
    }

}
