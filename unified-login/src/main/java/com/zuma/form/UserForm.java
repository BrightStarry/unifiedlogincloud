package com.zuma.form;

import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

/**
 * author:ZhengXing
 * datetime:2017/10/17 0017 16:09
 * 用户表单检验类
 */

@Data
public class UserForm {
    /**
     * 用户名
     */
    @NotBlank(message = "用户名为空")
    @Length(min = 4, max = 16,message = "用户名长度不正确(4-16)")
    private String username;

    /**
     * 密码
     */
    @NotBlank(message = "密码为空")
    @Length(min = 6, max = 16, message = "密码长度不正确（6-16）")
    private String password;
}
