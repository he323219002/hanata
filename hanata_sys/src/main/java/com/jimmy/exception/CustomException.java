package com.jimmy.exception;

import java.io.Serializable;

/**
 * @author Jimmy He
 * @date 2020-06-06
 */
public class CustomException extends Exception implements Serializable {
    public CustomException(String e){
        super(e);
    }
}
