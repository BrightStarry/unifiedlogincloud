package com.zuma.smssender.dto.response.self;

import com.zuma.smssender.enums.error.ZhangYouErrorEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * author:Administrator
 * datetime:2017/11/10 0010 09:18
 * 掌游发送短信接口异步回调，我们发回给平台的
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement(name = "MsgDataReportResp")
public class ZhangYouAsyncResponseResult {
    @XmlElement(name = "ResultCode")
    private String code;

    @XmlElement(name = "ResultMSG")
    private String message;

    @XmlElement(name = "Timestamp")
    private String date;

    public ZhangYouAsyncResponseResult(ZhangYouErrorEnum errorEnum, Date date) {
        this.code = errorEnum.getCode();
        this.message = errorEnum.getMessage();
        this.date = new SimpleDateFormat("yyyyMMddHHmmss").format(date);//可用@JsonFormat注解
    }
}
