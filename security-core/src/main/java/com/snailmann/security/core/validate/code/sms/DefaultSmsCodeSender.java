package com.snailmann.security.core.validate.code.sms;


/**
 * 模拟手机发送短信
 */
public class DefaultSmsCodeSender implements SmsCodeSender {
    @Override
    public void send(String mobile, String code) {
        System.out.println("向手机" + mobile + "发送短信验证码" + code);

    }
}
