package com.zuma.smssender.dto.response.sendsms;

import lombok.Data;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * author:Administrator
 * datetime:2017/11/10 0010 11:15
 * 群正发送短信接口异步回调响应
 */

@Data
@XmlRootElement(name = "result")
public class QunZhengSendSmsAsyncResponse {
    @XmlElement(name = "response")
    private Integer response;//本次返回的状态报告条数

    @XmlElement(name = "sms",type = QunZhengSendSmsAsyncResponseChild.class)
    private List<QunZhengSendSmsAsyncResponseChild> smsList;//每条报告实体类

    private QunZhengSendSmsAsyncResponseChild sms;//特例，用来给service方法循环调用

}
