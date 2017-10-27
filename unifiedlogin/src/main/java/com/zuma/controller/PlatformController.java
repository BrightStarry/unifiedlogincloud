package com.zuma.controller;

import com.zuma.annotation.Logined;
import com.zuma.config.Config;
import com.zuma.converter.JPAPage2PageVOConverter;
import com.zuma.domain.Platform;
import com.zuma.domain.User;
import com.zuma.domain.UserPlatformRelation;
import com.zuma.enums.error.ErrorEnum;
import com.zuma.exception.FormInvalidException;
import com.zuma.exception.UnifiedLoginException;
import com.zuma.form.ModifyPlatformNameForm;
import com.zuma.form.PlatformForm;
import com.zuma.service.PlatformService;
import com.zuma.util.ResultVOUtil;
import com.zuma.vo.PageVO;
import com.zuma.vo.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * author:ZhengXing
 * datetime:2017/10/18 0018 11:01
 * 平台控制层
 */
@Controller
@RequestMapping("/platform")
@Slf4j
public class PlatformController extends BaseController {

    @Autowired
    private PlatformService platformService;

    //进入平台列表页
    @Logined
    @GetMapping("/list")
    public String listView(Model model) {
        setNavigation(model, Config.MEMU_TOP1_B,Config.MEMU_TOP2_B);
        return "platform/list";
    }

    /**
     * 分页查询平台列表
     */
    @Logined
    @GetMapping("/list/{pageNo}")
    public String list(@PathVariable("pageNo") int pageNo, @RequestParam(required = false) Integer pageSize, Model model) {
        PageRequest pageRequest = getPageRequest(pageNo, pageSize);
        PageVO<Platform> page = platformService.findPage(pageRequest);
        model.addAttribute("page", page);

        return "platform/table";
    }

    /**
     * 根据名字查询平台
     */
    @Logined
    @GetMapping("/search/name/{name}")
    public String  findOneByName(@PathVariable("name") String name,Model model) {
        //参数校验
        notEmptyOfString(name);
        Platform platform = platformService.findOneByName(name);

        PageVO<Platform> page = new PageVO<>(1,1,1L,1,
                platform == null ? new ArrayList<Platform>() : Arrays.asList(platform));
        model.addAttribute("page", page);
        return "platform/table";
    }

    /**
     * 根据id查询平台
     */
    @Logined
    @ResponseBody
    @PostMapping("/search/id/{platformId}")
    public ResultVO<Platform> findOneByPlatformId(@PathVariable("platformId") long platformId) {
        //参数校验
        notZeroOfLong(platformId);
        Platform platform = platformService.findOneById(platformId);
        return ResultVOUtil.success(platform);
    }

    /**
     * 修改平台状态
     */
    @Logined
    @ResponseBody
    @PostMapping("{platformId}/modify/status/{status}")
    public ResultVO<?> modifyStatus(@PathVariable("platformId") long platformId, @PathVariable int status) {
        //参数校验
        notZeroOfLong(platformId);
        if (status < 0 || status > 1) {
            throw new UnifiedLoginException(ErrorEnum.FORM_ERROR);
        }
        platformService.updateStatusByPlatformId(platformId,status);
        return ResultVOUtil.success();
    }

    /**
     * 修改平台名字
     */
    @Logined
    @ResponseBody
    @PostMapping("/{platformId}/modify/name")
    public ResultVO<?> modifyName(@PathVariable("platformId") long platformId,
                                  @Valid ModifyPlatformNameForm modifyPlatformNameForm,BindingResult bindingResult) {
        //参数校验
        notZeroOfLong(platformId);
        isValid(bindingResult,log,"【修改平台名】表单校验失败");
        platformService.updateNameByPlatformId(platformId, modifyPlatformNameForm.getName());
        return ResultVOUtil.success();
    }

    /**
     * 分页查询该平台所有授权用户
     */
    @Logined
    @GetMapping("/{platformId}/rootUser/list/{pageNo}")
    public String  rootUserList(@PathVariable("platformId") long platformId,
                                                               @PathVariable("pageNo") int pageNo,
                                                               @RequestParam(required = false) Integer pageSize,
                                                                Model model) {
        //参数校验
        notZeroOfLong(platformId);
        PageRequest pageRequest = getPageRequest(pageNo, pageSize);
        PageVO<UserPlatformRelation> page = platformService.findPageGetRootUserList(platformId, pageRequest);
        //设置临时的平台id
        page.setId(platformId);
        model.addAttribute("page", page);

        return "platform/rootUserListModal";
    }

    /**
     * 增加平台
     */
    @Logined
    @ResponseBody
    @PostMapping("/add")
    public ResultVO<?> addPlatform(@Valid PlatformForm platformForm, BindingResult bindingResult) {
        isValid(bindingResult,log,"【新增平台】表单校验失败");
        platformService.insertByPlatformForm(platformForm);
        return ResultVOUtil.success();
    }


}
