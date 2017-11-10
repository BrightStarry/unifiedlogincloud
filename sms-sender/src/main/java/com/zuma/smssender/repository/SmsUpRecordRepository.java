package com.zuma.smssender.repository;

import com.zuma.smssender.entity.SmsUpRecord;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * author:Administrator
 * datetime:2017/11/10 0010 13:03
 */

public interface SmsUpRecordRepository extends JpaRepository<SmsUpRecord,Long> {
}
