package com.zuma.smssender.config;

import com.google.common.collect.Lists;
import com.zuma.smssender.enums.ChannelEnum;
import com.zuma.smssender.enums.PhoneOperatorEnum;
import com.zuma.smssender.util.HttpClientUtil;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

/**
 * author:ZhengXing
 * datetime:2017/11/7 0007 14:43
 * 帐号集合.使用二维数组来获取对应 通道和运营商 的帐号
 */
@Data
@Component
public class SmsAccountCollection {
    //帐号数组， 根据[通道code][运营商code]
    private CommonSmsAccount[][] accounts = new CommonSmsAccount[5][5];

    private SmsAccountCollection() {
        accounts
                [ChannelEnum.ZHANG_YOU.getCode()]
                [PhoneOperatorEnum.YIDONG.getCode()] = CommonSmsAccount.builder()
                .channel(String.valueOf(ChannelEnum.ZHANG_YOU.getCode()))
                .type(PhoneOperatorEnum.YIDONG.getCode())
                .aKey("10010317")
                .bKey("710317")
                .cKey("asdfg123456ghjjjjjkh")
                .build();

        //因为宽信帐号可以支持所有运营商，所以如此赋值
        CommonSmsAccount kuanxin = CommonSmsAccount.builder()
                .channel(String.valueOf(ChannelEnum.KUAN_XIN.getCode()))
                .type(PhoneOperatorEnum.ALL.getCode())
                .aKey("387568")
                .bKey("84f26c091438461bb01fcd021da1c197")
                .cKey("")
                .build();
        accounts
                [ChannelEnum.KUAN_XIN.getCode()]
                [PhoneOperatorEnum.ALL.getCode()] = kuanxin;
        accounts
                [ChannelEnum.KUAN_XIN.getCode()]
                [PhoneOperatorEnum.YIDONG.getCode()] = kuanxin;
        accounts
                [ChannelEnum.KUAN_XIN.getCode()]
                [PhoneOperatorEnum.DIANXIN.getCode()] = kuanxin;
        accounts
                [ChannelEnum.KUAN_XIN.getCode()]
                [PhoneOperatorEnum.LIANTONG.getCode()] = kuanxin;


        //群正
        accounts
                [ChannelEnum.QUN_ZHENG.getCode()]
                [PhoneOperatorEnum.YIDONG.getCode()] = CommonSmsAccount.builder()
                .channel(String.valueOf(ChannelEnum.QUN_ZHENG.getCode()))
                .type(PhoneOperatorEnum.YIDONG.getCode())
                .aKey("hzzmkjyzm")
                .bKey("YBpFJzkc2q170501")
                .cKey("")
                .build();

        //筑望
        accounts
                [ChannelEnum.ZHU_WANG.getCode()]
                [PhoneOperatorEnum.YIDONG.getCode()] = CommonSmsAccount.builder()
                .channel(String.valueOf(ChannelEnum.QUN_ZHENG.getCode()))
                .type(PhoneOperatorEnum.YIDONG.getCode())
                .aKey("944027")//企业id
                .bKey("944027")//企业帐号
                .cKey("SVPOUXJLYD")//企业密码
                .build();
//        accounts
//                [ChannelEnum.ZHU_WANG.getCode()]
//                [PhoneOperatorEnum.YIDONG.getCode()] = CommonSmsAccount.builder()
//                .channel(String.valueOf(ChannelEnum.QUN_ZHENG.getCode()))
//                .type(PhoneOperatorEnum.YIDONG.getCode())
//                .aKey("387843")//企业id
//                .bKey("387843")//企业帐号
//                .cKey("zuma#387843")//企业密码
//                .build();

        //畅想
        accounts
                [ChannelEnum.CHANG_XIANG.getCode()]
                [PhoneOperatorEnum.YIDONG.getCode()] = CommonSmsAccount.builder()
                .channel(String.valueOf(ChannelEnum.CHANG_XIANG.getCode()))
                .type(PhoneOperatorEnum.YIDONG.getCode())
                .aKey("zmkj")//帐号
                .bKey("zmkj")//密码
                .build();

    }

    /**
     * 单例
     */
    private static SmsAccountCollection instance;
    private static ReentrantLock lock = new ReentrantLock();
    public static SmsAccountCollection getInstance() {
        if (instance == null) {
            lock.lock();
            instance = new SmsAccountCollection();
            lock.unlock();
        }
        return instance;
    }

}
