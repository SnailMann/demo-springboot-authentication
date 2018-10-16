package com.snailmann.security.demo.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * aop核心，面向切片编程,拦截器
 *
 */
@Slf4j
@Aspect
@Component
public class TimeAspect {

    @Around("execution(* com.snailmann.security.demo.controller.UserController.*(..))")
    public Object handleControllerMethod(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        System.out.println("aspect starting");
        Object[] args = proceedingJoinPoint.getArgs();
        Arrays.asList(args).forEach(System.out::println);
        Object obj = proceedingJoinPoint.proceed();
        System.out.println("aspect end");
        return obj;
    }


    /*@Around("execution(* com.snailmann.security.demo.async.AsyncController.*(..))")
    public Object handleAsyncControllerMethod(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        log.info("aspect starting");
        Object obj = proceedingJoinPoint.proceed();
        log.info("aspect end");
        return obj;
    }*/


}
