package com.zuma.service;

import com.zuma.domain.User;
import com.zuma.domain.UserPlatformRelation;
import com.zuma.enums.GetOrCancelRootEnum;
import com.zuma.form.LoginForm;
import com.zuma.vo.PageVO;
import com.zuma.form.UserForm;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * author:ZhengXing
 * datetime:2017/10/17 0017 10:21
 * 用户业务接口
 */
public interface UserService {

    

    /**
     * 根据平台名授权用户
     */
    void getRootByPlatformName(String platformName, Long userId);

    /**
     * 根据令牌、账号、密码，确认该用户是否允许登录
     */
    void login(LoginForm loginForm);

    /**
     * 根据账号密码查询某个用户
     */
    User findOneByUsernameAndPwd(String username, String password);

    /**
     * 分页查询所有用户
     */
    PageVO<User> findPage(Pageable pageable);

    /**
     * 修改某用户状态
     */
    boolean updateStatusById(Long userId, Integer status);

    /**
     * 分页查询单个用户所有平台授权情况
     */
    PageVO<UserPlatformRelation> findPageById(Long userId, Pageable pageable);

    /**
     * 某用户获取/取消指定平台授权
     */
    boolean updateRootByUserIdAndPlatformId(Long userId, Long platformId, Integer status);

    /**
     * 查询单个用户信息
     */
    User findOneById(Long userId);

    /**
     * 修改某用户密码
     */
    boolean updatePassword(Long userId, String password);

    /**
     * 增加用户
     */
    boolean insertByUserForm(UserForm userForm);

    /**
     * 根据用户名查询用户
     */
    User findOneByUsername(String username);

    /**
     * 增加用户-平台关系记录
     */
    void insertRelationRecord(Long userId, String username, Long platformId, String platformName);

    /**
     * 给某用户授权授权所有平台
     */
    void getAllRoot(Long userId);

    /**
     * 给某用户取消授权所有平台
     */
    void cancelAllRoot(Long userId);



}
