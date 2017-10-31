package com.zuma.form;

import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;
import java.util.HashMap;

/**
 * author:ZhengXing
 * datetime:2017/10/18 0018 10:28
 * 登录表单
 */
@Data
public class LoginForm {
    @NotNull(message = "平台id为空")
    @Range(min = 1000, max = 9999,message = "id范围错误(1000-9999)")
    private Long id;

    @NotNull(message = "渠道为空")
    @Range(min = 0, max = 9,message = "渠道不在范围内(0-9)")
    private Integer channel;

    @NotBlank(message = "用户名为空")
    @Length(min = 4, max = 16, message = "用户名长度不正确（4-16）")
    private String username;

    @NotBlank(message = "密码为空")
    @Length(min = 16, max = 32, message = "密码长度不正确（16-32）")
    private String password;

}
