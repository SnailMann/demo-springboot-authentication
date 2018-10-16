package com.snailmann.security.browser.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RestController
public class BrowserSecurityController {

    /**
     *   For RequestCache
     *   在跳转到/authentication/require之前，SpringSecurity会将当前的请求缓存到HttpSessionRequestCache中
     *   比如说某个用户想查看某篇文章（/article），但是该文章需要用户登录才能查看,所以会自动跳转/authentication/require
     *   但是在跳转之前，Security会记录/article这个请求到Cache中，当我们通过/authentication/require完成登录，就可以从Cache
     *   取得上次要跳转的请求继续执行，在如下场景就是登录完毕后，自动跳转到该文章浏览
     */

    private RequestCache requestCache = new HttpSessionRequestCache();
    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    /**
     * 当需要身份认证时，跳转到这里
     *
     * @param request
     * @param response
     * @return
     */
    @PostMapping("/authentication/require")
    @ResponseStatus(code = HttpStatus.UNAUTHORIZED)
    public String requireAuthentication(HttpServletRequest request, HttpServletResponse response) throws IOException {
        SavedRequest savedRequest = requestCache.getRequest(request,response);
        if (savedRequest != null){
            String target = savedRequest.getRedirectUrl();
            log.info("引起跳转的请求是:" + target);
            if (StringUtils.endsWithIgnoreCase(target,".html")){
                redirectStrategy.sendRedirect(request,response,"");
            }
        }
        return null;
    }
}
