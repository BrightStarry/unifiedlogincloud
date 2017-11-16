package com.zuma.util;

import com.zuma.enums.CodeEnum;
import com.zuma.enums.ServiceEnum;

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
            if (each.getCode().equals(code)) {
                return each;
            }
        }
        return null;
    }

    public static void main(String[] args) {
        System.out.println(EnumUtil.getByCode(1, ServiceEnum.class).getMessage());
    }

}
