package com.snailmann.security.demo.config;


import com.snailmann.security.demo.filter.ThirdPartFilter;
import com.snailmann.security.demo.interceptor.TimeInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * WebMvcConfigurerAdapter应该就是用来代替web.xml的JavaBean方式
 */
@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {



    @Autowired
    TimeInterceptor timeInterceptor;

    /**
     * 通过configuration的方式来讲Filter注册到Spring容器中
     * 作用就是某些第三方filter无法使用@Component注解，springboot也没有提供web.xml的方式去注册
     * 所以就必须通过configuration的方式来注册
     * @return
     */
    @Bean
    public FilterRegistrationBean getThirdPartyFilter(){
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        registrationBean.setFilter(new ThirdPartFilter());
        List<String> urls = Stream.of("/filter/*").collect(Collectors.toList());
        registrationBean.setUrlPatterns(urls);
        return registrationBean;
    }

    /**
     * 让spring的intercptor生效（这是拦截同步请求的，异步请求可能通过这个无法实现）
     *
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //"/*/**"拦截所有
        registry.addInterceptor(timeInterceptor).addPathPatterns("/interceptor/**");

    }
/*
    @Bean
    public BrowserSecurityConfig getBrowserSecurityConfig(){
        return new BrowserSecurityConfig();
    }*/


    /*@Override //异步请求支持
    public void configureAsyncSupport(AsyncSupportConfigurer configurer) {
       configurer.registerCallableInterceptors((CallableProcessingInterceptor) timeInterceptor);
    }*/
}
