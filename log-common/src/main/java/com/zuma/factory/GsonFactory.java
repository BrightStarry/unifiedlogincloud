package com.zuma.factory;

import com.google.gson.Gson;

/**
 * author:ZhengXing
 * datetime:2017/11/1 0001 11:18
 * Gson对象工厂-创建线程私有gson
 */
public class GsonFactory {
    private ThreadLocal<Gson> gson = new ThreadLocal<Gson>();

    public Gson build(){
        if (gson.get() == null) {
            gson.set(new Gson());
        }
        return gson.get();
    }


    private GsonFactory(){}
    //静态内部类
    private static class GsonFactoryBuilder{
        private static GsonFactory instance = new GsonFactory();
    }
    //获取实例
    public static GsonFactory getInstance(){
        return GsonFactoryBuilder.instance;
    }
}
