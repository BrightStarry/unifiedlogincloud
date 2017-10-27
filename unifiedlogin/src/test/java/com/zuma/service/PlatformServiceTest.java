package com.zuma.service;

import com.zuma.domain.Platform;
import com.zuma.domain.UserPlatformRelation;
import com.zuma.form.PlatformForm;
import com.zuma.vo.PageVO;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * author:ZhengXing
 * datetime:2017/10/17 0017 17:27
 * 平台业务测试类
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class PlatformServiceTest {

    @Autowired
    private PlatformService platformService;
    /**
     * 查询所有平台
     */
    @Test
    public void testFindAll() {
        List<Platform> list = platformService.findAll();
        Assert.assertEquals(3,list.size());
    }

    /**
     * 分页查询所有平台
     */
    @Test
    public void testFindPage() {
        PageRequest pageRequest = new PageRequest(0, 2);
        PageVO<Platform> page = platformService.findPage(pageRequest);
        log.info("result={}", page);
        Assert.assertEquals(2,page.getList().size());
    }

    /**
     * 根据id查询平台
     */
    @Test
    public void testFindOneById() {
        Platform platform = platformService.findOneById(1008L);
        Assert.assertNotNull(platform);
    }

    /**
     * 根据用户id查询所有已授权平台
     */
    @Test
    public void testFindALLByUserId() {
        List<UserPlatformRelation> list = platformService.findALLByUserId(1002L);
        Assert.assertEquals(2,list.size());
    }

    /**
     * 根据平台id修改其授权状态
     */
    @Test
    public void testUpdateStatusByPlatformId() {
        Assert.assertTrue(platformService.updateStatusByPlatformId(1001L, 1));
    }

    /**
     * 新增平台
     */
    @Test
    public void testInsertByPlatformForm() {
        PlatformForm platformForm = new PlatformForm();
        platformForm.setName("平台D");
        Assert.assertTrue(platformService.insertByPlatformForm(platformForm));
    }

    /**
     * 修改平台名
     */
    @Test
    public void testUpdateNameByPlatformId() {
        Assert.assertTrue(platformService.updateNameByPlatformId(1001L, "平台A1"));
    }
}
