package com.usermanagement.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class GeneralExceptionWithMessage extends  Exception{

    public GeneralExceptionWithMessage(String message) {
        super(message);
    }
}
