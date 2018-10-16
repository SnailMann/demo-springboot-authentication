package com.snailmann.security.demo.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * 目前这个拦截器是全局拦截，即没有设置匹配，所以不管是Spring自己的controller还是自己的都是会被拦截的
 */
@Component
public class TimeInterceptor implements HandlerInterceptor {
    /**
     * 控制器代码执行前会执行这个方法（比如鉴权阶段这个在这里）
     * @param httpServletRequest
     * @param httpServletResponse
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object handler) throws Exception {
        System.out.println("preHandle...");
        System.out.println(((HandlerMethod)handler).getMethod().getName());
        httpServletRequest.setAttribute("startTime",System.currentTimeMillis());
        return true;
    }


    /**
     * 控制器代码执行之后，结果返回之前会执行这个方法，如果抛出异常则不会执行这个方法
     * @param httpServletRequest
     * @param httpServletResponse
     * @param o
     * @param modelAndView
     * @throws Exception
     */
    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
        System.out.println("PostHandle...");
        System.out.println("Interceptor 耗时: " + (double)(System.currentTimeMillis() - (Long) httpServletRequest.getAttribute("startTime")) + "s");
    }

    /**
     * 不管是否抛出异常，总之最后都会执行这个方法
     * @param httpServletRequest
     * @param httpServletResponse
     * @param o
     * @param e
     * @throws Exception
     */
    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
        System.out.println("afterHandle...");
        //如果设置了@ComponentAdvice,即异常统一处理handle，那么如果controller发生了对应的异常，这里的拦截器是拿不到异常的
        //因为ComponentAdvice比拦截器这个方法更快执行
        System.out.println("execption is " +e);
    }
}
