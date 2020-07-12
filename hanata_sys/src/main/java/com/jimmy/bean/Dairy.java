package com.jimmy.bean;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.Serializable;

/**
 * @author Jimmy He
 * @date 2020-06-15
 */

@Data
public class Dairy extends Model implements Serializable {
    private String title;

    private String content;

    @JsonIgnore
    private String tagId;
    private String tagName;
    private Integer likeCount;
    private Integer commentCount;
    private String state;

    @JsonIgnore
    private String preId;

    private String preName;

    @JsonIgnore
    private String nextId;
    private String nextName;
    private String open;
    private String commentOpen;
    private String coverPic;
    private String mp3Url;
    private String videoUrl;
    private Integer viewCount;
    private String picId;
}
