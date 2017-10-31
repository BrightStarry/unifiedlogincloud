package com.zuma.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * author:Administrator
 * datetime:2017/10/31 0031 10:38
 * 日志消息主体
 */
@Builder
@Data
public class LogMessage implements Serializable{
    //服务id
    private String serviceId;
    //模块id
    private String moduleId ;
    //日志时间
    private Date time;
    //日志内容
    private String content;
}
