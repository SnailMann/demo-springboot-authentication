package com.snailmann.security.demo.execption;

import lombok.Getter;


public class UserNotExistException extends RuntimeException {

    @Getter
    String id;

    public UserNotExistException (String id){
        super("UserId not exist");
        this.id =  id;
    }



}
