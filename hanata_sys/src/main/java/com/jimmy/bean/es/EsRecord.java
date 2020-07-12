package com.jimmy.bean.es;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Jimmy He
 * @date 2020-06-19
 */

@Data
@AllArgsConstructor
public class EsRecord implements Serializable {
    private String uid;
    private Date createTime;
    private String IP;
    private String url;

}
