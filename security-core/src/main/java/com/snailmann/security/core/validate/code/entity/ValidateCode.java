package com.snailmann.security.core.validate.code.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.awt.image.BufferedImage;
import java.time.LocalDateTime;

/**
 * 验证验证码   //这里默认作为短信验证的接收类，同时也是一个基类
 */
@Data
@AllArgsConstructor
public class ValidateCode {
    //Code码
    private String code;
    //过期时间
    private LocalDateTime expireTime;


    /**
     * @param code
     * @param expireInt 多少秒后过期，Second为单位
     */
    public ValidateCode(String code, int expireInt) {
        this.code = code;
        this.expireTime = LocalDateTime.now().plusSeconds(expireInt);
    }

    /**
     * 判断验证码是否过期
     *
     * @return
     */
    public boolean isExpried() {
        //当前时间是否已经超过了预期过期时间
        return LocalDateTime.now().isAfter(expireTime);
    }
}
