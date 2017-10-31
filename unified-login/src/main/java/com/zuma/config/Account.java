package com.zuma.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * author:ZhengXing
 * datetime:2017/10/17 0017 10:16
 * 自定义配置-管理账号
 */

@Component
@Data
@ConfigurationProperties(prefix = "account")
public class Account {
    private String username;
    private String password;
}
