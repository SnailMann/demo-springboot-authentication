package com.snailmann.security.demo;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.xml.ws.Service;
import java.lang.reflect.Field;
import java.util.Arrays;

@ComponentScan(basePackages={"com.snailmann.security"})
@SpringBootApplication
@EnableAsync
@EnableSwagger2
public class DemoApplication {


    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
}
