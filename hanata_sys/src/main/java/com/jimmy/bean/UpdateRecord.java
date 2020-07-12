package com.jimmy.bean;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Jimmy He
 * @date 2020-06-20
 */

@Data
public class UpdateRecord extends Model implements Serializable {
    private String version;
    private String content;
}
