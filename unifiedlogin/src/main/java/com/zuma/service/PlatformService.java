package com.zuma.service;

import com.zuma.domain.Platform;
import com.zuma.domain.User;
import com.zuma.domain.UserPlatformRelation;
import com.zuma.vo.PageVO;
import com.zuma.form.PlatformForm;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * author:ZhengXing
 * datetime:2017/10/17 0017 10:24
 * 平台业务接口
 */
public interface PlatformService {

    /**
     * 查询所有平台
     */
    List<Platform> findAll();

    /**
     * 分页查询所有平台
     */
    PageVO<Platform> findPage(Pageable pageable);

    /**
     * 根据id查询平台
     */
    Platform findOneById(Long platformId);

    /**
     * 查询某id用户的所有授权平台
     */
    List<UserPlatformRelation> findALLByUserId(Long userId);

    /**
     * 修改某平台的授权状态
     */
    boolean updateStatusByPlatformId(Long platformId, Integer status);

    /**
     * 增加平台
     */
    boolean insertByPlatformForm(PlatformForm platformForm);

    /**
     *  修改平台名字
     */
    boolean updateNameByPlatformId(Long platformId, String name);

    /**
     * 根据名字查询平台,去重，如果查询到，则抛出异常
     */
    void findOneByNameForDistinct(String name);

    /**
     * 根据全名查询瓶体
     */
    Platform findOneByName(String name);

    /**
     * 分页查询某平台的所有授权用户
     */
    PageVO<UserPlatformRelation> findPageGetRootUserList(Long platformId, Pageable pageable);



}
