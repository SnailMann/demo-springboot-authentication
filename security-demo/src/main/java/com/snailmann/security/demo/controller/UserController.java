package com.snailmann.security.demo.controller;



import com.fasterxml.jackson.annotation.JsonView;
import com.snailmann.security.demo.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@RestController
public class UserController {

    @GetMapping("/user")
    @JsonView(User.UserSimpleView.class)
    public List<User> query(@RequestParam String username) {

        log.info(username);
        List<User> users = Stream.of(new User(), new User(), new User()).collect(Collectors.toList());
        return users;
    }

    @GetMapping("/user/{id:\\d+}")                   //：\\d+只允许id是数字
    @JsonView(User.UserDetailView.class)
    public User getInfo(@PathVariable String id){
        User user = new User();
        user.setUsername("jerry");
        return user;
    }
}
