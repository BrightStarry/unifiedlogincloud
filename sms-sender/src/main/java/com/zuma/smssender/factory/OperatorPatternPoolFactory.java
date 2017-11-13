package com.zuma.smssender.factory;

import com.zuma.smssender.config.Config;
import com.zuma.smssender.enums.PhoneOperatorEnum;

import java.util.concurrent.locks.ReentrantLock;
import java.util.regex.Pattern;

/**
 * author:Administrator
 * datetime:2017/11/13 0013 15:55
 * 运营商正则模式对象池
 */
public class OperatorPatternPoolFactory {

    private CommonFactory<Pattern> dianxinPatternPool = new BaseCommonPool<Pattern>(){
        @Override
        CommonPool initCommonPool() {
            return new CommonPool<Pattern>() {
                @Override
                Pattern create() {
                    return Pattern.compile(Config.DIANXIN_PHONE_NUMBER_REGEXP);
                }
            };
        }
    };

    private CommonFactory<Pattern> liantongPatternPool = new BaseCommonPool<Pattern>(){
        @Override
        CommonPool initCommonPool() {
            return new CommonPool<Pattern>() {
                @Override
                Pattern create() {
                    return Pattern.compile(Config.LIANTONG_PHONE_NUMBER_REGEXP);
                }
            };
        }
    };

    private CommonFactory<Pattern> yidongPatternPool = new BaseCommonPool<Pattern>(){
        @Override
        CommonPool initCommonPool() {
            return new CommonPool<Pattern>() {
                @Override
                Pattern create() {
                    return Pattern.compile(Config.YIDONG_PHONE_NUMBER_REGEXP);
                }
            };
        }
    };

    private CommonFactory[] pools = new CommonFactory[]{
            yidongPatternPool,
            dianxinPatternPool,
            liantongPatternPool
    };

    public CommonFactory<Pattern> build(PhoneOperatorEnum phoneOperatorEnum){
        return (CommonFactory<Pattern>)pools[phoneOperatorEnum.getCode()];
    }





    private OperatorPatternPoolFactory(){}
    private static OperatorPatternPoolFactory instance;
    private static ReentrantLock lock = new ReentrantLock();
    public static OperatorPatternPoolFactory getInstance(){
        if(instance == null){
            lock.lock();
            if(instance == null)
                instance = new OperatorPatternPoolFactory();
            lock.unlock();
        }
        return instance;
    }
}
