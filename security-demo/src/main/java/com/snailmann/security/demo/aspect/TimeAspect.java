package com.snailmann.security.demo.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * aop核心，面向切片编程
 *
 */
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


}
