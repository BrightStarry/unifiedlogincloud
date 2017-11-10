package com.zuma.smssender.service;

import com.zuma.smssender.entity.SmsSendRecord;
import com.zuma.smssender.enums.BooleanStatusEnum;

/**
 * author:Administrator
 * datetime:2017/11/10 0010 15:24
 * 短信发送记录
 */
public interface SmsSendRecordService {
    //保存
    SmsSendRecord save(SmsSendRecord smsSendRecord);



    //根据id查询
    SmsSendRecord findOne(Long id);
}
