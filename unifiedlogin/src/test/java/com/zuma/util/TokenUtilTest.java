package com.zuma.util;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * author:ZhengXing
 * datetime:2017/10/18 0018 10:07
 * 工具类测试
 */

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class TokenUtilTest {
    /**
     * 生成令牌测试
     */
    @Test
    public void testGeerate() {
        String token = TokenUtil.generate();
        log.info("token={}",token);
        Assert.assertNotNull(token);
    }
}
