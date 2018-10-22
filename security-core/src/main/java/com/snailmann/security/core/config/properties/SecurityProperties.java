package com.snailmann.security.core.config.properties;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;


@Data
@ConfigurationProperties(prefix = "snailmann.security")
public class SecurityProperties {

    /**
     * 在这里被坑了很久，因为prefix只到snailmann.security这一级，所以browser以及下一级要怎么映射
     * 就需要用变量名去一一对应，即BrowserProperties的变量名必须对应yml的browser，而不能随意起名字
     * 一开始被误导的原因就是起名为browserProperties,而导致错误，记住必须映射好
     *
     * 浏览器端配置
     */
    BrowserProperties browser = new BrowserProperties();

    /**
     * 图片验证码\短信验证码等配置
     */
    ValidateCodeProperties validate = new ValidateCodeProperties();

}
