package com.snailmann.security.browser.config;

import com.snailmann.security.core.validate.code.config.security.SmsCodeAuthenticationSecurityConfig;
import com.snailmann.security.core.validate.code.config.security.AbstractChannelSecurityConfig;
import com.snailmann.security.core.constant.SecurityConstants;
import com.snailmann.security.core.properties.SecurityProperties;
import com.snailmann.security.core.validate.code.config.security.ValidateCodeSecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;


/**
 * 表单验证的配置
 * 继承于CORE的AbstractChannelSecurityConfig，间接继承于 WebSecurityConfigurerAdapter
 */
@Configuration
public class BrowserSecurityConfig extends AbstractChannelSecurityConfig {

    @Autowired
    private SecurityProperties securityProperties;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private SmsCodeAuthenticationSecurityConfig smsCodeAuthenticationSecurityConfig;

    @Autowired
    private ValidateCodeSecurityConfig validateCodeSecurityConfig;


    /**
     * 权限安全配置
     *
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        //基本的http配置
        applyPasswordAuthenticationConfig(http);

        http.apply(validateCodeSecurityConfig)//为了让validateCodeFilter在AbstractPreAuthenticatedProcessingFilter前过滤
                .and()
            .apply(smsCodeAuthenticationSecurityConfig) //sms自定义的一些逻辑配置
                .and()
            .rememberMe() //RemeberMe Function
                .tokenRepository(getPersistentTokenRepository())
                .tokenValiditySeconds(securityProperties.getBrowser().getRememberMeSeconds()) //过期时间
                .userDetailsService(userDetailsService)
                .and()
            .authorizeRequests()
                .antMatchers(  //匹配url
                        SecurityConstants.DEFAULT_UNAUTHENTICATION_URL,
                        SecurityConstants.DEFAULT_LOGIN_PROCESSING_URL_MOBILE,
                        securityProperties.getBrowser().getLoginPage(),
                        SecurityConstants.DEFAULT_VALIDATE_CODE_URL_PREFIX + "/*",
                        "/login.html",
                        "/user/regist")
                .permitAll() //匹配上的全部放行
                .anyRequest()
                .authenticated()
                .and()
            .csrf().disable();

    }


    /**
     * 记住我功能的Token存放
     *
     * @return
     */
    @Bean
    public PersistentTokenRepository getPersistentTokenRepository() {
        JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
        tokenRepository.setDataSource(dataSource);
        /*tokenRepository.setCreateTableOnStartup(true);  *///启动时，自动创建存储token的表。如果不设置则可以自己创建
        return tokenRepository;
    }

    /**
     * 密码加解密
     *
     * @return
     */
    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
