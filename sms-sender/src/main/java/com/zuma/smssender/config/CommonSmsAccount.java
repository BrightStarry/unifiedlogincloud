package com.zuma.smssender.config;

import lombok.Builder;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * author:ZhengXing
 * datetime:2017/11/7 0007 14:24
 * 通用短信api帐号
 */
@Data
@Builder
public class CommonSmsAccount {
    //帐号所属通道
    private String channel;
    //区分同个通道不同类型的帐号
    private Integer type;
    private String aKey;
    private String bKey;
    private String cKey;
}
