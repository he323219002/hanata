package com.jimmy.controller.blog;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;
import com.alibaba.fastjson.support.spring.annotation.ResponseJSONP;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jimmy.bean.Label;
import com.jimmy.exception.AlreadyExistsException;
import com.jimmy.exception.CustomException;
import com.jimmy.service.blog.LabelService;
import com.jimmy.servlet.PassToken;
import com.jimmy.utils.HaResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * @author Jimmy He
 * @date 2020-06-09
 */

@RestController
@RequestMapping("/api/label")
public class LabelController {

    @Autowired
    LabelService labelService;

    @PostMapping("/new")
    public HaResponse addLabel(@RequestBody Map<String,String> reqMap) throws CustomException, AlreadyExistsException {
        String name = reqMap.getOrDefault("name","");


        Integer count = labelService.addLabel(name);
        if(count==1){
            return new HaResponse();
        }else{
            throw new CustomException("参数错误");
        }
    }

    @PostMapping("/del")
    public HaResponse delLabel(@RequestBody Map<String,String> reqMap) throws CustomException {
        String uid = reqMap.getOrDefault("id","");

        Integer count = labelService.delLabel(uid);
        if(count==1){
            return new HaResponse();
        }else{
            throw new CustomException("参数错误");
        }
    }

    @PostMapping("/update")
    public HaResponse updateLabel(@RequestBody Map<String,String> reqMap) throws CustomException, AlreadyExistsException {
        String uid = reqMap.getOrDefault("id","");
        String name = reqMap.getOrDefault("name","");

        Integer count = labelService.updateLable(uid, name);
        if(count==1){
            return new HaResponse();
        }else{
            throw new CustomException("参数错误");
        }
    }


    @PassToken
    @PostMapping(path = "")
    public HaResponse listLabel(@RequestBody Map<String,Object> qMap){
        String limit = (String)qMap.getOrDefault("limit", "1");
        String offset = (String)qMap.getOrDefault("offset", "10");
        String q = (String) qMap.getOrDefault("q","");

//        PageHelper.startPage(Integer.valueOf(limit),Integer.valueOf(offset));
        Page<Label> labels = labelService.listLabel(q);


//        PageInfo<Label> pageInfo = new PageInfo<>(labels);

        HaResponse response = new HaResponse();
        response.setData(labels);

        return response;
    }

}
