package com.zuma.factory;

import com.google.gson.Gson;
import com.zuma.util.LogPathUtil;

import java.util.regex.Pattern;

/**
 * author:Administrator
 * datetime:2017/11/1 0001 15:31
 * 正则对象Pattern工厂
 */
public class PatternFactory {

    private ThreadLocal<Pattern> pattern = new ThreadLocal<>();

    public Pattern build(){
        if (pattern.get() == null) {
            pattern.set(Pattern.compile(LogPathUtil.DATE_REGEXP));
        }
        return pattern.get();
    }


    private PatternFactory(){}
    //静态内部类
    private static class PatternFactoryBuilder{
        private static PatternFactory instance = new PatternFactory();
    }
    //获取实例
    public static PatternFactory getInstance(){
        return PatternFactory.PatternFactoryBuilder.instance;
    }
}
