package com.snailmann.security.demo.controller;



import com.fasterxml.jackson.annotation.JsonView;
import com.snailmann.security.demo.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @DeleteMapping("/{id:\\d+}")
    public void delete(@PathVariable String id){
        System.out.println("record that id is " + id + " was delete");
    }


    @PutMapping("/{id:\\d+}")    //这个id可以直接映射到RequestBody里面的id，不需要@PathVariable
    public User update(@Valid @RequestBody User user, BindingResult errors){
        if (errors.hasErrors()){
            errors.getAllErrors().forEach(error -> {
               /* FieldError fieldError = (FieldError) error;
                System.out.println(fieldError.getField() +" "+ error.getDefaultMessage());*/
                System.out.println(error.getDefaultMessage());
            });
        }
        user.setId(1);
        log.info(user.toString());
        return user;
    }

    @PostMapping()
    public User create(@Valid @RequestBody User user, BindingResult errors){
        if (errors.hasErrors()){
            errors.getAllErrors().forEach(error -> System.out.println(error.getDefaultMessage()));
        }
        user.setId(1);
        log.info(user.toString());
        return user;
    }

    @GetMapping()
    @JsonView(User.UserSimpleView.class)
    public List<User> query(@RequestParam String username) {

        log.info(username);
        List<User> users = Stream.of(new User(), new User(), new User()).collect(Collectors.toList());
        return users;
    }

    @GetMapping("/{id:\\d+}")                   //：\\d+只允许id是数字
    @JsonView(User.UserDetailView.class)
    public User getInfo(@PathVariable String id){
        User user = new User();
        user.setUsername("jerry");
        return user;
    }
}
