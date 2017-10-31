package com.zuma.domain;

import com.zuma.enums.UserPlatformRelationStatusEnum;
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
 * datetime:2017/10/16 0016 17:23
 * 用户-平台-关系
 */

@Data
@Entity
@DynamicUpdate
@AllArgsConstructor
@NoArgsConstructor
public class UserPlatformRelation {

    /**
     * id
     */
    @Id
    @GeneratedValue
    private Long id;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 平台id
     */
    private Long platformId;

    /**
     * 平台名
     */
    private String platformName;


    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date updateTime;

    public UserPlatformRelation(Long userId, String username, Long platformId, String platformName) {
        this.userId = userId;
        this.username = username;
        this.platformId = platformId;
        this.platformName = platformName;
    }
}
