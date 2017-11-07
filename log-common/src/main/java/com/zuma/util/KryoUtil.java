package com.zuma.util;


import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.zuma.factory.KryoFactory;
import lombok.Cleanup;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * author:ZhengXing
 * datetime:2017/11/2 0002 17:02
 * 序列化工具类
 */
public class KryoUtil {

    private static KryoFactory kryoFactory = KryoFactory.getInstance();

    /**
     * 对象转byte[]，注意，如果对象过大，可能会引发kyro缓冲区错误，需要自定义其缓冲区
     * @param obj
     * @param <T>
     * @return
     */
    public static <T> byte[]  Object2Byte(T obj) throws IOException{
        Kryo kryo = kryoFactory.create();
//        @Cleanup
//        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        @Cleanup
        Output output = new Output(4096,Integer.MAX_VALUE);
        kryo.writeObject(output, obj);
        return output.toBytes();
    }

    public static <T> T byte2Object(byte[] buf,Class<T> cla)throws IOException{
        Kryo kryo = kryoFactory.create();
        @Cleanup
        ByteArrayInputStream inputStream = new ByteArrayInputStream(buf);
        @Cleanup
        Input input = new Input(inputStream,4096);
        return kryo.readObject(input, cla);
    }


}
