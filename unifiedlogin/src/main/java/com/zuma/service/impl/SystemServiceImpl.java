package com.zuma.service.impl;

import com.zuma.config.Account;
import com.zuma.enums.error.ErrorEnum;
import com.zuma.exception.UnifiedLoginException;
import com.zuma.form.AccountForm;
import com.zuma.service.SystemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * author:ZhengXing
 * datetime:2017/10/18 0018 08:53
 * 系统业务实现类
 */
@Service
@Slf4j
public class SystemServiceImpl implements SystemService {

    @Autowired
    private Account account;

    /**
     * 登录，验证用户名密码是否正确
     * @param accountForm
     * @return boolean 是否允许
     */
    @Override
    public void login(AccountForm accountForm) {
        //如果不一致，抛出异常
        if (!accountForm.getUsername().equals(account.getUsername()) || !accountForm.getPassword().equals(account.getPassword())) {
            log.info("【系统登录】账号密码错误.accountForm={}",accountForm);
            throw new UnifiedLoginException(ErrorEnum.SYSTEM_ACCOUNT_ERROR);
        }
    }
}
