package com.zuma.smssender.dto.response;

import lombok.Data;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * author:Administrator
 * datetime:2017/11/10 0010 13:53
 * 群正短信上行对象
 */
@Data
@XmlRootElement(name = "result")
public class QunZhengSmsUpResponse {
    private Integer response;//大于0：此消息所对应的状态报告条数

    @XmlElement(name = "sms",type = QunZhengSmsUpResponseChild.class)
    private List<QunZhengSmsUpResponseChild> smsList;

    private QunZhengSmsUpResponseChild sms;//特例属性，方便service循环调用
}
