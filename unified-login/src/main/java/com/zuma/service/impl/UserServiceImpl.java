package com.zuma.service.impl;

import com.zuma.converter.JPAPage2PageVOConverter;
import com.zuma.domain.Platform;
import com.zuma.domain.User;
import com.zuma.domain.UserPlatformRelation;
import com.zuma.enums.GetOrCancelRootEnum;
import com.zuma.enums.UserStatusEnum;
import com.zuma.form.LoginForm;
import com.zuma.vo.PageVO;
import com.zuma.enums.UserPlatformRelationStatusEnum;
import com.zuma.enums.error.ErrorEnum;
import com.zuma.exception.UnifiedLoginException;
import com.zuma.form.UserForm;
import com.zuma.repository.PlatformRepository;
import com.zuma.repository.UserPlatformRelationRepository;
import com.zuma.repository.UserRepository;
import com.zuma.service.PlatformService;
import com.zuma.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * author:ZhengXing
 * datetime:2017/10/17 0017 10:22
 * 用户业务类
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserPlatformRelationRepository userPlatformRelationRepository;
    @Autowired
    private PlatformService platformService;
    @Autowired
    private PlatformRepository platformRepository;

    /**
     * 根据平台名授权用户
     * @param platformName
     * @param userId
     */
    @Transactional
    @Override
    public void getRootByPlatformName(String platformName, Long userId) {
        //查询用户
        User user = findOneById(userId);
        //查询平台
        Platform platform = platformRepository.findTopByName(platformName);
        //验证平台存在
        if (platform == null) {
            throw new UnifiedLoginException(ErrorEnum.PLATFORM_EMPTY);
        }
        //判断是否已经授权
        //查询关系表记录
        UserPlatformRelation userPlatformRelation = userPlatformRelationRepository.findTopByUserIdAndPlatformId(userId, platform.getId());
        if (userPlatformRelation != null) {
            throw new UnifiedLoginException(ErrorEnum.USER_PLATFORM_ALREADY_ROOT);
        }
        //添加关系
       insertRelationRecord(userId, user.getUsername(), platform.getId(), platformName);
    }

    /**
     * 根据令牌、账号、密码，确认该用户是否允许登录
     * @param loginForm
     * @return
     */
    @Override
    public void login(LoginForm loginForm) {
        //根据id查询平台
        Platform platform = platformRepository.findOne(loginForm.getId());
        //验证平台不为空
        if (platform == null) {
            log.error("【验证某用户是否允许登录某平台】id错误，平台为空.loginForm={}",loginForm);
            throw new UnifiedLoginException(ErrorEnum.API_ID_ERROR);
        }
        //根据用户名查询用户
        User user = findOneByUsername(loginForm.getUsername());
        //如果不存在抛出异常
        if (user == null) {
            log.error("【验证某用户是否允许登录某平台】用户不存在.loginForm={}",loginForm);
            throw new UnifiedLoginException(ErrorEnum.API_USER_EMPTY);
        }

        //验证密码是否一致
        //加密前 密码+盐
        String beforeSignStr = user.getPassword() + platform.getToken();
        //加密
        String afterSignStr = DigestUtils.md5DigestAsHex(beforeSignStr.getBytes());
        if (!loginForm.getPassword().equals(afterSignStr)) {
            log.error("【验证某用户是否允许登录某平台】密码校验失败.loginForm={}",loginForm);
            throw new UnifiedLoginException(ErrorEnum.API_PWD_ERROR);
        }

        //验证用户是否启用-未启用，抛出异常
        if (UserStatusEnum.INVALID.getCode().equals(user.getStatus())) {
            log.error("【验证某用户是否允许登录某平台】用户无效，未启用该账号.userId={}",user.getId());
            throw new UnifiedLoginException(ErrorEnum.API_USER_INVALID);
        }
        //验证用户是否能登录该平台
        UserPlatformRelation userPlatformRelation = userPlatformRelationRepository.findTopByUserIdAndPlatformId(user.getId(), platform.getId());
        if (userPlatformRelation == null) {
            log.error("【验证某用户是否允许登录某平台】用户无法登录该平台.userId={},platformId={}",user.getId(), platform.getId());
            throw new UnifiedLoginException(ErrorEnum.API_USER_NOT_ROOT);
        }

        log.info("【验证某用户是否允许登录某平台】登录成功!参数={}",loginForm);
    }

    /**
     * 根据用户名、密码查询用户
     * @param username
     * @param password
     * @return
     */
    @Override
    public User findOneByUsernameAndPwd(String username, String password) {
        log.info("【根据账号密码查询用户】查询记录。username={},password={}",username,password);
        //查询
        User user = userRepository.findTopByUsernameAndPassword(username, password);
        //自定义异常
        if (user == null)
            throw new UnifiedLoginException(ErrorEnum.USER_ERROR);
        return user;
    }

    /**
     * 分页查询所有用户
     * @param pageable
     * @return
     */
    @Override
    public PageVO<User> findPage(Pageable pageable) {
        //查询
        Page<User> page = userRepository.findAll(pageable);
        //转换
        return JPAPage2PageVOConverter.convert(page);
    }

    /**
     * 根据id修改用户状态
     * @param userId
     * @return
     */
    @Transactional
    @Override
    public boolean updateStatusById(Long userId, Integer status) {
        //根据id查询用户
        User user = findOneById(userId);
        //判断
        if(user.getStatus().equals(status)){
            log.error("【根据id修改用户状态】修改前后状态相同.userId={},status={}",userId,status);
            throw new UnifiedLoginException(ErrorEnum.UPDATE_AFTER_BEFORE_SAME);
        }
        //修改
        user.setStatus(status);
        userRepository.save(user);
        return true;
    }

    /**
     * 根据id分页查询用户所有授权平台情况
     * @param userId
     * @param pageable
     * @return
     */
    @Override
    public PageVO<UserPlatformRelation> findPageById(Long userId, Pageable pageable) {
        //查询出用户所有已授权平台
        Page<UserPlatformRelation> page = userPlatformRelationRepository.findAllByUserId(userId, pageable);
        return JPAPage2PageVOConverter.convert(page);
    }

    /**
     * 根据用户id和平台id修改授权状态
     * @param userId
     * @param platformId
     * @return
     */
    @Transactional
    @Override
    public boolean updateRootByUserIdAndPlatformId(Long userId, Long platformId, Integer status) {
        //查询用户
        User user = findOneById(userId);
        //查询平台
        Platform platform = platformService.findOneById(platformId);
        //查询关系表记录
        UserPlatformRelation userPlatformRelation = userPlatformRelationRepository.findTopByUserIdAndPlatformId(userId, platformId);
        //如果要修改为有效,且不存在
        if (UserPlatformRelationStatusEnum.VALID.getCode().equals(status) && userPlatformRelation == null) {
            //增加关系
            insertRelationRecord(userId, user.getUsername(), platformId, platform.getName());
        }
        //如果要修改为无效，且存在
        if (UserPlatformRelationStatusEnum.INVALID.getCode().equals(status) && userPlatformRelation != null) {
            //删除关系
            userPlatformRelationRepository.delete(userPlatformRelation.getId());
        }
        return true;
    }



    /**
     * 根据id查询用户
     * @param userId
     * @return
     */
    @Override
    public User findOneById(Long userId) {
        User user = userRepository.findOne(userId);
        if (user == null) {
            log.error("【根据id查询用户】用户不存在.userId={}",userId);
            throw new UnifiedLoginException(ErrorEnum.USER_EMPTY);
        }
        return user;
    }

    /**
     * 修改用户密码
     * @param userId
     * @param password
     * @return
     */
    @Transactional
    @Override
    public boolean updatePassword(Long userId, String password) {
        //查询用户
        User user = findOneById(userId);
        //判断
        if(password.equals(user.getPassword())){
            log.error("【修改用户密码】新密码与旧密码相同.userId={}",userId);
            throw new UnifiedLoginException(ErrorEnum.UPDATE_AFTER_BEFORE_SAME);
        }
        //修改
        user.setPassword(password);
        userRepository.save(user);
        return true;
    }

    /**
     * 增加用户
     * @param userForm
     * @return
     */
    @Transactional
    @Override
    public boolean insertByUserForm(UserForm userForm) {
        //根据用户名查询用户-确保不重复
        User result = userRepository.findTopByUsername(userForm.getUsername());
        if (result != null) {
            log.error("【新增用户】用户名重复.username={}", userForm.getUsername());
            throw new UnifiedLoginException(ErrorEnum.NAME_REPEAT);
        }
        //创建用户
        User user = new User();
        //复制属性
        BeanUtils.copyProperties(userForm, user);
        //设置创建时间
        user.setCreateTime(new Date());
        //增加
        user = userRepository.save(user);

        /**
         * 默认授权所有平台登录
         */
        //查询所有平台
        List<Platform> platformList = platformService.findAll();
        //要增加的关系表对象集合
        List<UserPlatformRelation> relationList = new ArrayList<>(platformList.size());
        //填充集合
        for (Platform platform : platformList) {
            UserPlatformRelation userPlatformRelation = new UserPlatformRelation(user.getId(), userForm.getUsername(), platform.getId(), platform.getName());
            relationList.add(userPlatformRelation);
        }
        //批量增加
        userPlatformRelationRepository.save(relationList);

        return true;
    }

    /**
     * 根据用户名查询用户
     * @param username
     * @return
     */
    @Override
    public User findOneByUsername(String username) {
        User user = userRepository.findTopByUsername(username);
        return user;
    }

    /**
     * 增加用户-平台关系记录
     * @param userId
     * @param platformId
     * @param platformName
     */
    @Transactional
    @Override
    public void insertRelationRecord(Long userId, String username, Long platformId, String platformName) {
        UserPlatformRelation userPlatformRelation = new UserPlatformRelation(userId, username, platformId, platformName);
        userPlatformRelationRepository.save(userPlatformRelation);
    }

    /**
     * 给用户授权所有平台
     * @param userId
     */
    @Transactional
    @Override
    public void getAllRoot(Long userId) {
        //查询用户
        findOneById(userId);
        //查询所有平台
        List<Platform> platformList = platformService.findAll();
        //遍历增加
        for (Platform platform : platformList) {
            updateRootByUserIdAndPlatformId(userId, platform.getId(), GetOrCancelRootEnum.GET_ROOT.getCode());
        }
    }

    /**
     * 给某用户取消授权所有平台
     * @param userId
     */
    @Transactional
    @Override
    public void cancelAllRoot(Long userId) {
        userPlatformRelationRepository.deleteAllByUserId(userId);
    }



}
