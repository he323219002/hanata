package com.jimmy.bean;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author Jimmy He
 * @date 2020-06-05
 */


@Data
public class Model {
    protected String uid;
    @JsonFormat(pattern ="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    protected Date createTime;
    @JsonFormat(pattern ="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    protected Date updateTime;
    @JsonIgnore
    protected String createUserId;
    @JsonIgnore
    protected String updateUserId;
    @JsonIgnore
    protected String deleted;
}
