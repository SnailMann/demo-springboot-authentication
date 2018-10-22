package com.snailmann.security.core.config.properties;


import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ImageCodeProperties extends SmsCodeProperties {


    /**
     * 图形验证码长度默认为4，而短信默认为6
     */
    public ImageCodeProperties() {
        setLength(4);
    }

    //验证码宽度
    private int width = 67;
    //验证码高度
    private int height = 23;



}

