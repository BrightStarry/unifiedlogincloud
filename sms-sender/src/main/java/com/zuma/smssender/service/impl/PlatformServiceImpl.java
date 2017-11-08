package com.zuma.smssender.service.impl;

import com.zuma.smssender.entity.Platform;
import com.zuma.smssender.enums.error.ErrorEnum;
import com.zuma.smssender.exception.SmsSenderException;
import com.zuma.smssender.repository.PlatformRepository;
import com.zuma.smssender.service.PlatformService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * author:Administrator
 * datetime:2017/11/8 0008 10:44
 */
@Service
@Slf4j
public class PlatformServiceImpl implements PlatformService {
    @Autowired
    private PlatformRepository platformRepository;

    /**
     * 根据id查询平台
     * @param id
     * @return
     */
    @Override
    public Platform findOne(Long id) {
        Platform platform = platformRepository.findOne(id);
        if(platform == null){
            log.error("【根据id查询平台】平台不存在.id={}",id);
            throw new SmsSenderException(ErrorEnum.PLATFORM_EMPTY);
        }

        return platform;
    }
}
