package com.snailmann.security.demo.async;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

@Slf4j
@RestController
public class AsyncController {

    @Autowired
    AsyncService asyncService;

    @Autowired
    private MockQueue mockQueue;

    @Autowired
    private DeferredResultHolder deferredResultHolder;

    /**
     * 模拟正常的线程异步方式
     * 如果返回值的是Callable，应该是SpringMVC集成的关系，Callable任务会被加入Spring提供的线程中去执行，正常来说Callable是不会启动的
     * 所以我们可以看到这个方法有请求时，不仅仅有主线程在处理，还有开启SpringMVC集成的副线程
     *
     * @return
     * @throws ExecutionException
     * @throws InterruptedException
     */

    @GetMapping("test/1/order")
    public Callable<Map<String,String>> order() throws ExecutionException, InterruptedException {
        log.error("主线程开始");

        Callable<Map<String,String>> result = () -> {
            log.error("副线程开始");
            Thread.sleep(5000);
            log.error("副线程结束");
            Map<String,String> map = new HashMap<>();
            map.put("status", "success");
            return map;
        };
        log.error("主线程返回");


        return result;

    }

    /**
     * 仅仅是为了与test1对比，如果返回值是Runnable，会不会启动线程，答案是不会的
     *
     * @return
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @GetMapping("test/2/order")
    public Runnable order2() throws ExecutionException, InterruptedException {
        log.error("主线程开始");

        Runnable result = () -> {
            log.error("副线程开始");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.error("副线程结束");
        };
        log.error("主线程返回");

        return result;

    }


    /***
     * 模拟Deferred方式
     * @throws InterruptedException
     */
    @GetMapping("test/3/order")
    public DeferredResult<String> order3() throws  InterruptedException {
        log.error("主线程开始");

        String orderNumber = RandomStringUtils.randomNumeric(8); //8位的随机数
        mockQueue.setPlaceOrder(orderNumber);                              //获得请求，将请求放入消息队列
        DeferredResult<String> result = new DeferredResult<>();
        deferredResultHolder.getMap().put(orderNumber,result);
        log.error("主线程返回");

        return result;


    }


    /**
     * 为了测试@Async注解和Callable,ReferredResult的区别
     * 这里发现一个问题，貌似@Async修饰的方法不能与调用者在同一个类中？测试的使用无法实现异步
     * @throws InterruptedException
     */
    @GetMapping("/test/async")
    public void asyncTest() throws  InterruptedException {
        log.error("主线程开始");

        asyncService.testMethod("1");
        Thread.sleep(5000);
        asyncService.testMethod("2");
        asyncService.testMethod("3");

        log.error("主线程返回");




    }


}
