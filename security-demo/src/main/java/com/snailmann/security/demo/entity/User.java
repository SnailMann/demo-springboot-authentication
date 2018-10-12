package com.snailmann.security.demo.entity;

import com.fasterxml.jackson.annotation.JsonView;
import com.snailmann.security.demo.validator.MyConstraint;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Past;
import java.util.Date;

@Data
public class User {

    public interface UserSimpleView {
    }


    public interface UserDetailView extends UserSimpleView {
    }

    private Integer id;

    @MyConstraint(message = "用户名自定义注解测试(校验失败时输出)")
    @JsonView(UserSimpleView.class)
    private String username;

    @NotBlank(message = "密码不允许为空") //字符串必须不为空
    @JsonView(UserDetailView.class)
    private String password;

    @Past(message = "生日必须是过去式") //必须是过去的时间
    private Date birthday;


}
