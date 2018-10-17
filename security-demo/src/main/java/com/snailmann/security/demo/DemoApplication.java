package com.snailmann.security.demo;

import com.snailmann.security.core.config.properties.BrowserProperties;
import com.snailmann.security.core.config.properties.SecurityProperties;
import com.snailmann.security.core.config.properties.TestProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.annotation.PostConstruct;

@ComponentScan(basePackages={"com.snailmann.security"})
@SpringBootApplication
@EnableAsync
@EnableSwagger2
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);

    }
}
