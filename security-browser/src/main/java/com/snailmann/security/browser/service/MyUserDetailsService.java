package com.snailmann.security.browser.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;


@Slf4j
@Component
public class MyUserDetailsService implements UserDetailsService {

    //正式情况，可以在这里注入Mybatis的Mapper

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("登录用户名：" + username);
        //根据用户名查找用户信息
        String password = passwordEncoder.encode("123456");
        log.info("数据库密码是：" + password);
        //这里的一块实际上是要去数据库中读取的，这里省略了
        return new User(username,password,
                true,true,true,true
                , AuthorityUtils.commaSeparatedStringToAuthorityList("admin"));
        //SpringSecurity的User对象已经实现了UserDetails接口
        //SpringSecurity的username,password完成认证，后面的authorities集合完成授权的工作
        //即这个User具有哪些权限，可以访问什么，不能访问什么
    }
}
