package com.snailmann.security.demo.service.impl;

import com.snailmann.security.demo.service.HelloService;
import org.springframework.stereotype.Service;


@Service
public class HelloServiceImpl implements HelloService {
    @Override
    public String greeting(String name) {
        System.out.println("greeting");
        return "hello" + name;
    }
}
