package com.snailmann.security.browser.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class BrowserSecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * 密码加解密
     * @return
     */
    @Bean
    public PasswordEncoder getPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }


    /**
     * 登录验证
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //任何请求都需要表单认证
        //httpBasic() 与 formLogin()
        http.formLogin()
                .loginPage("/authentication/require")    //自动登录页面，没有则使用默认的
                .loginProcessingUrl("/authentication/form")   //自定义，对应form表达的url请求
                .and()
                .authorizeRequests()
                .antMatchers("/login.html").permitAll()  //当访问/login.html是，放行，避免造成死循环
                .antMatchers("/authentication/require").permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .csrf().disable();
    }
}
