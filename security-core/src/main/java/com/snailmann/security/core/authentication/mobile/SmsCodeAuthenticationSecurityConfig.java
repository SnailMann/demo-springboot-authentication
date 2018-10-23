package com.snailmann.security.core.authentication.mobile;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;


/**
 * 因为SmsCodeAuthenticationFilter是我们自定义的Filter，与其同级的UsernamePasswordAuthenticationFilter相同
 * 但UsernamePasswordAuthenticationFilter是SpringSecurity集成的，在BrowserSecurityConfig定义了配置。
 * 但是我们的SmsCodeAuthenticationFilter是没有集成的，所以需要我们独立的config去配置
 */
public class SmsCodeAuthenticationSecurityConfig extends SecurityConfigurerAdapter
        <DefaultSecurityFilterChain, HttpSecurity> {

    @Autowired
    @Qualifier("myAuthenticationSuccessHandler")  //因为变量名等于组件名，所以可以省略Qualifier
    private AuthenticationSuccessHandler myAuthenticationSuccessHandler;

    @Autowired
    @Qualifier("myAuthenctiationFailHandler")
    private AuthenticationFailureHandler myAuthenticationFailureHandler;


    @Override
    public void configure(HttpSecurity http) throws Exception {
        SmsCodeAuthenticationFilter smsCodeAuthenticationFilter = new SmsCodeAuthenticationFilter();
        smsCodeAuthenticationFilter.setAuthenticationManager(http.getSharedObject(AuthenticationManager.class));
        smsCodeAuthenticationFilter.setAuthenticationSuccessHandler(myAuthenticationSuccessHandler);
        smsCodeAuthenticationFilter.setAuthenticationFailureHandler(myAuthenticationFailureHandler);
    }
}
