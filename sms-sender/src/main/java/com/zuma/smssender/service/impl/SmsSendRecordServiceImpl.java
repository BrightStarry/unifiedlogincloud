package com.zuma.smssender.service.impl;

import com.zuma.smssender.entity.SmsSendRecord;
import com.zuma.smssender.enums.BooleanStatusEnum;
import com.zuma.smssender.enums.error.ErrorEnum;
import com.zuma.smssender.exception.SmsSenderException;
import com.zuma.smssender.repository.SmsSendRecordRepository;
import com.zuma.smssender.service.SmsSendRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * author:Administrator
 * datetime:2017/11/10 0010 15:40
 * 短信发送记录
 */
@Service
@Slf4j
public class SmsSendRecordServiceImpl implements SmsSendRecordService {

    @Autowired
    private SmsSendRecordRepository smsSendRecordRepository;

    @Override
    public SmsSendRecord save(SmsSendRecord smsSendRecord) {
        return smsSendRecordRepository.save(smsSendRecord);
    }


    @Override
    public SmsSendRecord findOne(Long id) {
        return smsSendRecordRepository.findOne(id);
    }

    @Override
    public void updateStatus(Long id,BooleanStatusEnum flag,String resultBody){
        SmsSendRecord record = findOne(id);
        record.setIsSuccess(flag.getCode())
                .setResultBody(resultBody);
        save(record);
    }

    @Override
    public void updateStatusOfAsync(Long id, BooleanStatusEnum flag, String asyncResultBody) {
        SmsSendRecord record = findOne(id);
        record.setIsSuccess(flag.getCode())
                .setAsyncResultBody(asyncResultBody);
        save(record);
    }
}
