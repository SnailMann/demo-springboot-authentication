package com.snailmann.security.core.properties;

import com.snailmann.security.core.properties.SecurityProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 这个配置文件的作用就是为了让SecurityCoreConfig生效
 * 因为作用就相当于在SecurityProperties加上@Component的效果，即注册到Spring容器中
 */
@Configuration
@EnableConfigurationProperties(SecurityProperties.class)
public class SecurityCoreConfig {


}
