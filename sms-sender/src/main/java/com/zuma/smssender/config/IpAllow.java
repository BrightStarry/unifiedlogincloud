package com.zuma.smssender.config;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * author:ZhengXing
 * datetime:2017-11-09 20:21
 * ip白名单
 */
@ConfigurationProperties(prefix = "ip")
@Component
@Data
public class IpAllow {
    private String allowIp;
    private String[] ips = StringUtils.split(allowIp,",");
}
