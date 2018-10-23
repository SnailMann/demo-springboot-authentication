package com.snailmann.security.core.validate.code.config;

import com.snailmann.security.core.properties.SecurityProperties;
import com.snailmann.security.core.validate.code.ValidateCodeGenerator;
import com.snailmann.security.core.validate.code.image.ImageCodeGenerator;
import com.snailmann.security.core.validate.code.sms.DefaultSmsCodeSender;
import com.snailmann.security.core.validate.code.sms.SmsCodeSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 默认配置
 */
@Configuration
public class ValidateCodeBeanConfig {

    @Autowired
    private SecurityProperties securityProperties;


    /**
     * 相当于是默认配置，用来被覆盖的
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(name = "imageCodeGenerator")     //如果Spring容器中已经有一个imageCodeGenerator，则不用该方法，如果没有则用该方法生成
    public ValidateCodeGenerator imageCodeGenerator(){
        ImageCodeGenerator imageCodeGenerator = new ImageCodeGenerator();
        imageCodeGenerator.setSecurityProperties(securityProperties);
        return imageCodeGenerator;
    }

    /**
     * 默认配置
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(SmsCodeSender.class)
    public SmsCodeSender smsCodeSender(){
        return new DefaultSmsCodeSender();
    }

}
