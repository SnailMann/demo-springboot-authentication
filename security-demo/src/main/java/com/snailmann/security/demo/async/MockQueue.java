package com.snailmann.security.demo.async;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 模拟消息中间件，用于实现DeferredResult
 * 这里模拟两个队列，一个队列是专门放placeOrder，也就是获得的请求信息就放入这个队列
 * 另一个队列是专门放已经处理好的请求，即返回结果，就是completeOrder
 */

@Slf4j
@Component
public class MockQueue {

    private String placeOrder;
    private String completeOrder;

    public String getPlaceOrder() {
        return placeOrder;
    }

    public void setPlaceOrder(String placeOrder) throws InterruptedException {

        new Thread(() -> {  //模拟处理数据是由应用2来完成的
            log.info("接到下单请求" + placeOrder);
            try {
                Thread.sleep(5000); //模拟下单过程，大概需要1s时间
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            this.completeOrder = placeOrder; //订单处理完毕后，将处理完的订单号放入消息队列
            log.info("接到下单请求处理完成" + placeOrder);
        }, "app-2").start();

    }

    public String getCompleteOrder() {
        return completeOrder;
    }

    public void setCompleteOrder(String completeOrder) {
        this.completeOrder = completeOrder;
    }
}
