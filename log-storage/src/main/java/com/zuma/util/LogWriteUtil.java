package com.zuma.util;

import com.zuma.dto.LogMessage;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

/**
 * author:ZhengXing
 * datetime:2017/11/1 0001 17:07
 * 日志消息 转 文件 工具类
 */
@Component
@Slf4j
public class LogWriteUtil {


    private static LogPathUtil logPathUtil;
    @Autowired
    public void setLogPathUtil(LogPathUtil logPathUtil) {
        LogWriteUtil.logPathUtil = logPathUtil;
    }

    private  static final String LINE_BREAK = System.getProperty("line.separator");

    public static  void write(LogMessage logMessage) {
        try {
            //生成File
            File file = LogPathUtil.generate(logMessage);
            //如果生成失败,直接退出
            if (file == null)
                return;
            //生成成功，则进行io
            FileUtils.writeStringToFile(file,
                    logMessage.getContent() + LINE_BREAK,
                    CharsetUtil.UTF_8,
                    true);
        } catch (IOException e) {
            log.error("log写入文件异常。error={}",e.getMessage());
        }
    }

    public  void main(String[] args) {
        String a = "2017-11-01 13:08:21.122  INFO 7848 --- [ntLoopGroup-1-2] com.zuma.handler.ServerHandler           : server接收到bba283b0消息:LogMessage(serviceId=null, channel=null, moduleId=null, time=Wed Nov 01 14:08:21 CST 2017, content=2  17:19:39.663 [main] DEBUG io.netty.buffer.PooledByteBufAllocator - -Dio.netty.allocator.pageSize: 8192)";
        LogMessage logMessage = LogMessage.builder()
                .content(a)
                .channel(1)
                .moduleName("登录")
                .serviceId(1)
                .build();
        write(logMessage);
    }
}
