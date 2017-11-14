package com.zuma.smssender.dto.response.smsup;

import lombok.Builder;
import lombok.Data;

/**
 * author:Administrator
 * datetime:2017/11/10 0010 13:31
 * 宽信短信上行对象
 */
@Data
public class KuanXinSmsUpResponse {
    private String id;//唯一序列号
    private String mobile;//用户上行手机号码，如：13505710000
    private String srcId;//接收号码，平台提供的接入号
    private String msgContent;//接收内容，用户上行内容信息
    private String time;//接收时间，需要按yyyyMMddHHmmss格式，如：20110115105822)
}
