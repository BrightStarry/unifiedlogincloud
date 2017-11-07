package com.zuma.smssender.config;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;

import static org.junit.Assert.*;

/**
 * author:ZhengXing
 * datetime:2017/11/7 0007 14:48
 * 配置参数注入测试
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class SmsAccountCollectionTest {
    @Autowired
    private SmsAccountCollection smsAccountCollection;
    @Autowired
    private CommonSmsAccount commonSmsAccount;
    @Test
    public void test1() {
        log.info("aaaa,{}",commonSmsAccount);
        Assert.assertNotEquals(0,smsAccountCollection.getAccountList().size());
        for (Map<String,String> map :smsAccountCollection.getAccountList()){
            for(Map.Entry<String,String> entry : map.entrySet())
            log.info("temp,key:{},value:{}",entry.getKey(),entry.getValue());
        }
    }
}