package com.zuma.dto;

import lombok.Data;

import java.util.Date;

/**
 * author:Administrator
 * datetime:2017/10/30 0030 15:48
 */
@Data
public class Log {
    private Integer channel;//通道
    private Integer module;//模块
    private Date logTime;//日志时间
    private String body;//日志主体
}
