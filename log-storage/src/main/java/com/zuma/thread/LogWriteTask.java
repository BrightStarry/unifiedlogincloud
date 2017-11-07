package com.zuma.thread;

import com.zuma.dto.LogMessage;
import com.zuma.executor.LogWriteExecutor;
import com.zuma.util.LogWriteUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * author:ZhengXing
 * datetime:2017/11/2 0002 10:40
 * 日志写入任务
 */
@Slf4j
public class LogWriteTask implements Runnable {
    private String threadName;
    private LinkedBlockingQueue<LogMessage> logQueue;
    private LogWriteUtil logWriteUtil;

    public LogWriteTask(LinkedBlockingQueue<LogMessage> logQueue, LogWriteUtil logWriteUtil) {
        this.logQueue = logQueue;
        this.logWriteUtil = logWriteUtil;
    }


    @Override
    public void run() {
        try {
            while (true) {
                //如果线程池停止，退出
                if(LogWriteExecutor.isStop())
                    return;
                //阻塞从队列获取log
                LogMessage logMessage = logQueue.take();
                //写入操作
                LogWriteUtil.write(logMessage);
            }
        } catch (InterruptedException e) {
            log.error("线程任务中断。error={}",e.getMessage(),e);
        }
    }
}
