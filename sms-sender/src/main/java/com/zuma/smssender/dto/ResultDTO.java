package com.zuma.smssender.dto;

import lombok.Data;

/**
 * author:ZhengXing
 * datetime:2017/10/16 0016 17:02
 * 返回对象
 */


@Data
public class ResultDTO<T> {
    /**状态码*/
    private String code;
    /**消息*/
    private String message;
    /**数据*/
    private T data;
}
