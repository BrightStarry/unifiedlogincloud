package com.zuma.service.impl;

import com.zuma.converter.JPAPage2PageVOConverter;
import com.zuma.domain.Platform;
import com.zuma.domain.User;
import com.zuma.domain.UserPlatformRelation;
import com.zuma.util.TokenUtil;
import com.zuma.vo.PageVO;
import com.zuma.enums.error.ErrorEnum;
import com.zuma.exception.UnifiedLoginException;
import com.zuma.form.PlatformForm;
import com.zuma.repository.PlatformRepository;
import com.zuma.repository.UserPlatformRelationRepository;
import com.zuma.service.PlatformService;
import com.zuma.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * author:ZhengXing
 * datetime:2017/10/17 0017 10:25
 * 平台业务类
 */
@Service
@Slf4j
public class PlatformServiceImpl implements PlatformService {
    @Autowired
    private PlatformRepository platformRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private UserPlatformRelationRepository userPlatformRelationRepository;

    /**
     * 查询所有平台
     * @return
     */
    @Override
    public List<Platform> findAll() {
        return platformRepository.findAll();
    }

    /**
     * 分页查询所有平台
     * @param pageable
     * @return
     */
    @Override
    public PageVO<Platform> findPage(Pageable pageable) {
        return JPAPage2PageVOConverter.convert(platformRepository.findAll(pageable));
    }

    /**
     * 根据id查询平台
     * @param platformId
     * @return
     */
    @Override
    public Platform findOneById(Long platformId) {
        Platform platform = platformRepository.findOne(platformId);
        if (platform == null) {
            log.error("【根据id查询平台】平台不存在.platform={}",platformId);
            throw new UnifiedLoginException(ErrorEnum.PLATFORM_EMPTY);
        }
        return platform;
    }

    /**
     * 根据用户id查询所有已授权平台
     * @param userId
     * @return
     */
    @Override
    public List<UserPlatformRelation> findALLByUserId(Long userId) {
        //查询用户-主要是为验证用户是否存在
        User user = userService.findOneById(userId);
        //查询平台
        return userPlatformRelationRepository.findAllByUserId(userId);
    }


    /**
     * 根据平台id修改该平台授权状态
     * @param platformId
     * @param status
     * @return
     */
    @Transactional
    @Override
    public boolean updateStatusByPlatformId(Long platformId, Integer status) {
        //查询平台
        Platform platform = findOneById(platformId);
        //判断
        if(platform.getStatus().equals(status)){
            log.error("【根据id修改平台状态】修改前后状态相同.userId={},status={}",platformId,status);
            throw new UnifiedLoginException(ErrorEnum.UPDATE_AFTER_BEFORE_SAME);
        }
        //修改
        platform.setStatus(status);
        platformRepository.save(platform);
        return true;
    }

    /**
     * 增加平台
     * @param platformForm
     * @return
     */
    @Transactional
    @Override
    public boolean insertByPlatformForm(PlatformForm platformForm) {
        //去重名
        findOneByNameForDistinct(platformForm.getName());
        //新建平台
        Platform platform = new Platform();
        //属性复制
        BeanUtils.copyProperties(platformForm, platform);
        //设置令牌
        platform.setToken(TokenUtil.generate());
        //设置创建时间
        platform.setCreateTime(new Date());
        //保存
        platformRepository.save(platform);
        return true;
    }

    /**
     * 修改平台名
     * @param platformId
     * @param name
     * @return
     */
    @Transactional
    @Override
    public boolean updateNameByPlatformId(Long platformId, String name) {
        //查询平台
        Platform platform = findOneById(platformId);
        //验证
        if (name.equals(platform.getName())) {
            log.error("【根据id修改平台名】修改前后名字相同.platformId={}",platformId);
            throw new UnifiedLoginException(ErrorEnum.UPDATE_AFTER_BEFORE_SAME);
        }
        //去重名
        findOneByNameForDistinct(name);
        //修改
        platform.setName(name);
        platformRepository.save(platform);
        //维护修改关系表
        userPlatformRelationRepository.updatePlatformNameByPlatformId(platformId, name);
        return true;
    }

    /**
     * 根据名字查询平台，去重，如果查询到则抛出异常
     * @param name
     * @return
     */
    @Override
    public void findOneByNameForDistinct(String name) {
        Platform platform = findOneByName(name);
        if(platform != null){
            log.error("【根据名字查询平台-去重】平台名重复.name={}",name);
            throw new UnifiedLoginException(ErrorEnum.NAME_REPEAT);
        }
    }

    /**
     * 根据名字查询平台
     * @param name
     * @return
     */
    @Override
    public Platform findOneByName(String name) {
        return platformRepository.findTopByName(name);
    }

    /**
     * 分页，根据平台id查询所有授权用户
     * @param platformId
     * @return
     */
    @Override
    public PageVO<UserPlatformRelation> findPageGetRootUserList(Long platformId, Pageable pageable) {
        Page<UserPlatformRelation> page = userPlatformRelationRepository.findAllByPlatformId(platformId, pageable);

        return JPAPage2PageVOConverter.convert(page);
    }




}
