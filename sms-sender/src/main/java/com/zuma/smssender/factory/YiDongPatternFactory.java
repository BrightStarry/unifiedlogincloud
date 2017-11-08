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

public class YiDongPatternFactory implements CommonFactory<Pattern> {

    private ThreadLocal<Pattern> pattern = new ThreadLocal<>();

    public Pattern build(){
        if (pattern.get() == null) {
            pattern.set(Pattern.compile(Config.YIDONG_PHONE_NUMBER_REGEXP));
        }
        return pattern.get();
    }

    private YiDongPatternFactory(){}
    private static YiDongPatternFactory instance;
    private static ReentrantLock lock = new ReentrantLock();
    public static YiDongPatternFactory getInstance(){
        if(instance == null){
            lock.lock();
            if(instance == null)
                instance = new YiDongPatternFactory();
            lock.unlock();
        }
        return instance;
    }
}
