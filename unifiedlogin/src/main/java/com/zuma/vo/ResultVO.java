package com.zuma.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * author:ZhengXing
 * datetime:2017/10/16 0016 17:02
 * 返回对象
 */


@ApiModel
@Data
public class ResultVO<T> {
    /**状态码*/
    @ApiModelProperty(value = "异常码",name = "异常码,0000为成功")
    private String code;
    /**消息*/
    @ApiModelProperty(value = "异常消息",name = "异常描述")
    private String message;
    /**数据*/
    @ApiModelProperty(value = "返回数据",name = "返回数据")
    private T data;
}
