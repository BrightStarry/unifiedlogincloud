package com.zuma.smssender.dto.response;

import lombok.Data;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * author:Administrator
 * datetime:2017/11/10 0010 13:55
 */
@XmlRootElement(name = "sms")
@Data
public class QunZhengSmsUpResponseChild {
    @XmlElement(name = "phone")
    private String phone;//手机号
    @XmlElement(name = "content")
    private String content;//消息内容
    @XmlElement(name = "recvdate")
    private String recvDate;//接收日期
    @XmlElement(name = "service_no")
    private String serviceNo;//扩展号
}
