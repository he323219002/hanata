package com.jimmy.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Jimmy He
 * @date 2020-06-09
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArticleLabel extends Model implements Serializable {
    private String labelId;
    private String labelName;
    private String articleId;
    private String title;

}
