package com.zuma.smssender.socket;

import com.zuma.smssender.enums.error.ErrorEnum;
import com.zuma.smssender.exception.SmsSenderException;
import io.netty.channel.Channel;
import lombok.Data;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * author:ZhengXing
 * datetime:2017/11/30 0030 14:32
 * SocketPair仓库
 */
@Data
public class SocketStore {
    //线程安全,适合读多写少场景的list集合
    private static List<SocketPair> socketPairs = new CopyOnWriteArrayList<>();

    /**
     * 增加socketPair
     */
    public static void add(SocketPair socketPair) {
        socketPairs.add(socketPair);
    }

    /**
     * 获取指定索引的socketPair
     */
    public static SocketPair get(int i) {
        return socketPairs.get(i);
    }

    /**
     * 返回当前并发数最少的socket
     */
    public static  SocketPair getBestSocketPair() {
        SocketPair bestSocketPair = null;//最好对象
        //遍历出并发数最少的socket
        for (SocketPair item : socketPairs) {
            //循环时顺便判断是否关闭,关闭了就删除
            //该集合是可以安全删除的
            if(!item.isOpen())
                socketPairs.remove(item);
            if (bestSocketPair == null || item.getCurrentQPS() < bestSocketPair.getCurrentQPS())
                bestSocketPair = item;
        }
        //如果为空表示没有可用socket,抛出异常
        if (bestSocketPair == null) {
            throw new SmsSenderException(ErrorEnum.NON_AVAILABLE_SOCKET);
        }
        return bestSocketPair;
    }

}
