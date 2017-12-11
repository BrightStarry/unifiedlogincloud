package com.zuma.smssender.enums;

import lombok.Getter;

/**
 * author:ZhengXing
 * datetime:2017/11/7 0007 16:44
 * 短信通道 枚举
 */
@Getter
public enum ChannelEnum implements CodeEnum<Integer> {
    UNKNOWN(-1,"未指定通道",null,null,null),
    ZHANG_YOU(0,"掌游",2,"zhangYou",new PhoneOperatorEnum[]{PhoneOperatorEnum.YIDONG}),
    KUAN_XIN(1,"宽信",1,"kuanXin",new PhoneOperatorEnum[]{PhoneOperatorEnum.YIDONG,PhoneOperatorEnum.LIANTONG,PhoneOperatorEnum.DIANXIN}),
    QUN_ZHENG(2,"群正",3,"qunZheng",new PhoneOperatorEnum[]{PhoneOperatorEnum.YIDONG}),
    ZHU_WANG(3,"筑望",4,"zhuWang",new PhoneOperatorEnum[]{PhoneOperatorEnum.YIDONG}),
    CHANG_XIANG(4, "畅想", 5, "changXiang", new PhoneOperatorEnum[]{PhoneOperatorEnum.YIDONG}),

    ;
    private Integer code;
    private String message;
    private Integer order;//优先级,从小到大
    private String key;//用来选定spring容器中对应名字的bean
    private PhoneOperatorEnum[] phoneOperatorSupport;//支持的运营商数组

    ChannelEnum(Integer code, String message, Integer order, String key, PhoneOperatorEnum[] phoneOperatorSupport) {
        this.code = code;
        this.message = message;
        this.order = order;
        this.key = key;
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
