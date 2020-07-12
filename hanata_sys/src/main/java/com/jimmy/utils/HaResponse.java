package com.jimmy.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * @author Jimmy He
 * @date 2020-06-05
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HaResponse {
    private Integer code = 0;
    private Object data;
    private String msg = "success";


    // invalid request
    public HaResponse(Integer code,String msg) {
        this.code = code;
        this.msg = msg;
    }

    // valid request and get data
    public HaResponse(Map<String,Object> data) {
        this.code = 0;
        this.data = data;
        this.msg = "success";
    }
}
