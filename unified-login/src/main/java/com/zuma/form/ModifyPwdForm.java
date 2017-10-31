package com.zuma.form;

import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Pattern;

/**
 * author:ZhengXing
 * datetime:2017/10/18 0018 15:02
 * 修改密码
 */
@Data
public class ModifyPwdForm {
    @NotBlank(message = "密码不能为空")
    @Length(min = 6, max = 16, message = "密码长度不符合规定（6-16）")
    @Pattern(regexp = "[a-zA-Z0-9]*",message = "密码只能由数字和字母构成")
    private String afterPwd;
}
