package com.snailmann.security.demo.entity;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;

@Data
public class User {

    public interface UserSimpleView{};
    public interface UserDetailView extends UserSimpleView{}

    @JsonView(UserSimpleView.class)
    private String username;
    @JsonView(UserDetailView.class)
    private String password;


}
