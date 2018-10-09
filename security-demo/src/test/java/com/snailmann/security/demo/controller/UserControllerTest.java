package com.snailmann.security.demo.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import java.util.stream.Stream;



@RunWith(SpringRunner.class)
@SpringBootTest
public class UserControllerTest {

    //模拟web请求环境，就不需要启动tomcat
    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;

    @Before
    public void init(){
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build(); //在方法执行之前构造web模拟环境
    }

    @Test
    public void whenQuerySuccess() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/user")
                .param("username","jerry")
                .contentType(MediaType.APPLICATION_JSON_UTF8))   //get请求，url是/user,contentType是json,编码是utf-8
                .andExpect(MockMvcResultMatchers.status().isOk())                                                //把期望的结果写在里面，期望返回的http状态码是200
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(3));   //期待返回的集合长度为3
    }

}
