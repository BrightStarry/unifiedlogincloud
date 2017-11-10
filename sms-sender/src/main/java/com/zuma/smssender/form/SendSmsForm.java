package com.zuma.smssender.form;

import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * author:Administrator
 * datetime:2017/11/8 0008 09:56
 * 其他平台调用我。发送短信接口表单验证
 */
@Data
public class SendSmsForm {
    @NotBlank(message = "平台Id不能为空")
    @Size(min = 1000, max = 9999,message = "平台id不符合规范")
    private Long platformId;//平台id

    private Integer channel;//通道: 1:掌游；2：宽信；3：群正

    @NotBlank(message = "手机号不能为空")
    @Length(min = 11,message = "手机号不符合规范,小于11位")
    private String phones;//手机号[], 逗号间隔

    @NotBlank(message = "短信消息不能为空")
    private String smsMessage;//短信消息[]， !&间隔

    @NotBlank(message = "签名不能为空")
    @Length(min = 32, max = 32, message = "签名必须为32位")
    private String sign;//签名； 平台key + 手机号 + 当前毫秒数,做MD5，32,小写

    @NotBlank(message = "毫秒数不能为空")
    private Long timestamp;
}
