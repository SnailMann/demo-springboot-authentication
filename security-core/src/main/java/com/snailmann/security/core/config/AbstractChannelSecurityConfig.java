/**
 * 
 */
package com.snailmann.security.core.config;

import com.snailmann.security.core.constant.SecurityConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;


/**
 * 基本登录验证配置
 * 用于给别处继承，比如Browser和App
 */
public class AbstractChannelSecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * 自定义的验证成功后逻辑处理（主要作用是决定返回的东西是什么）
     */
	@Autowired
	protected AuthenticationSuccessHandler imoocAuthenticationSuccessHandler;


    /**
     * 自定义的验证失败后逻辑处理
     */
	@Autowired
	protected AuthenticationFailureHandler imoocAuthenticationFailureHandler;
	
	protected void applyPasswordAuthenticationConfig(HttpSecurity http) throws Exception {
		http.formLogin()   //httpBasic()是弹窗 与 formLogin()是正常网页
			.loginPage(SecurityConstants.DEFAULT_UNAUTHENTICATION_URL) //自动登录页面，没有则使用默认的
			.loginProcessingUrl(SecurityConstants.DEFAULT_LOGIN_PROCESSING_URL_FORM) //自定义，对应form表达的url请求
			.successHandler(imoocAuthenticationSuccessHandler) //加了这个就不会使用SpringSecurity默认的配置
			.failureHandler(imoocAuthenticationFailureHandler); //自定义登录失败处理逻辑
	}
}
