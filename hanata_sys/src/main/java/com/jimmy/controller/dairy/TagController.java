package com.jimmy.controller.dairy;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jimmy.bean.Tag;
import com.jimmy.exception.AlreadyExistsException;
import com.jimmy.exception.CustomException;
import com.jimmy.service.dairy.TagService;
import com.jimmy.servlet.PassToken;
import com.jimmy.utils.HaResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Jimmy He
 * @date 2020-06-15
 */

@RestController
@RequestMapping("/api/tag")
public class TagController {
    @Autowired
    TagService tagService;

    @PostMapping("/new")
    public HaResponse addTag(@RequestBody Map<String,Object> reqMap) throws CustomException, AlreadyExistsException {
        String name = (String) reqMap.getOrDefault("name","");
        if(name.equals("")){
            throw new CustomException("参数必填");
        }

        Integer count = tagService.newTag(name);
        if(count==1){
            return new HaResponse();
        }else{
            throw new CustomException("参数错误");
        }
    }

    @PostMapping("/del")
    public HaResponse delTag(@RequestBody Map<String,Object> reqMap) throws CustomException {
        String uid = (String) reqMap.getOrDefault("id","");
        if(uid.equals("")){
            throw new CustomException("参数必填");
        }
        Integer count = tagService.delTag(uid);
        if(count==1){
            return new HaResponse();
        }else{
            throw new CustomException("参数错误");
        }
    }

    @PassToken
    @PostMapping("/update")
    public HaResponse updateCategory(@RequestBody Map<String,Object> reqMap) throws CustomException, AlreadyExistsException {
        String uid = (String) reqMap.getOrDefault("id","");
        String name = (String) reqMap.getOrDefault("name","");

        if(uid.equals("")||name.equals("")){
            throw new CustomException("参数必填");
        }

        Integer count = tagService.updateTag(uid, name);
        if(count==1){
            return new HaResponse();
        }else{
            throw new CustomException("参数错误");
        }
    }

    @PostMapping(path = "")
    @PassToken
    public HaResponse listTag(@RequestBody Map<String,Object> qMap){
        String limit = (String) qMap.getOrDefault("limit", "1");
        String offset = (String) qMap.getOrDefault("offset", "10");
        String q = (String) qMap.getOrDefault("q", "");

//        PageHelper.startPage(Integer.valueOf(limit),Integer.valueOf(offset));
        Page<Tag> tags = tagService.listTag(q);


//        PageInfo<Tag> pageInfo = new PageInfo<>(tags);

        HaResponse response = new HaResponse();
        response.setData(tags);

        return response;
    }
}
