package com.zuma.repository;

import com.zuma.domain.Platform;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * author:ZhengXing
 * datetime:2017/10/16 0016 17:29
 * 平台数据操作
 */
public interface PlatformRepository extends JpaRepository<Platform,Long>{
    /**
     * 根据名字查询平台
     */
    Platform findTopByName(String name);

    /**
     * 根据令牌查询平台
     */
    Platform findTopByToken(String token);


}
