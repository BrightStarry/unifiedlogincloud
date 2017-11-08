package com.zuma.smssender.dto;

import com.zuma.smssender.enums.error.ErrorEnum;
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

    /**
     *  返回成功状态，以及数据
     */
    public static <T> ResultDTO<T> success(T data) {
        ResultDTO<T> resultDTO = new ResultDTO<T>();
        resultDTO.setCode(ErrorEnum.SUCCESS.getCode());
        resultDTO.setMessage(ErrorEnum.SUCCESS.getMessage());
        resultDTO.setData(data);
        return resultDTO;
    }

    /**
     * 返回成功状态，数据为空
     */
    public static ResultDTO<?> success(){
        return success(null);
    }

    /**
     * 返回错误状态， 包含错误状态码和错误消息
     */
    public static ResultDTO<?> error(String code, String msg) {
        ResultDTO resultDTO = new ResultDTO();
        resultDTO.setCode(code);
        resultDTO.setMessage(msg);
        return resultDTO;
    }
}
