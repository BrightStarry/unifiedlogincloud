package com.zuma.smssender.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * author:Administrator
 * datetime:2017/11/9 0009 14:27
 * 失败的数据
 * 此数据需要split
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorData {
    private String phones;//失败的手机号
    private String messages;//失败的短信消息
}
