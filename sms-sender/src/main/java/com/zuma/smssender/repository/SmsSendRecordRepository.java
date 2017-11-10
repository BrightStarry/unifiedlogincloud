package com.zuma.smssender.repository;

import com.zuma.smssender.entity.SmsSendRecord;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * author:Administrator
 * datetime:2017/11/10 0010 15:22
 * 短信发送记录
 */
public interface SmsSendRecordRepository extends JpaRepository<SmsSendRecord,Long> {
}
