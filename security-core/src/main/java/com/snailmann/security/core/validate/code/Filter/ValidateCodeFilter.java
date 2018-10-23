package com.snailmann.security.core.validate.code.Filter;

import com.snailmann.security.core.config.properties.SecurityProperties;
import com.snailmann.security.core.validate.code.ValidateCodeProcessor;
import com.snailmann.security.core.validate.code.controller.ValidateCodeController;
import com.snailmann.security.core.validate.code.entity.ImageCode;
import com.snailmann.security.core.validate.code.exception.ValidateCodeException;
import com.sun.org.apache.regexp.internal.RE;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.social.connect.web.HttpSessionSessionStrategy;
import org.springframework.social.connect.web.SessionStrategy;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * ValidateCodeFilter（ImageCode的过滤器）需要在UsernamePasswordAuthenticationFilter前过滤，也就是在登录之前，先要完成验证码校验
 * Spring默认的Filter都会实现该接口，OncePerRequestFilter保证过滤器每个请求只被过滤一次
 */
public class ValidateCodeFilter extends OncePerRequestFilter implements InitializingBean {

    /**
     * 这里是省略了@Qualifier的注入方式，通过变量名跟comonent名映射
     *
     */
    @Getter@Setter
    private AuthenticationFailureHandler myAuthenticationFailureHandler;

    private SessionStrategy sessionStrategy = new HttpSessionSessionStrategy();

    //需要拦截的url
    @Getter@Setter
    private Set<String> urls = new HashSet<>();

    @Getter@Setter
    private SecurityProperties securityProperties;

    private AntPathMatcher pathMatcher = new AntPathMatcher();


    /**
     * 添加需要的匹配的URL
     * @throws ServletException
     */
    @Override
    public void afterPropertiesSet() throws ServletException {
        super.afterPropertiesSet();
        //将yml的配置url根据,分割出一段段的url，放入set中
        String[] configUrls = StringUtils
                .splitByWholeSeparatorPreserveAllTokens(securityProperties.getValidate().getImage().getUrl(),",");
        if (configUrls != null){
            urls.addAll(Arrays.asList(configUrls));
        }
        urls.add("/authentication/form");
        /*urls.add("/authentication/mobile");*/
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        boolean action = false;

        for (String url :urls){
            if (pathMatcher.match(url,request.getRequestURI())) //如果传进来的uri可以和我们配置的需要拦截的url一致，则true
                action = true;
        }

        //before //请求是/authentication/form，且是Post请求（意思就是登陆请求才做处理）
        //after  //如果传进来的uri可以和我们配置的需要拦截的url一致,则进入
        if (action){

            try {
                validate(new ServletWebRequest(request));

            }catch (ValidateCodeException e){  //如果出现异常，则用我们之前定义的MyAuthenticationFailureHandler实现类去处理
                myAuthenticationFailureHandler.onAuthenticationFailure(request,response,e);
                return; //处理完异常就不继续走了
            }
        }
        //如果不是登陆请求就直接放行
        filterChain.doFilter(request,response);
    }

    /**
     * 主要逻辑
     * @param request
     */
    private void validate(ServletWebRequest request) throws ServletRequestBindingException {

        ImageCode codeInSession = (ImageCode) sessionStrategy.getAttribute(request,
                ValidateCodeProcessor.SESSION_KEY_PREFIX + "IMAGE");
        String codeInRequest = ServletRequestUtils.getStringParameter(request.getRequest(),"imageCode");



        if (StringUtils.isBlank(codeInRequest)){
            throw new ValidateCodeException("验证码的值不能为空");
        }
        if (null == codeInSession){
            throw new ValidateCodeException("验证码不存在");
        }
        if (codeInSession.isExpried()){
            sessionStrategy.removeAttribute(request,ValidateCodeProcessor.SESSION_KEY_PREFIX + "IMAGE");
            throw new ValidateCodeException("验证码已过期");
        }
        if (!StringUtils.equals(codeInSession.getCode(),codeInRequest)){
            throw new ValidateCodeException("验证码不匹配");
        }
        //如果成功，则要删除Session中的信息
        sessionStrategy.removeAttribute(request,ValidateCodeProcessor.SESSION_KEY_PREFIX + "IMAGE");
    }
}
