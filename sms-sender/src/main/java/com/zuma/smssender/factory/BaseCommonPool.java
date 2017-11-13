package com.zuma.smssender.factory;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

/**
 * author:Administrator
 * datetime:2017/11/13 0013 14:14
 * 组合CommonPool，简单实现{@link CommonFactory}接口
 */
@Slf4j
public abstract class BaseCommonPool<T> implements CommonFactory<T>{

    protected GenericObjectPool<T> pool;

    public BaseCommonPool() {
        initPool();
    }

    void initPool(){
        CommonPool<T> commonPool = initCommonPool();
        pool = new GenericObjectPool<T>(commonPool,getPoolConfig());
    }

    //默认获取config类方法，可在子类中重写
    GenericObjectPoolConfig getPoolConfig(){
        GenericObjectPoolConfig config = new GenericObjectPoolConfig();
        config.setMinIdle(0);
        config.setMaxTotal(30);
        config.setMaxIdle(3);
        return config;
    }

    abstract CommonPool<T> initCommonPool();


    @Override
    public T borrow() {
        try {
            return pool.borrowObject();
        } catch (Exception e) {
            log.error("【pool】对象借用失败.error={}",e.getMessage(),e);
        }
        return null;
    }

    @Override
    public void returnObj(T obj) {
        pool.returnObject(obj);
    }
}
