package com.snailmann.security.core.properties;

import lombok.Data;



@Data
public class BrowserProperties {

    private LoginType loginType = LoginType.JSON;
    private String loginPage = "/login.html";
    private int rememberMeSeconds = 3600;  //记住我的过期时间，单位s
}
