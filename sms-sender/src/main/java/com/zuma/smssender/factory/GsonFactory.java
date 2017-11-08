package com.zuma.smssender.factory;

import com.google.gson.Gson;
import com.zuma.smssender.util.XmlResolver;

import java.util.concurrent.locks.ReentrantLock;

/**
 * author:ZhengXing
 * datetime:2017/11/1 0001 11:18
 * Gson对象工厂-创建线程私有gson
 */
public class GsonFactory implements CommonFactory<Gson>{
    private ThreadLocal<Gson> gson = new ThreadLocal<Gson>();

    public Gson build(){
        if (gson.get() == null) {
            gson.set(new Gson());
        }
        return gson.get();
    }


    private GsonFactory(){}
    private static GsonFactory instance;
    private static ReentrantLock lock = new ReentrantLock();
    public static GsonFactory getInstance(){
        if(instance == null){
            lock.lock();
            if(instance == null)
                instance = new GsonFactory();
            lock.unlock();
        }
        return instance;
    }
}
