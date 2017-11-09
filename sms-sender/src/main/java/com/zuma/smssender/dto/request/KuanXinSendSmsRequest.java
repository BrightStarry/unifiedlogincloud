package com.zuma.smssender.dto.request;

import lombok.Builder;
import lombok.Data;

/**
 * author:Administrator
 * datetime:2017/11/9 0009 16:41
 * 宽信请求对象
 */
@Data
@Builder
public class KuanXinSendSmsRequest {
    private String userId;//用户编号，接入方信息唯一标识
    private Long ts;//时间戳, 5分钟内有效, 时间戳是指格林威治时间1970年01月01日00时00分00秒起至现在的总毫秒数
    private String sign;//三个信息字符串拼接，然后md5算法加密 （MD5用32位，值必须要小写进行加密）md5(userid + ts + apikey) 中间无需空格
    private String mobile;//需要发送的手机号(多个号码以英文逗号 “,”分隔) 一次性最多1000个号码
    private String msgcontent;//短信内容 需要用urlencoder的utf-8编码 示例: java：URLEncode.encode(content,“utf-8”)1.普通短信70字 2.长短信350字
    private String extnum;//下发扩展号（1-4位）4个字节以内
    private String time;//发送时间(为空表示立即发送，如果定时发送，则需要按yyyyMMddHHmmss格式，如：20110115105822)
}
