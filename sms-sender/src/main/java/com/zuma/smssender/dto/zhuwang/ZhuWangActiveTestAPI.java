package com.zuma.smssender.dto.zhuwang;

import com.zuma.smssender.enums.ZhuWangCommandIdEnum;
import com.zuma.smssender.util.ZhuWangUtil;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.*;

/**
 * author:ZhengXing
 * datetime:2017/11/24 0024 13:57
 * 链路检测接口api
 */
public interface ZhuWangActiveTestAPI {

    //请求
    @Data
    @ToString(callSuper = true)
    @NoArgsConstructor
    @EqualsAndHashCode(callSuper = false)
    class Request extends ZhuWangHeader {
        private byte reserved = 0;


        //指定sequence,用来响应
        public Request(ZhuWangCommandIdEnum commandIdEnum,Integer sequenceId){
            this.commandId = commandIdEnum.getCode();
            this.sequenceId = sequenceId== null ? ZhuWangUtil.getSequenceId() : sequenceId;
        }


        public byte[] toByteArray() throws IOException {
            @Cleanup ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            @Cleanup DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
            dataOutputStream.writeInt(this.getCommandId());
            dataOutputStream.writeInt(this.getSequenceId());
            dataOutputStream.writeByte(this.reserved);
            return byteArrayOutputStream.toByteArray();
        }
    }


    //响应
    @Data
    @ToString(callSuper = true)
    @AllArgsConstructor
    @NoArgsConstructor
    @EqualsAndHashCode(callSuper = false)
    class Response extends ZhuWangHeader {
        //一个1字节的空回复即可,该字节为空,无需解析
        private byte reserved;

        public Response(byte[] data) throws IOException {
            @Cleanup ByteArrayInputStream bins = new ByteArrayInputStream(data);
            @Cleanup DataInputStream dins = new DataInputStream(bins);
            ZhuWangUtil.setHeader(dins,this);
        }

    }
}