package com.snailmann.security.core.config.properties;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "snailmann.security")
public class SecurityProperties {

    BrowserProperties browserProperties = new BrowserProperties();
}
