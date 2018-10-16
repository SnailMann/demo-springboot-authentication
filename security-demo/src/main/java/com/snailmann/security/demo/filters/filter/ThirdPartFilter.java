package com.snailmann.security.demo.filter;


import javax.servlet.*;
import java.io.IOException;

/**
 * 不适用@Component以模拟第三方的拦截器无法加@Component
 * 在springboot以前的方式中，可以在web.xml去配置，让第三方filter加入Spring容器
 * 但在springboot中没有web.xml
 */
public class ThirdPartFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("ThirdPart Filter initialize...");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("ThirdPart Filter doFilter now...");
        filterChain.doFilter(servletRequest,servletResponse);
    }

    @Override
    public void destroy() {

    }
}
