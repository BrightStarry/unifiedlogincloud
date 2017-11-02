package com.zuma.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

;

/**
 * author:Administrator
 * datetime:2017/10/31 0031 10:38
 * 日志消息主体
 */
@Data
@Builder
public class LogMessage implements Serializable{
    //服务id
    private Integer serviceId;
    //通道
    private Integer channel;
    //模块name
    private String moduleName ;
    //日志时间
    private Date date;
    //日志内容
    private String content;

}
