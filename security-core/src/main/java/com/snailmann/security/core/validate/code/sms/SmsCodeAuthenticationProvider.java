package com.snailmann.security.core.validate.code.sms;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

public class SmsCodeAuthenticationProvider implements AuthenticationProvider {

    @Getter
    @Setter
    private UserDetailsService userDetailsService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        SmsCodeAuthenticationToken authenticationToken = (SmsCodeAuthenticationToken) authentication;
        UserDetails user = userDetailsService.loadUserByUsername((String) authenticationToken.getPrincipal());
        //如果有该手机号，则代表有该账号，如果没有则代表该手机号并没有注册
        if (user == null){
            throw new InternalAuthenticationServiceException("无法获取用户信息");
        }

        //这里我们就不能用一个参数的构造函数了，要使用两个构造函数的参数，因为因为完成了认证，需要setAuthenticated(true)
        SmsCodeAuthenticationToken authenticationResult = new SmsCodeAuthenticationToken(user,user.getAuthorities());
        //将未认证的Token的Details（request请求,在Filter里set了）拷贝到已认证的token里面去
        authenticationResult.setDetails(authenticationToken.getDetails());
        return authenticationResult;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        //isAssignableFrom的作用于instanceof相反，判断调用类是否是参数的父类或相同，是则true,否是false
        return SmsCodeAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
