package com.jimmy.controller.system;

import com.jimmy.exception.AlreadyExistsException;
import com.jimmy.exception.CustomException;
import com.jimmy.service.system.SubscribeService;
import com.jimmy.utils.HaResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author Jimmy He
 * @date 2020-06-18
 */

@RestController
@RequestMapping("/api")
public class SubscribeController {

    @Autowired
    SubscribeService subscribeService;

    @PostMapping("/subscribe")
    public HaResponse subscirbe() throws AlreadyExistsException {
        subscribeService.subscribe();
        return new HaResponse();
    }

    @PostMapping("/subscribe/cancel")
    public HaResponse cancelSub(@RequestBody Map<String,Object> reqMap) throws CustomException {
        String userId = (String) reqMap.getOrDefault("id", "");
        subscribeService.cancelSub(userId);
        return new HaResponse();
    }

}
