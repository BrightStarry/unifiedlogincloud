package com.zuma.smssender.config;

import com.google.common.collect.Lists;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * author:ZhengXing
 * datetime:2017/11/7 0007 14:43
 * 帐号集合
 */
@Component
@Data
public class SmsAccountCollection {
    private List<CommonSmsAccount> accountList;

    public SmsAccountCollection() {
        CommonSmsAccount account1 = CommonSmsAccount.builder()
                .channel("1")
                .type("1")
                .aKey("10010317")
                .bKey("710317")
                .cKey("asdfg123456ghjjjjjkh")
                .build();
        CommonSmsAccount account2 = CommonSmsAccount.builder()
                .channel("2")
                .type("1")
                .aKey("387568")
                .bKey("84f26c091438461bb01fcd021da1c197")
                .cKey("")
                .build();
        CommonSmsAccount account3 = CommonSmsAccount.builder()
                .channel("2")
                .type("1")
                .aKey("hzzmkjyzm")
                .bKey("YBpFJzkc2q170501")
                .cKey("")
                .build();
        Lists.newArrayList(account1,account2,account3);
    }

}
