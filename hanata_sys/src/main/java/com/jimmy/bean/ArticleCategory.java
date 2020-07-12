package com.jimmy.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author Jimmy He
 * @date 2020-06-09
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticleCategory extends Model implements Serializable {
    private String name;
    private Integer count;
}
