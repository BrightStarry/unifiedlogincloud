package com.zuma.smssender.service.impl;

import com.zuma.smssender.entity.SmsUpRecord;
import com.zuma.smssender.repository.SmsUpRecordRepository;
import com.zuma.smssender.service.SmsUpRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * author:Administrator
 * datetime:2017/11/10 0010 13:10
 * 短信上行记录service
 */
@Service
public class SmsUpRecordServiceImpl implements SmsUpRecordService {

    @Autowired
    private SmsUpRecordRepository smsUpRecordRepository;

    @Override
    public void save(SmsUpRecord smsUpRecord) {
        smsUpRecordRepository.save(smsUpRecord);
    }
}
