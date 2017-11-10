package com.zuma.smssender.dto.request;

import lombok.Builder;
import lombok.Data;

/**
 * author:ZhengXing
 * datetime:2017-11-09 19:11
 * 群正平台发送短信接口请求对象
 */
@Data
@Builder()
public class QunZhengSendSmsRequest {
    private String flag;//操作命令
    private String loginName;//用户id
    private String password;//密码
    private String p;//手机号
    private String c;//短信消息
}
