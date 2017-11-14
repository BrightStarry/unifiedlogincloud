package com.zuma.smssender.dto.response.sendsms.async;

import lombok.Data;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * author:Administrator
 * datetime:2017/11/10 0010 09:26
 * 掌游异步回调接口，包括下行结果通知、上行推送等
 */
@Data
@XmlRootElement(name = "MsgDataReport")
public class ZhangYouAsyncResponse {
    private String msgType;//消息类型
    private String msgCode;//消息代码
    private String msgContent;//消息内容
    private String mobileSource;//用户手机号
    private String timestamp;//时间戳
    private String spCode;//用户上行端口
    private String taskId;//对应下行任务编号,MsgType取值为report时，该节点有效，时间戳(17位)+3位随机数
}
