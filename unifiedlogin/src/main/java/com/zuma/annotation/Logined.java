package com.zuma.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * author:ZhengXing
 * datetime:2017/10/18 0018 09:18
 * Controller层方法是否需要登录的判断注解
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Logined {
}
