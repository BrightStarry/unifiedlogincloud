package com.zuma.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.zuma.enums.UserStatusEnum;
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
 * datetime:2017/10/16 0016 17:20
 * 用户信息
 */

@Data
@Entity
@DynamicUpdate
@AllArgsConstructor
@NoArgsConstructor
public class User {
    /**
     * id
     */
    @Id
    @GeneratedValue
    private Long id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 状态： 0：停用；1：启用
     */
    private Integer status = UserStatusEnum.VALID.getCode();

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date updateTime;

    public User(String username, String password, Integer status) {
        this.username = username;
        this.password = password;
        this.status = status;
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
