package com.zuma.smssender.factory;

/**
 * author:Administrator
 * datetime:2017/11/8 0008 13:23
 * 工厂类接口
 * 方便后续将使用线程局部变量实现的factory换成pool形式
 */
public interface CommonFactory<T> {
    T borrow();

    void returnObj(T obj);
}
