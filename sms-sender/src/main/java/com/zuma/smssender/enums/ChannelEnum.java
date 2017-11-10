package com.zuma.smssender.enums;

import lombok.Getter;

/**
 * author:ZhengXing
 * datetime:2017/11/7 0007 16:44
 * 短信通道 枚举
 */
@Getter
public enum ChannelEnum implements CodeEnum<Integer> {
    UNKNOWN(0,"未指定通道",null,null),
    ZHANG_YOU(1,"掌游",2,new PhoneOperatorEnum[]{PhoneOperatorEnum.YIDONG}),
    KUAN_XIN(2,"宽信",1,new PhoneOperatorEnum[]{PhoneOperatorEnum.YIDONG,PhoneOperatorEnum.LIANTONG,PhoneOperatorEnum.DIANXIN}),
    QUN_ZHENG(3,"群正",3,new PhoneOperatorEnum[]{PhoneOperatorEnum.YIDONG})
    ;
    private Integer code;
    private String message;
    private Integer order;//优先级,从小到大
    private PhoneOperatorEnum[] phoneOperatorSupport;//支持的运营商数组

    ChannelEnum(Integer code, String message, Integer order, PhoneOperatorEnum[] phoneOperatorSupport) {
        this.code = code;
        this.message = message;
        this.order = order;
        this.phoneOperatorSupport = phoneOperatorSupport;
    }

    /**
     * 根据优先级获取channel
     * @param order
     */
    public static ChannelEnum getChannelEnum(Integer order){
        for (ChannelEnum each : ChannelEnum.class.getEnumConstants()) {
            if(order.equals(each.getOrder()))
                return each;
        }
        return null;
    }

}
