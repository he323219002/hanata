package com.jimmy.controller.blog;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jimmy.bean.Category;
import com.jimmy.bean.Label;
import com.jimmy.exception.AlreadyExistsException;
import com.jimmy.exception.CustomException;
import com.jimmy.service.blog.CategoryService;
import com.jimmy.service.blog.LabelService;
import com.jimmy.servlet.PassToken;
import com.jimmy.utils.HaResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Jimmy He
 * @date 2020-06-11
 */

@RestController
@RequestMapping("/api/category")
public class CategoryController {

    @Autowired
    CategoryService categoryService;

    @PostMapping("/new")
    public HaResponse addCategory(@RequestBody Map<String,Object> reqMap) throws CustomException, AlreadyExistsException {
        String name = (String) reqMap.getOrDefault("name","");


        Integer count = categoryService.newCategory(name);
        if(count==1){
            return new HaResponse();
        }else{
            throw new CustomException("参数错误");
        }
    }

    @PostMapping("/del")
    public HaResponse delCategory(@RequestBody Map<String,Object> reqMap) throws CustomException {
        String uid = (String) reqMap.getOrDefault("id","");

        Integer count = categoryService.delCategory(uid);
        if(count==1){
            return new HaResponse();
        }else{
            throw new CustomException("参数错误");
        }
    }

    @PostMapping("/update")
    public HaResponse updateCategory(@RequestBody Map<String,String> reqMap) throws CustomException, AlreadyExistsException {
        String uid = reqMap.getOrDefault("id","");
        String name = reqMap.getOrDefault("name","");


        Integer count = categoryService.updateCategory(uid, name);
        if(count==1){
            return new HaResponse();
        }else{
            throw new CustomException("参数错误");
        }
    }



    @PassToken
    @PostMapping(path = "")
    public HaResponse listCategory(@RequestBody Map<String,Object> qMap){
        String limit = (String) qMap.getOrDefault("limit", "1");
        String offset = (String) qMap.getOrDefault("offset", "10");
        String q = (String) qMap.getOrDefault("q","");


//        PageHelper.startPage(Integer.valueOf(limit),Integer.valueOf(offset));
        Page<Category> categories = categoryService.listCategory(q);

//        PageInfo<Category> pageInfo = new PageInfo<>(categories);

        HaResponse response = new HaResponse();
        response.setData(categories);

        return response;
    }


}
