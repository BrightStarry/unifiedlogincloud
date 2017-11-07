package com.zuma.smssender.util;


import com.zuma.smssender.enums.CodeEnum;

/**
 * author:ZhengXing
 * datetime:2017/10/18 0018 10:02
 * 枚举工具类
 */
public class EnumUtil {

    /**
     * 根据Code返回枚举
     * @param code
     * @param enumClass
     * @param <T>
     * @return
     */
    public static <T extends CodeEnum> T getByCode(Integer code, Class<T> enumClass) {
        for (T each : enumClass.getEnumConstants()) {
            if (code.equals(each.getCode())) {
                return each;
            }
        }
        return null;
    }


}
