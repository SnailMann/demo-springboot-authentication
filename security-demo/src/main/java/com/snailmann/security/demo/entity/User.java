package com.snailmann.security.demo.entity;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import java.util.Date;

@Data
public class User {

    public interface UserSimpleView {
    }


    public interface UserDetailView extends UserSimpleView {
    }

    private Integer id;

    @JsonView(UserSimpleView.class)
    private String username;

    @NotBlank
    @JsonView(UserDetailView.class)
    private String password;

    private Date birthday;


}
