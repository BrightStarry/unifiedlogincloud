package com.zuma.smssender.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

/**
 * author:Administrator
 * datetime:2017/11/10 0010 12:46
 * 短信上行记录
 */
@Data
@Entity
@DynamicUpdate
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SmsUpRecord {
    /**
     * id
     */
    @Id
    @GeneratedValue
    private Long id;

    /**
     * 通道id
     */
    private Integer channelId;

    /**
     * 通道名
     */
    private String channelName;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 上行内容
     */
    private String content;

    /**
     * 通道上行实体类jsonString
     */
    private String requestBody;

    /**
     * 上行时间
     */
    private Date upTime;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date updateTime;
}
