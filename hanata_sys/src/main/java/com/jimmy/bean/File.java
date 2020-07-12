package com.jimmy.bean;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Jimmy He
 * @date 2020-06-15
 */

@Data
public class File extends Model implements Serializable {
    private String filePath;
    private String fileName;
    private String contentType;
}
