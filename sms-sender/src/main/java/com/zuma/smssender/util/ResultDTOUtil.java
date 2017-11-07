package com.zuma.smssender.util;


import com.zuma.smssender.dto.ResultDTO;
import com.zuma.smssender.enums.error.ErrorEnum;

/**
 * author:ZhengXing
 * datetime:2017/10/18 0018 10:02
 * 返回对象工具类
 */
public class ResultDTOUtil {
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
