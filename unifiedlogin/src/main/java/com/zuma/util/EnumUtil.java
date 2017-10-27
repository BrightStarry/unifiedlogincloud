package com.zuma.util;

import com.zuma.enums.ChannelEnum;
import com.zuma.enums.CodeEnum;

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

    public static void main(String[] args) {
        ChannelEnum channelEnum = EnumUtil.getByCode(1, ChannelEnum.class);
        System.out.println(channelEnum.getMessage());
    }
}
