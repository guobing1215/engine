package com.cummins.bigscreen.screen.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;

/**
 * @ClassName : SendEamil
 * @Description :邮件发送
 * @Author : 郭兵
 * @Date: 2020-09-30 15:00
 */
public class EmailUtils {
    private final static Logger logger= LoggerFactory.getLogger(EmailUtils.class);
    // SMTP服务器(这里用的163 SMTP服务器)
    public static final String MEAIL_163_SMTP_HOST = "smtp.163.com";
    public static final String SMTP_163_PORT = "25";// 端口号,这个是163使用到的;QQ的应该是465或者875


    public static void sendEamil(String myEmailAccount, String myEmailPassword, String emailReceiveAccount, String message){
        Properties p = new Properties();
        p.setProperty("mail.smtp.host", MEAIL_163_SMTP_HOST);
        p.setProperty("mail.smtp.port", SMTP_163_PORT);
        p.setProperty("mail.smtp.socketFactory.port", SMTP_163_PORT);
        p.setProperty("mail.smtp.auth", "true");
        p.setProperty("mail.smtp.socketFactory.class", "SSL_FACTORY");

        Session session = Session.getInstance(p, new Authenticator() {
            // 设置认证账户信息
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(myEmailAccount, myEmailPassword);
            }
        });
        session.setDebug(true);
        try {
        MimeMessage mimeMessage = new MimeMessage(session);
        // 发件人
        mimeMessage.setFrom(new InternetAddress(myEmailAccount));
        // 收件人和抄送人
        mimeMessage.setRecipients(Message.RecipientType.TO, emailReceiveAccount);
        // 内容(这个内容还不能乱写,有可能会被SMTP拒绝掉;多试几次吧)
        mimeMessage.setSubject("大屏数据接口异常信息");
        mimeMessage.setContent(message, "text/html;charset=UTF-8");
        mimeMessage.setSentDate(new Date());
        mimeMessage.saveChanges();
        Transport.send(mimeMessage);
        } catch (MessagingException e) {
            logger.error("邮件发送失败"+e.getMessage());
            e.printStackTrace();
        }
    }
}
