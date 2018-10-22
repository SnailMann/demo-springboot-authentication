package com.snailmann.security.core.config.properties;


import lombok.Data;

@Data
public class ImageCodeProperties {

    //验证码宽度
    private int width = 67;
    //验证码高度
    private int height = 23;
    //验证码数字个数
    private int length = 4;
    //过期时间,单位seconds
    private int expireInt = 60;

    //需要图形验证的url
    private String url;


}

