package com.snailmann.security.core.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 这个配置文件的作用就是为了让SecurityCoreConfig生效
 */
@Configuration
@EnableConfigurationProperties(SecurityCoreConfig.class)
public class SecurityCoreConfig {


}
