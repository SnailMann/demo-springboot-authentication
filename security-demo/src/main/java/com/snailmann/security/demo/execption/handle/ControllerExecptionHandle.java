package com.snailmann.security.demo.execption.handle;

import com.snailmann.security.demo.execption.UserNotExistException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ControllerExecptionHandle {

    @ExceptionHandler(UserNotExistException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String,Object> handleUserNotExistException(UserNotExistException ex){
        Map<String,Object> result = new HashMap<>();
        result.put("status",HttpStatus.INTERNAL_SERVER_ERROR.value());
        result.put("LocalizedMessage",ex.getLocalizedMessage());
        result.put("class",ex.getClass());
        result.put("userId",ex.getId());
        result.put("message",ex.getMessage());
        return result;

    }
}
