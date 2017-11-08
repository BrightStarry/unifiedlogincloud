package com.zuma.factory;

import com.google.gson.Gson;
import com.zuma.util.LogPathUtil;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

/**
 * author:ZhengXing
 * datetime:2017/11/1 0001 15:31
 * 正则对象Pattern工厂
 */

@Component
public class PatternFactory {

    private ThreadLocal<Pattern> pattern = new ThreadLocal<>();

    public Pattern build(){
        if (pattern.get() == null) {
            pattern.set(Pattern.compile(LogPathUtil.DATE_REGEXP));
        }
        return pattern.get();
    }
}
