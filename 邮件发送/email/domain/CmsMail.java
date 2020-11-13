package com.example.email.domain;

import lombok.Data;

/**
 * @ClassName : CmsMailConfig
 * @Description :
 * @Author : 郭兵
 * @Date: 2020-09-30 16:24
 */

@Data
public class CmsMail {
    /**
     * 主键
     */
    private Integer mailId;

    /**
     * 邮箱账号
     */
    private String mailAccount;

    /**
     * 授权码
     */
    private String mailLicense;

    /**
     * 状态，0：使用，1：未使用
     */
    private String isUse;

    /**
     * 是否删除，0：否，1：是
     */
    private String isDelete;

    /**
     * 创建者ID
     */
    private Integer createBy;
}
