package com.snailmann.security.demo.filter;

import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * 需要@Compenent把filter注册到容器中，filter才会生效
 */
@Component
public class TimeFilter implements Filter {


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("time filter initialize");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        System.out.println(httpServletRequest.getRequestURL());
        if (httpServletRequest.getRequestURL().toString().indexOf("/") > 0){  //意思就是凡是controller请求都直接pass，为了后面的实现，除去filter的干扰
            filterChain.doFilter(servletRequest,servletResponse);
        } else {
            System.out.println("time filter start");
            long startTime = System.currentTimeMillis();
            filterChain.doFilter(servletRequest,servletResponse);
            System.out.println("time filter spend : " + (double)(System.currentTimeMillis() - startTime)/1000 + "s");
            System.out.println("time filter finish");
        }

    }

    @Override
    public void destroy() {
        System.out.println("time filter destroy");
    }
}
