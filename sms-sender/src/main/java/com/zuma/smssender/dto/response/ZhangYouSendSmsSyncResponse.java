package com.zuma.smssender.dto.response;

import lombok.Builder;
import lombok.Data;

/**
 * author:Administrator
 * datetime:2017/11/9 0009 09:45
 * 掌游同步响应接口参数
 */
@Data
@Builder
public class ZhangYouSendSmsSyncResponse {
    private String code;//返回码
    private String id;//本次请求id
}
