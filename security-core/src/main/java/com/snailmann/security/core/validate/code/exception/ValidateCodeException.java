package com.snailmann.security.core.validate.code.exception;


import org.springframework.security.core.AuthenticationException;

/**
 * AuthenticationException是SpringSecurity提供的登陆验证出现异常的基类
 */
public class ValidateCodeException extends AuthenticationException {
    public ValidateCodeException(String msg) {
        super(msg);
    }
}
