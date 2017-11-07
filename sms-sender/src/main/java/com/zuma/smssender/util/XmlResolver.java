package com.zuma.smssender.util;

import java.util.concurrent.locks.ReentrantLock;

/**
 * author:ZhengXing
 * datetime:2017/11/7 0007 14:04
 * xml解析器
 */
public class XmlResolver {



    /**
     * 单例
     */
    private static XmlResolver instance;
    private static ReentrantLock lock = new ReentrantLock();
    private XmlResolver() {};
    public static XmlResolver getInstance(){
        if(instance == null){
            lock.lock();
            if(instance == null)
                instance = new XmlResolver();
            lock.unlock();
        }
        return instance;
    }
}
