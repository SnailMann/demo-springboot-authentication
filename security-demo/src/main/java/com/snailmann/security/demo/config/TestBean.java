package com.snailmann.security.demo.config;

import com.snailmann.security.demo.async.MockQueue;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * constructor > @Autowired > PostConstruct > afterPropertiesSet
 */
@Component
public class TestBean implements InitializingBean {

    @Autowired
    MockQueue mockQueue;

    public TestBean() {
        System.out.println("testBean constructor");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println(this);
        System.out.println("testBean afterPropertiesSet");
    }


    @PostConstruct
    public void init(){
        System.out.println("testBean PostConstruct running");
    }
}
