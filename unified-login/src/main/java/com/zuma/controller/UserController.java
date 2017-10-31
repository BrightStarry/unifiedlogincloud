package com.zuma.controller;

import com.zuma.annotation.Logined;
import com.zuma.config.Config;
import com.zuma.domain.User;
import com.zuma.domain.UserPlatformRelation;
import com.zuma.enums.error.ErrorEnum;
import com.zuma.exception.FormInvalidException;
import com.zuma.exception.UnifiedLoginException;
import com.zuma.form.LoginForm;
import com.zuma.form.ModifyPwdForm;
import com.zuma.form.UserForm;
import com.zuma.service.UserService;
import com.zuma.util.ResultVOUtil;
import com.zuma.vo.PageVO;
import com.zuma.vo.ResultVO;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * author:ZhengXing
 * datetime:2017/10/18 0018 10:58
 * 用户控制类
 */

@Controller
@RequestMapping("/user")
@Slf4j
public class UserController extends BaseController {
    @Autowired
    private UserService userService;




    /**
     * 进入用户列表页
     */
    @Logined
    @GetMapping("/list")
    public String listView(Model model) {
        setNavigation(model,Config.MEMU_TOP1_A,Config.MEMU_TOP2_A);
        return "user/list";
    }
    /**
     * 分页查询用户列表
     */
    @Logined
    @GetMapping("/list/{pageNo}")
    public String list(@PathVariable("pageNo") int pageNo, @RequestParam(required = false) Integer pageSize,Model model) {
        PageRequest pageRequest = getPageRequest(pageNo, pageSize);
        PageVO<User> page = userService.findPage(pageRequest);

        model.addAttribute("page", page);
        return "user/table";
    }

    /**
     * 用户名查询单个用户
     */
    @Logined
    @GetMapping("/search/name/{username}")
    public String findOneByUsername(@PathVariable("username") String username,Model model) {
        //参数校验
        notEmptyOfString(username);
        User user = userService.findOneByUsername(username);

        //构造PageVO<User>,如果list为null，会栈溢出
        PageVO<User> page = new PageVO<>(1,1,1L,1,
                user == null ? new ArrayList<User>() : Arrays.asList(user));
        model.addAttribute("page", page);
        return "/user/table";
    }

    /**
     * 根据id查询单个用户
     */
    @Logined
    @ResponseBody
    @PostMapping("/search/id/{userId}")
    public ResultVO<User> findOneByUserId(@PathVariable("userId") long userId) {
        //参数校验
        notZeroOfLong(userId);
        User user = userService.findOneById(userId);
        return ResultVOUtil.success(user);
    }

    /**
     * 修改密码
     */
    @Logined
    @ResponseBody
    @PostMapping("/{userId}/modify/pwd")
    public ResultVO<?> modifyPwd(@PathVariable("userId") long userId, @Valid ModifyPwdForm form, BindingResult bindingResult) {
        //参数校验
        isValid(bindingResult, log, "【修改密码】密码不符合规范.");
        //修改密码
        userService.updatePassword(userId, form.getAfterPwd());
        return ResultVOUtil.success();
    }

    /**
     * 修改状态
     */
    @Logined
    @ResponseBody
    @PostMapping("/{userId}/modify/status/{status}")
    public ResultVO<?> modifyStatus(@PathVariable("userId") long userId, @PathVariable int status) {
        //参数校验
        notZeroOfLong(userId);
        if (status < 0 || status > 1) {
            throw new UnifiedLoginException(ErrorEnum.FORM_ERROR);
        }
        userService.updateStatusById(userId, status);
        return ResultVOUtil.success();
    }

    /**
     * 分页查询用户已授权的平台
     */
    @Logined
    @GetMapping("/{userId}/rootPlatform/list/{pageNo}")
    public String rootPlatformList(@PathVariable("userId") long userId, @PathVariable("pageNo") Integer pageNo,
                                   @RequestParam(required = false) Integer pageSize,Model model) {
        //参数验证
        notZeroOfLong(userId);

        PageRequest pageRequest = getPageRequest(pageNo, pageSize);
        PageVO<UserPlatformRelation> page = userService.findPageById(userId, pageRequest);
        page.setId(userId);
        model.addAttribute("page", page);

        return "user/rootPlatformListModal";
    }

    /**
     * 获取/取消授权
     */
    @Logined
    @ResponseBody
    @PostMapping("{userId}/root/{platformId}/{status}")
    public ResultVO<?> getOrCancelRoot(@PathVariable("userId") long userId,@PathVariable("status") int status,
                                       @PathVariable("platformId") long platformId) {
        //参数校验
        notZeroOfLong(userId, platformId);
        if (status> 1 || status < 0) {
            throw new UnifiedLoginException(ErrorEnum.FORM_ERROR);
        }
        userService.updateRootByUserIdAndPlatformId(userId, platformId, status);
        return ResultVOUtil.success();
    }

    /**
     * 根据平台名给用户授权
     */
    @Logined
    @ResponseBody
    @PostMapping("/{userId}/root/name/{platformName}")
    public ResultVO<?> getRootByPlatformName(@PathVariable("userId") long userId,@PathVariable("platformName") String platformName) {
        notZeroOfLong(userId);
        notEmptyOfString(platformName);
        userService.getRootByPlatformName(platformName,userId);
        return ResultVOUtil.success();
    }

    /**
     * 取消所有授权
     */
    @Logined
    @ResponseBody
    @PostMapping("/{userId}/cancelAllRoot")
    public ResultVO<?> cancelAllRoot(@PathVariable("userId") long userId) {
        userService.cancelAllRoot(userId);
        return ResultVOUtil.success();
    }

    /**
     * 获取所有授权
     */
    @Logined
    @ResponseBody
    @PostMapping("/{userId}/getAllRoot")
    public ResultVO<?> getAllRoot(@PathVariable("userId") long userId) {
        userService.getAllRoot(userId);
        return ResultVOUtil.success();
    }

    /**
     * 新增用户
     */
    @Logined
    @ResponseBody
    @PostMapping("/add")
    public ResultVO<?> addUser(@Valid UserForm userForm, BindingResult bindingResult){
        //表单校验
        isValid(bindingResult,log, "【新增用户】表单校验不通过.");
        userService.insertByUserForm(userForm);
        return ResultVOUtil.success();
    }
}
