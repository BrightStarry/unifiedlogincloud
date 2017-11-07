package com.zuma.factory;

import com.esotericsoftware.kryo.Kryo;

/**
 * author:ZhengXing
 * datetime:2017/11/2 0002 16:59
 * Kryo序列化对象工厂
 */
public class KryoFactory implements com.esotericsoftware.kryo.pool.KryoFactory{

    private ThreadLocal<Kryo> kryo = new ThreadLocal<>();

    private KryoFactory(){}

    @Override
    public Kryo create() {
        if (kryo.get() == null) {
            kryo.set(new Kryo());
        }
        return kryo.get();
    }

    //静态内部类
    private static class KryoFactoryBuilder{
        private static KryoFactory instance = new KryoFactory();
    }
    //获取实例
    public static KryoFactory getInstance(){
        return KryoFactoryBuilder.instance;
    }
}
