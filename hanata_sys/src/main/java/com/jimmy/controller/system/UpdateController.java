package com.jimmy.controller.system;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jimmy.bean.Article;
import com.jimmy.bean.UpdateRecord;
import com.jimmy.exception.CustomException;
import com.jimmy.service.system.UpdateService;
import com.jimmy.servlet.PassToken;
import com.jimmy.utils.HaResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author Jimmy He
 * @date 2020-06-20
 */

@RestController
@RequestMapping("/api/update")
public class UpdateController {

    @Autowired
    UpdateService updateService;

    @PostMapping("/new")
    public HaResponse newUpdate(@RequestBody Map<String, Object> reqMap) {
        String version = (String) reqMap.getOrDefault("version", "");
        String content = (String) reqMap.getOrDefault("content", "");

        try{
            updateService.newUpdateRecord(version,content);
            return new HaResponse();
        }catch (Exception e){
            return new HaResponse(1,e.getMessage());
        }

    }

    @PostMapping("/del")
    public HaResponse delUpdate(@RequestBody Map<String, Object> reqMap){
        String id = (String) reqMap.getOrDefault("id", "");

        try{
            updateService.delUpdateRecord(id);
            return new HaResponse();
        } catch (CustomException e) {
            return new HaResponse(1,e.getMessage());
        }
    }


    @PostMapping("/update")
    public HaResponse updateRecord(@RequestBody Map<String, Object> reqMap){
        String id = (String) reqMap.getOrDefault("id", "");
        String version = (String) reqMap.getOrDefault("version", "");
        String content = (String) reqMap.getOrDefault("content", "");

        try{
            updateService.updateUpdateRecord(id,version,content);
            return new HaResponse();

        } catch (CustomException e) {
            e.printStackTrace();
            return new HaResponse(1,e.getMessage());
        }
    }

    @PassToken
    @PostMapping("")
    public HaResponse ListUpdate(@RequestBody Map<String,Object> qMap){
        String limit = (String)qMap.getOrDefault("limit", "1");
        String offset = (String) qMap.getOrDefault("offset", "10");


        PageHelper.startPage(Integer.valueOf(limit),Integer.valueOf(offset));
        Page<UpdateRecord> updateRecords = updateService.listUpdate();
        PageInfo<UpdateRecord> pageInfo = new PageInfo<>(updateRecords);

        HaResponse response = new HaResponse();
        response.setData(pageInfo);

        return response;
    }

}
