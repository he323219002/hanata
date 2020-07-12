package com.jimmy.bean.redis;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;

/**
 * @author Jimmy He
 * @date 2020-06-18
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubModel implements Serializable {
    private String mail;
    private String nickname;
}
