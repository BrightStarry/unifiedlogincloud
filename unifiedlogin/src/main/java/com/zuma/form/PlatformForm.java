package com.zuma.form;

import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

/**
 * author:ZhengXing
 * datetime:2017/10/17 0017 16:22
 * 平台表单校验类
 */

@Data
public class PlatformForm {
    @NotBlank(message = "平台名为空")
    @Length(min = 2, max = 16, message = "平台名长度不正确（2-16）")
    private String name;
}
