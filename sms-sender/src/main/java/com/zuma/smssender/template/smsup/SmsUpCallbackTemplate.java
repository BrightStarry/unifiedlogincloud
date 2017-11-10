package com.zuma.smssender.template.smsup;

import com.google.gson.Gson;
import com.zuma.smssender.entity.SmsUpRecord;
import com.zuma.smssender.factory.CommonFactory;
import com.zuma.smssender.factory.GsonFactory;
import com.zuma.smssender.service.SmsUpRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * author:Administrator
 * datetime:2017/11/10 0010 12:43
 * 短信上行处理模版类
 */
@Slf4j
@Component
public abstract class SmsUpCallbackTemplate<T> {

    protected CommonFactory<Gson> gsonCommonFactory = GsonFactory.getInstance();

    private static SmsUpRecordService smsUpRecordService;
    @Autowired
    public  void setSmsUpRecordService(SmsUpRecordService smsUpRecordService) {
        SmsUpCallbackTemplate.smsUpRecordService = smsUpRecordService;
    }

    //处理
    public boolean callbackHandle(T response){
        //T 转 上行记录实体
        SmsUpRecord smsUpRecord = responseToSmsUpRecord(response);
        //入库
        saveSmsUpRecord(smsUpRecord);
        return true;
    }

    //T 转 上行记录实体
    abstract SmsUpRecord responseToSmsUpRecord(T response);

    //入库
    void saveSmsUpRecord(SmsUpRecord smsUpRecord) {
        smsUpRecordService.save(smsUpRecord);
    }
}
