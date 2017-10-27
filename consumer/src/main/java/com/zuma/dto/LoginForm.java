package com.zuma.dto;

import lombok.Builder;
import lombok.Data;

/**
 * author:ZhengXing
 * datetime:2017/10/18 0018 10:28
 * 登录表单
 */

@Builder
@Data
public class LoginForm {
    private Long id;

    private Integer channel;

    private String username;

    private String password;


}
