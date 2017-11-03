package com.zuma.executor;

import com.zuma.config.LogServerConfig;
import com.zuma.dto.LogMessage;
import com.zuma.thread.LogWriteTask;
import com.zuma.util.LogWriteUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.*;

/**
 * author:Administrator
 * datetime:2017/11/1 0001 18:01
 *
 * 日志写入任务执行器
 */

@Component
@Slf4j
public class LogWriteExecutor {
    //线程池
    private ThreadPoolExecutor threadPoolExecutor;
    //日志队列
    private LinkedBlockingQueue<LogMessage> logQueue;
    //是否停止
    private static boolean stop = false;

    @Autowired
    private LogServerConfig logServerConfig;

    @Autowired
    private LogWriteUtil logWriteUtil;

    //增加log到队列
    public void addLogToQueue(LogMessage logMessage){
        try {
            logQueue.offer(logMessage, 3, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            log.error("增加log到队列超时。logQueueSize={}",logQueue.size());
        }
    }

    //运行任务
    private void start() {
        for (int i = 0; i < logServerConfig.getThreadNum(); i++) {
            stop = false;
            threadPoolExecutor.submit(new LogWriteTask(logQueue,logWriteUtil));
        }
    }
    //停止任务
    public void stop() {
        stop = true;
        threadPoolExecutor.shutdown();
    }

    //是否停止
    public static boolean isStop(){
        return stop;
    }



    /**
     * 线程池异常处理方法
     * @param r
     * @param t
     */
    private  void restart(Runnable r, Throwable t) {

        //因为全是无线循环，所以如果停止一定是发生了异常,重启
        if(!isStop()){
            threadPoolExecutor.execute(new LogWriteTask(logQueue,logWriteUtil));
            log.error("线程任务停止，重新开启");
            log.error("当前线程数:{}，队列线程数:{}",threadPoolExecutor.getActiveCount(),threadPoolExecutor.getQueue().size());
        }
    }

    /**
     * 初始化
     */
    public void init () {
        /**
         * 1.log直接放入队列，开启固定线程循环跑，如果队列为空则等待
         * 2.netty每收到一个log直接开启一个线程跑。
         */
        threadPoolExecutor =
                new ThreadPoolExecutor(
                        logServerConfig.getThreadNum(),//核心线程数
                        logServerConfig.getMaxThreadNum(),//最大线程数
                        60,//线程空闲超时时间
                        TimeUnit.SECONDS,//秒
                        new LinkedBlockingQueue<Runnable>(),//线程队列
                        new RejectedExecutionHandler() {//线程拒绝策略-日志记录，并扩容最大线程数-暂时基本算是无效
                            @Override
                            public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                                log.error("线程池满，任务被拒绝!!!");
                                executor.setMaximumPoolSize(logServerConfig.getMaxThreadNum());
                            }
                        }
                ){//重写线程结束处理方法
                    @Override
                    protected void afterExecute(Runnable r, Throwable t) {
                        super.afterExecute(r, t);
                        restart(r, t);
                    }
                };
        //允许核心线程数超时
        threadPoolExecutor.allowCoreThreadTimeOut(true);

        //队列-可固定大小，超出后增加线程。
        logQueue = new LinkedBlockingQueue<>();

        start();
    }
}
