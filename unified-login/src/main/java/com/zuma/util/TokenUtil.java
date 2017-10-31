package com.zuma.util;

import org.springframework.util.DigestUtils;

import java.util.Random;

/**
 * author:ZhengXing
 * datetime:2017/10/18 0018 10:02
 * 令牌工具类
 */
public class TokenUtil {

    /**
     * 生成每个平台唯一的令牌
     * @param
     * @return
     */
    public static String generate() {
        String date = Long.valueOf(System.currentTimeMillis()).toString();
        String random = String.valueOf(new Random().nextInt(900000) + 100000);
        return random + date;
    }

    public static void main(String[] args) {
        String str = "111111" + "2004441508733612845";
        String s = DigestUtils.md5DigestAsHex(str.getBytes());
        System.out.println(s);
    }
}
