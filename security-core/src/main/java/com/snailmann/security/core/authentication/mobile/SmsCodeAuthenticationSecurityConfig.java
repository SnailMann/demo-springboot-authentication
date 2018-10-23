package com.snailmann.security.core.authentication.mobile;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;


/**
 * 因为SmsCodeAuthenticationFilter是我们自定义的Filter，与其同级的UsernamePasswordAuthenticationFilter相同
 * 但UsernamePasswordAuthenticationFilter是SpringSecurity集成的，在BrowserSecurityConfig定义了配置。
 * 但是我们的SmsCodeAuthenticationFilter是没有集成的，所以需要我们独立的config去配置
 */
@Component
public class SmsCodeAuthenticationSecurityConfig extends SecurityConfigurerAdapter
        <DefaultSecurityFilterChain, HttpSecurity> {

    @Autowired
    @Qualifier("myAuthenticationSuccessHandler")
    private AuthenticationSuccessHandler myAuthenticationSuccessHandler;

    @Autowired
    @Qualifier("myAuthenctiationFailHandler")
    private AuthenticationFailureHandler myAuthenticationFailureHandler;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        SmsCodeAuthenticationFilter smsCodeAuthenticationFilter = new SmsCodeAuthenticationFilter();

        //添加AuthenticationManager.class
        smsCodeAuthenticationFilter.setAuthenticationManager(http.getSharedObject(AuthenticationManager.class));
        //添加自定义认证成功逻辑处理器AuthenticationSuccessHandler
        smsCodeAuthenticationFilter.setAuthenticationSuccessHandler(myAuthenticationSuccessHandler);
        //添加自定义认证成失败逻辑处理器AuthenticationFailureHandler
        smsCodeAuthenticationFilter.setAuthenticationFailureHandler(myAuthenticationFailureHandler);

        //配置我们的Provider
        SmsCodeAuthenticationProvider smsCodeAuthenticationProvider = new SmsCodeAuthenticationProvider();
        smsCodeAuthenticationProvider.setUserDetailsService(userDetailsService);

        //添加我们的Provider
        http.authenticationProvider(smsCodeAuthenticationProvider)
                .addFilterAfter(smsCodeAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        //将我们的smsCodeAuthenticationFilter过滤顺序排在UsernamePasswordAuthenticationFilter的后面，紧跟着
    }
}
