package com.zuma.smssender.util;

import com.zuma.smssender.enums.PhoneOperatorEnum;
import com.zuma.smssender.enums.error.ErrorEnum;
import com.zuma.smssender.exception.SmsSenderException;
import com.zuma.smssender.factory.CommonFactory;
import com.zuma.smssender.factory.DianXinPatternFactory;
import com.zuma.smssender.factory.LianTongPatternFactory;
import com.zuma.smssender.factory.YiDongPatternFactory;
import lombok.extern.slf4j.Slf4j;

import java.util.regex.Pattern;

/**
 * author:Administrator
 * datetime:2017/11/8 0008 13:36
 * 手机号工具类
 */
@Slf4j
public class PhoneUtil {
    private static CommonFactory<Pattern> yidongPatternFactory = YiDongPatternFactory.getInstance();
    private static CommonFactory<Pattern> liantongPatternFactory = LianTongPatternFactory.getInstance();
    private static CommonFactory<Pattern> dianxinPatternFactory = DianXinPatternFactory.getInstance();

    /**
     * 根据手机号判断其运营商
     */
    public static PhoneOperatorEnum[] getPhoneOperator(String... phones){
        //返回数组
        PhoneOperatorEnum[] result = new PhoneOperatorEnum[phones.length];
        for (int i=0 ;i < phones.length; i++){
            // 责任链模式...
            //获取pattern
            Pattern yidongPattern = yidongPatternFactory.build();
            Pattern liantongPattern = liantongPatternFactory.build();
            Pattern dianxinPattern = dianxinPatternFactory.build();
            //如果匹配
            result[i] = yidongPattern.matcher(phones[i]).matches() ? PhoneOperatorEnum.YIDONG :
                    liantongPattern.matcher(phones[i]).matches() ? PhoneOperatorEnum.LIANTONG :
                            dianxinPattern.matcher(phones[i]).matches() ? PhoneOperatorEnum.DIANXIN :
                                    PhoneOperatorEnum.UNKNOWN;
            //如果运营商未知
            if(result[i].equals(PhoneOperatorEnum.UNKNOWN)){
                log.error("【根据手机号判断运营商】运营商未知。phone={}",phones[i]);
                throw new SmsSenderException(ErrorEnum.PHONE_UNKNOWN);
            }
        }
        return result;
    }
}
