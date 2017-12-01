package com.zuma.smssender.dto;

import lombok.Builder;
import lombok.Data;

/**
 * author:Administrator
 * datetime:2017/11/9 0009 15:41
 * 掌游同步发送后，需缓存的数据
 */
@Data
@Builder
public class CommonCacheDTO {
    private String id;//api流水号
    private Long platformId;//平台id
    private Long timestamp;//时间戳-sendSmsForm中平台发过来的-方便平台区分
    private String phones;//该次调用的手机号
    private String smsMessage;//该次调用的短信消息
    private Long recordId;//该次短信发送记录id
}
