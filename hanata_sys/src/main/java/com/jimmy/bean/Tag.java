package com.jimmy.bean;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Jimmy He
 * @date 2020-06-15
 */

@Data
public class Tag extends Model implements Serializable {
    private String name;
    private Integer count;
}
