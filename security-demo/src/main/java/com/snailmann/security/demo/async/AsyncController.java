package com.snailmann.security.demo.async;


import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.*;

@Slf4j
@RestController
public class AsyncController {


    /**
     * 如果返回值的是Callable，可能是SpringMVC集成的关系，Callable任务会被加入线程中去执行，正常来说Callable是不会启动的
     * 所以我们可以看到这个方法有请求时，不仅仅有主线程在处理，还有开启SpringMVC集成的副线程
     *
     * @return
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @GetMapping("test/1/order")
    public Callable<String> order() throws ExecutionException, InterruptedException {
        log.error("主线程开始");

        Callable<String> result = () -> {
            log.error("副线程开始");
            Thread.sleep(1000);
            log.error("副线程结束");
            return "success";
        };
        log.error("主线程返回");

       FutureTask<String> future = new FutureTask<>(result);


        Executors.newFixedThreadPool(5).submit(future);

        while (true) {
            Thread.sleep(1000);
            log.info(future.get());


        }

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


    @GetMapping("test/3/order")
    public void order3() throws ExecutionException, InterruptedException {
        log.error("主线程开始");

        Callable<String> result = () -> {
            log.error("副线程开始");
            Thread.sleep(1000);
            log.error("副线程结束");

            return "ok";
        };
        FutureTask<String> futureTask = new FutureTask<>(result);

        new Thread(futureTask).start();

        Thread.sleep(1200);
        if (futureTask.isDone())
            log.info(futureTask.get());


        log.error("主线程返回");


    }

}
