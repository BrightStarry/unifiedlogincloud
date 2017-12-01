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

    //修改记录状态
    void updateStatus(Long id, BooleanStatusEnum flag, String resultBody);

    //增加修改记录状态异步
    void updateStatusOfAsync(Long id, BooleanStatusEnum flag, String asyncResultBody);

    //根据id查询
    SmsSendRecord findOne(Long id);
}
