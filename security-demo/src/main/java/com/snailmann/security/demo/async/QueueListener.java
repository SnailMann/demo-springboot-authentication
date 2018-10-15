package com.snailmann.security.demo.async;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * 消息监听
 */
@Slf4j
@Component
public class QueueListener implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private MockQueue mockQueue;
    @Autowired
    private DeferredResultHolder deferredResultHolder;

    /**
     * 容器启动后会做的事情
     * @param event
     */
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {

        //为了防止主线程阻塞，所以用子线程去做监听的工作
        new Thread(()->{
            while (true) {
                if (StringUtils.isNotBlank(mockQueue.getCompleteOrder())) {
                    String orderNumber = mockQueue.getCompleteOrder(); //不断监听消息队列是否有完成处理的请求结果
                    log.info("返回订单处理结果：" +  orderNumber);
                    deferredResultHolder.getMap().get(orderNumber).setResult("place order success"); //如果有则去对应的DeferredResult中取setResult
                    mockQueue.setCompleteOrder(null);
                } else { //如果队列中没有消息，则休眠一段时间再继续
                    try {
                        Thread.sleep(100);
                    }catch (Exception e){
                        e.getStackTrace();
                    }
                }
            }
        }).start();



    }
}
