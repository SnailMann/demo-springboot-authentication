package com.snailmann.security.browser.config;

import com.snailmann.security.core.config.properties.SecurityProperties;
import com.snailmann.security.core.validate.code.Filter.ValidateCodeFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;


/**
 * 表单验证的配置
 */
@Configuration
public class BrowserSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    SecurityProperties securityProperties;

    @Autowired
    @Qualifier("myAuthenticationSuccessHandler")  //因为变量名等于组件名，所以可以省略Qualifier
    private AuthenticationSuccessHandler myAuthenticationSuccessHandler;

    @Autowired
    @Qualifier("myAuthenctiationFailHandler")
    private AuthenticationFailureHandler myAuthenticationFailureHandler;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private UserDetailsService userDetailsService;



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


    /**
     * 权限安全配置
     *
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {


        ValidateCodeFilter validateCodeFilter = new ValidateCodeFilter();
        validateCodeFilter.setMyAuthenticationFailureHandler(myAuthenticationFailureHandler);
        //任何请求都需要表单认证
        //为了实现验证码验证，我们需要将我们的验证码过滤器在UsernamePasswordAuthenticationFilter之前实现(过滤链)
        http.addFilterBefore(validateCodeFilter, UsernamePasswordAuthenticationFilter.class)
                .formLogin()                                    //httpBasic()是弹窗 与 formLogin()是正常网页
                    .loginPage("/authentication/require")           //自动登录页面，没有则使用默认的
                    .loginProcessingUrl("/authentication/form")     //自定义，对应form表达的url请求
                    .successHandler(myAuthenticationSuccessHandler) //加了这个就不会使用SpringSecurity默认的配置
                    .failureHandler(myAuthenticationFailureHandler) //自定义登录失败处理逻辑
                    .and()
                .rememberMe()                                       //RememberMe Function
                    .tokenRepository(getPersistentTokenRepository())
                    .tokenValiditySeconds(securityProperties.getBrowser().getRememberMeSeconds())
                    .userDetailsService(userDetailsService)
                    .and()
                .authorizeRequests()
                .antMatchers(
                        "/login.html"
                        , securityProperties.getBrowser().getLoginPage()).permitAll()  //当访问/login.html是，放行，避免造成死循环
                .antMatchers(
                        "/authentication/require",
                        "/code/image").permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .csrf().disable();
    }
}
