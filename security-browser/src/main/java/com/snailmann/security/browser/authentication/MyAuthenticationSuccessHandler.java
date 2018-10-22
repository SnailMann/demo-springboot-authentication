package com.snailmann.security.browser.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.snailmann.security.core.config.properties.LoginType;
import com.snailmann.security.core.config.properties.SecurityProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * 自定义登录成功的逻辑，然后再securityConfig中显示声明，代表默认的Handle
 */
@Slf4j
@Component("myAuthenticationSuccessHandler")
public class MyAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SecurityProperties securityProperties;

    /**
     * authentication根据不同的方式登录，所包含的信息也有所有不同
     * 比如browser登录，就会包含
     * {
     *     "authorities": [
     *         {
     *             "authority": "admin"
     *         }
     *     ],
     *     "details": {
     *         "remoteAddress": "0:0:0:0:0:0:0:1",
     *         "sessionId": "A9EDF7DC8C76FF42B8EE64F21B1F9DC5"
     *     },
     *     "authenticated": true,
     *     "principal": {
     *         "password": null,
     *         "username": "1001",
     *         "authorities": [
     *             {
     *                 "authority": "admin"
     *             }
     *         ],
     *         "accountNonExpired": true,
     *         "accountNonLocked": true,
     *         "credentialsNonExpired": true,
     *         "enabled": true
     *     },
     *     "credentials": null,
     *     "name": "1001"
     * }
     * @param request
     * @param response
     * @param authentication
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("登录成功");


        if (LoginType.JSON.equals(securityProperties.getBrowser().getLoginType())){ //如果是JSON,则返回JSON
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(objectMapper.writeValueAsString(authentication));
        } else {   //如果不是JSON,则执行默认的方法，耦合式的跳转
            super.onAuthenticationSuccess(request,response,authentication);
        }




    }
}
