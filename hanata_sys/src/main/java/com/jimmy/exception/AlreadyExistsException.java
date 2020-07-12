package com.jimmy.exception;

import java.io.Serializable;

/**
 * @author Jimmy He
 * @date 2020-06-06
 */


public class AlreadyExistsException extends Exception implements Serializable {
    public AlreadyExistsException(String e){
        super(e);
    }
}
