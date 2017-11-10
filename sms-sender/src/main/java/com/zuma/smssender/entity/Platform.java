package com.zuma.smssender.entity;

import com.zuma.smssender.enums.PlatformStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

/**
 * author:ZhengXing
 * datetime:2017/10/16 0016 16:58
 * 平台信息
 */
@Data
@Entity
@DynamicUpdate
@AllArgsConstructor
@NoArgsConstructor
public class Platform {
    /**
     * id
     */
    @Id
    @GeneratedValue
    private Long id;

    /**
     * 名字
     */
    private String name;

    /**
     * 令牌
     */
    private String token = "";

    /**
     * 回调url
     */
    private String callbackUrl;

    /**
     * 状态： 0：停止授权；1：启用授权
     */
    private Integer status = PlatformStatusEnum.VALID.getCode();

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date updateTime;

    public Platform(String name, String token, Integer status) {
        this.name = name;
        this.token = token;
        this.status = status;
    }

    public Platform(String name, Integer status) {
        this.name = name;
        this.status = status;
    }

}
