package com.zuma.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * author:Administrator
 * datetime:2017/10/31 0031 10:35
 * 日志消息头
 */
@Builder
@Data
public class LogHeader  implements Serializable {
    //服务id
    private String serviceId = "0";
    //模块id
    private String moduleId = "A";
    //日志时间
    private Date time = new Date();
}
