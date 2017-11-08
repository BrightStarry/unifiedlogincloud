package com.zuma.smssender.factory;

import com.zuma.smssender.config.Config;

import java.util.concurrent.locks.ReentrantLock;
import java.util.regex.Pattern;

/**
 * author:ZhengXing
 * datetime:2017/11/1 0001 15:31
 * 正则对象Pattern工厂
 */

public class DianXinPatternFactory implements CommonFactory<Pattern> {

    private ThreadLocal<Pattern> pattern = new ThreadLocal<>();

    public Pattern build(){
        if (pattern.get() == null) {
            pattern.set(Pattern.compile(Config.DIANXIN_PHONE_NUMBER_REGEXP));
        }
        return pattern.get();
    }

    private DianXinPatternFactory(){}
    private static DianXinPatternFactory instance;
    private static ReentrantLock lock = new ReentrantLock();
    public static DianXinPatternFactory getInstance(){
        if(instance == null){
            lock.lock();
            if(instance == null)
                instance = new DianXinPatternFactory();
            lock.unlock();
        }
        return instance;
    }
}
