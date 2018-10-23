package com.snailmann.security.browser.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.snailmann.security.browser.entity.SimpleResponse;
import com.snailmann.security.core.properties.LoginType;
import com.snailmann.security.core.properties.SecurityProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 自定义登录失败处理
 */
@Slf4j
@Component("myAuthenctiationFailHandler")
public class MyAuthenctiationFailHandler extends SimpleUrlAuthenticationFailureHandler {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SecurityProperties securityProperties;

    /**
     * 相对于登录成功而言，这里的第三个参数就不同了，而是异常
     * {
     *     "case": null
     *     "stackTrace":[...]
     *     "localizedMessage:" 坏的凭证
     *     "message": 坏的凭证
     *     "suppressed":
     * }
     * @param request
     * @param response
     * @param exception
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        log.info("登录失败");

        if (LoginType.JSON.equals(securityProperties.getBrowser().getLoginType())){
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(objectMapper.writeValueAsString(new SimpleResponse(exception.getMessage())));
        } else  {
            super.onAuthenticationFailure(request,response,exception);
        }

    }
}
