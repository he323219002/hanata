package com.jimmy.controller.system;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jimmy.exception.CustomException;
import com.jimmy.service.system.ESService;
import com.jimmy.servlet.PassToken;
import com.jimmy.utils.HaResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author Jimmy He
 * @date 2020-06-17
 */

@RestController
@RequestMapping("/api")
public class ESSearchController {

    @Autowired
    ESService esService;

    @PostMapping("/search")
    @PassToken
    public HaResponse ESSearch(@RequestBody Map<String,Object> reqMap) throws IOException, CustomException {

        String keyword = (String) reqMap.getOrDefault("keyword", "");
        Integer page = Integer.valueOf((String) reqMap.getOrDefault("page", "1"));
        Integer size = Integer.valueOf((String) reqMap.getOrDefault("size", "20"));

//        PageHelper.startPage(Integer.valueOf(String.valueOf(page)),Integer.valueOf(String.valueOf(size)));
        Map<String, Object> res = esService.search(keyword, (page - 1) * size, size);
//        PageInfo<Map<String,String>> pageInfo = new PageInfo<>(resList);

        HaResponse response = new HaResponse();
        response.setData(res);
        return response;
    }


    @PostMapping("/visitor")
    @PassToken
    public HaResponse VisitorESSearch(@RequestBody Map<String,Object> reqMap) throws IOException {
        Integer page = Integer.valueOf((String) reqMap.getOrDefault("page", 1));
        Integer size = Integer.valueOf((String) reqMap.getOrDefault("size", 20));

        Map<String, Object> res = esService.visitor((page-1)*size,size);

        return new HaResponse(){
            {
                setData(res);
            }
        };
    }
}
