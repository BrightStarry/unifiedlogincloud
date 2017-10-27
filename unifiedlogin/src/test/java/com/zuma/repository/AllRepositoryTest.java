package com.zuma.repository;

import com.zuma.domain.Platform;
import com.zuma.domain.User;
import com.zuma.domain.UserPlatformRelation;
import com.zuma.enums.PlatformStatusEnum;
import com.zuma.enums.UserPlatformRelationStatusEnum;
import com.zuma.enums.UserStatusEnum;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * author:ZhengXing
 * datetime:2017/10/17 0017 08:53
 * dao层测试
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class AllRepositoryTest {
    @Autowired
    private PlatformRepository platformRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserPlatformRelationRepository userPlatformRelationRepository;

    /**
     * 平台保存
     */
    @Test
    public void test1(){
        Platform platform = new Platform("平台C", PlatformStatusEnum.VALID.getCode());
        Platform result = platformRepository.save(platform);
        Assert.assertNotNull(result);
    }

    /**
     * 平台修改
     */
    @Test
    public void test2(){
        Platform platform = platformRepository.findOne(1001L);
        platform.setStatus(PlatformStatusEnum.VALID.getCode());
        platformRepository.save(platform);
    }

    /**
     * 用户增加
     */
    @Test
    public void test3() {
        User user = new User("e", "123456", UserStatusEnum.VALID.getCode());
        User result = userRepository.save(user);
        log.info("result={}",result);
        Assert.assertNotNull(result);
    }

    /**
     * 用户修改
     */
    @Test
    public void test4() {
        User user = userRepository.findOne(1001L);
        user.setPassword("123456");
        userRepository.save(user);
    }

    /**
     * 根据用户名密码查询用户
     */
    @Test
    public void test5() {
        User user = userRepository.findTopByUsernameAndPassword("b", "123456");
        Assert.assertNotNull(user);
    }

    /**
     * 根据用户名查询用户
     */
    @Test
    public void test6() {
        User user = userRepository.findTopByUsername("b");
        Assert.assertNotNull(user);
    }

    /**
     * 分页查询所有用户
     */
    @Test
    public void test7() {
        PageRequest pageRequest = new PageRequest(0,1);
        Page<User> page = userRepository.findAll(pageRequest);
        Assert.assertEquals(3,page.getTotalPages());
    }

    /**
     * 用户-平台-关系  增加
     */
    @Test
    public void test8() {
        UserPlatformRelation userPlatformRelation = new UserPlatformRelation(1002L,"B",1003L, "平台C" );
        UserPlatformRelation result = userPlatformRelationRepository.save(userPlatformRelation);
        Assert.assertNotNull(result);
    }

    /**
     * 用户平台关系 修改
     */
    @Test
    public void test9() {
        UserPlatformRelation userPlatformRelation = userPlatformRelationRepository.findOne(1001L);
        userPlatformRelation.setPlatformName("平台C");
        userPlatformRelationRepository.save(userPlatformRelation);
    }

    /**
     * 分页查询某用户授权的所有平台
     */
    @Test
    public void test10() {
        PageRequest pageRequest = new PageRequest(0,1);
        Page<UserPlatformRelation> page = userPlatformRelationRepository.findAllByUserId(1000L, pageRequest);
        Assert.assertEquals(2,page.getTotalElements());
    }

    /**
     * 根据用户id和平台id查询到对应的关系记录
     */
    @Test
    public void test11() {
        UserPlatformRelation result = userPlatformRelationRepository.findTopByUserIdAndPlatformId(1001L, 1002L);
        Assert.assertNotNull(result);
    }

    /**
     * 分页查询某平台的所有授权用户
     */
    @Test
    public void test12() {
        PageRequest pageRequest = new PageRequest(0,1);
        Page<UserPlatformRelation> page = userPlatformRelationRepository.findAllByPlatformId(1003L, pageRequest);
        Assert.assertEquals(2,page.getTotalPages());
    }

    /**
     * 根据userId删除所有关系记录
     */
    @Transactional
    @Test
    public void test13() {
        userPlatformRelationRepository.deleteAllByUserId(1006L);
    }


    /**
     * 根据平台id修改平台名
     */
    @Test
    public void test14() {
        userPlatformRelationRepository.updatePlatformNameByPlatformId(1001L,"平台A1");
    }


}
