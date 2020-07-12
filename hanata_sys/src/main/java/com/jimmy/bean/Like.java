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
@AllArgsConstructor
@NoArgsConstructor
public class Like extends Model implements Serializable {
    private String userName;
    private String userAvatar;
    private String articleId;
    private String title;
}
