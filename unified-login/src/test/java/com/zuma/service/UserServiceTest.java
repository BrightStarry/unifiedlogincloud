package com.zuma.service;

import com.zuma.domain.User;
import com.zuma.domain.UserPlatformRelation;
import com.zuma.vo.PageVO;
import com.zuma.form.UserForm;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * author:ZhengXing
 * datetime:2017/10/17 0017 16:49
 * 用户业务层测试类
 */

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class UserServiceTest {
    @Autowired
    private UserService userService;

    /**
     * 根据用户名密码查询对象
     */
    @Test
    public void testFindOneByUsernameAndPwd(){
        User user = userService.findOneByUsernameAndPwd("b", "123456");
        log.info("【result】={}", user);
        Assert.assertNotNull(user);
    }

    /**
     * 分页查询所有用户
     */
    @Test
    public void  testFindPage() {
        PageRequest pageRequest = new PageRequest(0,2);
        PageVO<User> page = userService.findPage(pageRequest);
        log.info("result={}", page);
        Assert.assertNotNull(page.getList().get(0));
    }

    /**
     * 根据id修改用户状态
     */
    @Test
    public void testUpdateStatusById() {
        Assert.assertTrue(userService.updateStatusById(1001L, 1));
    }

    /**
     * 根据id分页查询用户所有平台授权情况
     */
    @Test
    public void testFindPageById() {
        PageRequest pageRequest = new PageRequest(0,1);
        PageVO<UserPlatformRelation> page = userService.findPageById(1002L, pageRequest);
        log.info("result={}",page);
        Assert.assertNotNull(page.getList().get(0));
    }

    /**
     * 根据用户id和平台id修改授权状态
     */
    @Test
    public void testUpdateRootByUserIdAndPlatformId() {
        Assert.assertTrue(userService.updateRootByUserIdAndPlatformId(1002L, 1003L, 0));
    }

    /**
     * 根据id查询用户
     */
    @Test
    public void testFindOneById() {
        User user = userService.findOneById(1008L);
        log.info("result={}", user);
        Assert.assertNotNull(user);
    }

    /**
     * 修改用户密码
     */
    @Test
    public void testUpdatePassword() {
        Assert.assertTrue(userService.updatePassword(1001L, "123456"));
    }

    /**
     * 新增用户
     */
    @Test
    public void testInsertByUserForm() {
        UserForm userForm = new UserForm();
        userForm.setUsername("f");
        userForm.setPassword("123456");
        Assert.assertTrue(userService.insertByUserForm(userForm));
    }

    /**
     * 根据userId删除所有关系记录
     */
    @Test
    public void testCancelAllRoot() {
        userService.cancelAllRoot(1006L);
    }

}
