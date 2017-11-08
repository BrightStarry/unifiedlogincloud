package com.zuma.smssender.service;

import com.zuma.smssender.entity.Platform;

/**
 * author:Administrator
 * datetime:2017/11/8 0008 10:43
 * 平台服务
 */
public interface PlatformService {
    Platform findOne(Long id);
}
