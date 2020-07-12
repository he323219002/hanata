package com.jimmy.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author Jimmy He
 * @date 2020-06-13
 */

@Data
@NoArgsConstructor
public class Comment extends Model implements Serializable {
    private String receiverId;
    private String articleId;
    private String title;
    private String content;
    private String fartherId;
    private String fartherContent;
    private String userName;
    private String userAvatar;
}
