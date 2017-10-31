package com.zuma.form;

import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

/**
 * author:ZhengXing
 * datetime:2017/10/23 0023 10:50
 * 修改平台名
 */
@Data
public class ModifyPlatformNameForm {
    @NotBlank(message = "平台名为空")
    @Length(min = 2, max = 16, message = "平台名长度不正确（2-16）")
    private String name;

}
