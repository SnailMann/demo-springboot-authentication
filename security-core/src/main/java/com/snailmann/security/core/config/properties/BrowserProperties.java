package com.snailmann.security.core.config.properties;

import lombok.Data;



@Data
public class BrowserProperties {

    private String loginPage = "/login.html";
    private Integer port ;
}
