package com.zuma.smssender.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

/**
 * author:Administrator
 * datetime:2017/11/10 0010 15:16
 */
@Data
@Entity
@DynamicUpdate
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Accessors(chain = true)
public class SmsSendRecord {
    /**
     * 记录id
     */
    @Id
    @GeneratedValue
    private Long id;

    /**
     * 平台id
     */
    private Long platformId;

    /**
     * 平台名
     */
    private String platformName;

    /**
     * 调用者请求对象json字符
     */
    private String requestBody;

    /**
     * 是否成功 1.成功 0.失败
     */
    private Integer isSuccess;


    /**
     * 同步返回对象json
     */
    private String resultBody;

    /**
     * 异步返回对象json
     */
    private String asyncResultBody;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date updateTime;
}
