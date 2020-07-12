package com.jimmy.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Jimmy He
 * @date 2020-06-13
 */

@Data
public class Message extends Model{
    private String title;
    private String content;
    private String isRead;
    private String userId;
    private String userName;
    private String avatar;
}
