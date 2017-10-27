package com.zuma.service;

import com.zuma.form.AccountForm;

/**
 * author:ZhengXing
 * datetime:2017/10/18 0018 08:52
 * 系统业务接口
 */
public interface SystemService {
    /**
     * 系统用户登录
     */
    void login(AccountForm accountForm);
}
