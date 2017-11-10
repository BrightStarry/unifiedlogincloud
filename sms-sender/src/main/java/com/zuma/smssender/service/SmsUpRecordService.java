package com.zuma.smssender.service;

import com.zuma.smssender.entity.SmsUpRecord;

/**
 * author:Administrator
 * datetime:2017/11/10 0010 13:08
 * 短信上行记录实体service
 */
public interface SmsUpRecordService {
    //保存
    void save(SmsUpRecord smsUpRecord);
}
