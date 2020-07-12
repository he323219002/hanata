package com.jimmy.controller.dairy;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jimmy.bean.Article;
import com.jimmy.bean.Dairy;
import com.jimmy.exception.AlreadyExistsException;
import com.jimmy.exception.CustomException;
import com.jimmy.service.dairy.DairyService;
import com.jimmy.servlet.PassToken;
import com.jimmy.utils.CommonUtils;
import com.jimmy.utils.HaResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Jimmy He
 * @date 2020-06-15
 */

@RestController
@RequestMapping("/api/dairy")
public class DairyController {
    @Autowired
    DairyService dairyService;

    @PostMapping("/new")
    public HaResponse newArticle(@RequestBody Map<String, Object> reqMap) {

        HaResponse response = new HaResponse();
        try {
            String title = (String) reqMap.getOrDefault("title", "");
            String content = (String) reqMap.getOrDefault("content", "");
            String tagId = (String) reqMap.getOrDefault("tid", "");
            String open = (String) reqMap.getOrDefault("open", "1");
            String commentOpen = (String) reqMap.getOrDefault("commentOpen", "1");
            String picId = (String) reqMap.getOrDefault("picId", "");
            String state = (String) reqMap.getOrDefault("state", "1");

            dairyService.newDairy(title, content, tagId, open, commentOpen, picId, state);
            return response;

        } catch (CustomException | AlreadyExistsException | IOException e) {
            response.setData(e.getCause());
            response.setMsg(e.getMessage());
            System.out.println(e);
            response.setCode(1);
            return response;
        }
    }

    @PostMapping("/del")
    public HaResponse delDairy(@RequestBody Map<String, Object> reqMap) throws CustomException, IOException {
        String dairyId = (String) reqMap.get("id");
        Integer count = dairyService.delDairy(dairyId);
        if (count == 1) {
            return new HaResponse();
        }
        return new HaResponse(1, "删除失败");

    }

    @PostMapping("/update")
    public HaResponse updateDairy(@RequestBody Map<String, Object> reqMap) throws CustomException, AlreadyExistsException {
        String uid = (String) reqMap.getOrDefault("id", "");
        String title = (String) reqMap.getOrDefault("title", "");
        String content = (String) reqMap.getOrDefault("content", "");
        String categoryId = (String) reqMap.getOrDefault("categoryId", "");
        String open = (String) reqMap.getOrDefault("open", "1");
        String commentOpen = (String) reqMap.getOrDefault("commentOpen", "1");
        String picId = (String) reqMap.getOrDefault("picId", "");
        String state = (String) reqMap.getOrDefault("state", "1");
        Integer integer = dairyService.updateDairy(uid, title, content, categoryId, open, commentOpen, picId, state);
        if (integer == 1) {
            return new HaResponse();
        }
        return new HaResponse(1, "添加失败,可能重复");
    }

    @PassToken
    @PostMapping("/show")
    public HaResponse showDairy(@RequestBody Map<String, Object> reqMap) throws CustomException {
        String uid = (String) reqMap.getOrDefault("id", "");
        Map map = dairyService.showDairy(uid);
        HaResponse response = new HaResponse();
        response.setData(map);
        return response;
    }

    @PassToken
    @PostMapping("")
    public HaResponse ListDairy(@RequestBody Map<String, Object> qMap) throws CustomException {
        String limit = (String) qMap.getOrDefault("limit", "1");
        String offset = (String) qMap.getOrDefault("offset", "10");
        String q = (String) qMap.getOrDefault("q", "");
        String tid = (String) qMap.getOrDefault("tid", "");
        String admin = (String) qMap.getOrDefault("admin", "0");
        String state = (String) qMap.getOrDefault("state", "");


        PageHelper.startPage(Integer.valueOf(limit), Integer.valueOf(offset));
        Page<Dairy> dairies = dairyService.listDairy(admin, q, tid, state);

        for (Dairy dairy : dairies) {
            String content = dairy.getContent();
            if (content.length() > 200) {
                String newContent = content.substring(0, 200) + "...";
                dairy.setContent(newContent);
            }
        }

        PageInfo<Dairy> pageInfo = new PageInfo<>(dairies);

        HaResponse response = new HaResponse();
        response.setData(pageInfo);

        return response;
    }
}
