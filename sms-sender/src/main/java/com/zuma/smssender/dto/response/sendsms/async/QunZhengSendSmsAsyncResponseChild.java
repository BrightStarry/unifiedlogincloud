package com.zuma.smssender.dto.response.sendsms.async;

import lombok.Data;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * author:Administrator
 * datetime:2017/11/10 0010 11:18
 * 群正发送短信接口异步回调响应子对象
 */

@XmlRootElement(name = "sms")
@Data
public class QunZhengSendSmsAsyncResponseChild {
//    @XmlElement(name = "phone")
    private String phone;//手机号
//    @XmlElement(name = "pno")
    private String pno;//流水号
//    @XmlElement(name = "state")
    private String state;//状态码
}
