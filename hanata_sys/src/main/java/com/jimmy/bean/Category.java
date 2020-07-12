package com.jimmy.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author Jimmy He
 * @date 2020-06-11
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Category extends Model implements Serializable {
    private String name;
    private Integer count;
}
