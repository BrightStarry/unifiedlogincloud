package com.zuma.smssender.dto.response.sendsms;

import lombok.Builder;
import lombok.Data;

/**
 * author:ZhengXing
 * datetime:2017-11-09 19:17
 * 群正同步响应接口参数
 */
@Data
@Builder
public class QunZhengSendSmsResponse {
    String code;//返回码
    String id;//本次请求id
}
