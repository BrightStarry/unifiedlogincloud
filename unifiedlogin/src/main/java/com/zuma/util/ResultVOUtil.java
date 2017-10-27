package com.zuma.util;


import com.zuma.enums.error.ErrorEnum;
import com.zuma.vo.ResultVO;

/**
 * author:ZhengXing
 * datetime:2017/10/18 0018 10:02
 * 返回对象工具类
 */
public class ResultVOUtil {
    /**
     *  返回成功状态，以及数据
     */
    public static <T> ResultVO<T> success(T data) {
        ResultVO<T> resultVO = new ResultVO<T>();
        resultVO.setCode(ErrorEnum.SUCCESS.getCode());
        resultVO.setMessage(ErrorEnum.SUCCESS.getMessage());
        resultVO.setData(data);
        return resultVO;
    }

    /**
     * 返回成功状态，数据为空
     */
    public static ResultVO<?> success(){
        return success(null);
    }

    /**
     * 返回错误状态， 包含错误状态码和错误消息
     */
    public static ResultVO<?> error(String code, String msg) {
        ResultVO resultVO = new ResultVO();
        resultVO.setCode(code);
        resultVO.setMessage(msg);
        return resultVO;
    }
}
