package com.zuma.smssender.factory;

import com.zuma.smssender.config.Config;
import org.springframework.stereotype.Component;

import java.util.concurrent.locks.ReentrantLock;
import java.util.regex.Pattern;

/**
 * author:ZhengXing
 * datetime:2017/11/1 0001 15:31
 * 正则对象Pattern工厂
 */

public class LianTongPatternFactory implements CommonFactory<Pattern> {

    private ThreadLocal<Pattern> pattern = new ThreadLocal<>();

    public Pattern build(){
        if (pattern.get() == null) {
            pattern.set(Pattern.compile(Config.LIANTONG_PHONE_NUMBER_REGEXP));
        }
        return pattern.get();
    }

    private LianTongPatternFactory(){}
    private static LianTongPatternFactory instance;
    private static ReentrantLock lock = new ReentrantLock();
    public static LianTongPatternFactory getInstance(){
        if(instance == null){
            lock.lock();
            if(instance == null)
                instance = new LianTongPatternFactory();
            lock.unlock();
        }
        return instance;
    }
}
