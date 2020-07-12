package com.jimmy.bean;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author Jimmy He
 * @date 2020-06-07
 */


@Data
@NoArgsConstructor
@AllArgsConstructor
public class Article extends Model implements Serializable {

    private String title;

    private String content;

    private String categoryId;

    private String categoryName;

    private Integer likeCount;

    private Integer commentCount;

    private String state;


    @JsonIgnore
    private String preId;

    @JsonIgnore
    private String preName;

    @JsonIgnore
    private String nextId;

    @JsonIgnore
    private String nextName;

    @JsonIgnore
    private String open;

    @JsonIgnore
    private String commentOpen;

    private Integer viewCount;
}
