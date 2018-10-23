package com.snailmann.security.core.validate.code.sms;

import com.snailmann.security.core.properties.SecurityProperties;
import com.snailmann.security.core.validate.code.ValidateCodeGenerator;
import com.snailmann.security.core.validate.code.entity.ValidateCode;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.ServletWebRequest;


/**
 * 生成短信验证码
 */
@Component("smsCodeGenerator")
public class SmsCodeGenerator implements ValidateCodeGenerator {

    @Autowired
    private SecurityProperties securityProperties;
    @Override
    public ValidateCode generate(ServletWebRequest request) {
        String code = RandomStringUtils.randomNumeric(securityProperties.getValidate().getSms().getLength()); //长度
        return new ValidateCode(code,securityProperties.getValidate().getSms().getExpireInt());
    }
}
