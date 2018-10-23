package com.snailmann.security.core.properties;


import lombok.Data;

@Data
public class SmsCodeProperties {

    //验证码数字个数
    private int length = 6;
    //过期时间,单位seconds
    private int expireInt = 60;
    //需要图形验证的url
    private String url;


}

