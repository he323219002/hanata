package com.jimmy.bean;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Jimmy He
 * @date 2020-06-16
 */
@Data
@AllArgsConstructor
public class ESModel implements Serializable {
    private String uid;
    private Date createTime;
    private String title;
    private String content;
    private String deleted;
    private String open;
    private String state;
    // 1article 2dairy
    private String kind;
}
