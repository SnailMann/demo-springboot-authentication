package com.snailmann.security.demo.async;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;


/**
 *  为了测试@Async注解和Callable,ReferredResult的区别
 */
@Slf4j
@Service
public class AsyncService {

    @Async
    public void testMethod(String number) throws InterruptedException {
        log.error( "Task-"+ number+ " running");
        Thread.sleep(2000);
        log.error( "Task-"+ number+ " end");
    }
}
