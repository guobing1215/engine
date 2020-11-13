package com.example.email.domain;

import lombok.Data;

/**
 * @ClassName : MailVo
 * @Description :
 * @Author : 郭兵
 * @Date: 2020-09-30 16:23
 */
@Data
public class MailVo {
    /**
     * 邮件标题
     */
    private String mailTiTle;
    /**
     * 邮件内容
     */
    private String mailContent;
    /**
     * 收件人(们)
     */
    private String recipients;
}