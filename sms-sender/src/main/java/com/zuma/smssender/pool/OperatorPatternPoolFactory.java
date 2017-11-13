package com.zuma.smssender.pool;

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

    private CommonPool<Pattern> dianxinPatternPool = new BaseCommonPool<Pattern>(){
        @Override
        SimpleObjectFactory initCommonPool() {
            return new SimpleObjectFactory<Pattern>() {
                @Override
                Pattern create() {
                    return Pattern.compile(Config.DIANXIN_PHONE_NUMBER_REGEXP);
                }
            };
        }
    };

    private CommonPool<Pattern> liantongPatternPool = new BaseCommonPool<Pattern>(){
        @Override
        SimpleObjectFactory initCommonPool() {
            return new SimpleObjectFactory<Pattern>() {
                @Override
                Pattern create() {
                    return Pattern.compile(Config.LIANTONG_PHONE_NUMBER_REGEXP);
                }
            };
        }
    };

    private CommonPool<Pattern> yidongPatternPool = new BaseCommonPool<Pattern>(){
        @Override
        SimpleObjectFactory initCommonPool() {
            return new SimpleObjectFactory<Pattern>() {
                @Override
                Pattern create() {
                    return Pattern.compile(Config.YIDONG_PHONE_NUMBER_REGEXP);
                }
            };
        }
    };

    private CommonPool[] pools = new CommonPool[]{
            yidongPatternPool,
            dianxinPatternPool,
            liantongPatternPool
    };

    public CommonPool<Pattern> build(PhoneOperatorEnum phoneOperatorEnum){
        return (CommonPool<Pattern>)pools[phoneOperatorEnum.getCode()];
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
