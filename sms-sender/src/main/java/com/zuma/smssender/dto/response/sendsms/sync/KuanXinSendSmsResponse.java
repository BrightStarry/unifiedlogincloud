package com.zuma.smssender.dto.response.sendsms.sync;

import lombok.Data;

/**
 * author:Administrator
 * datetime:2017/11/9 0009 16:57
 * 宽信发送短信接口响应对象
 */
@Data
public class KuanXinSendSmsResponse {
    private String code;//状态码
    private String msg;//状态描述
    private KuanXinSendSmsResponseData data;//数据节点


    /**
     * 宽信发送短信接口响应对象中的data子对象
     */
    @Data
    public class KuanXinSendSmsResponseData{
        private String taskId;//任务ID,接口返回的taskid，如果接口返回非0，则不返回data节点
    }
}
