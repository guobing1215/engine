package com.example.email.utils;

import com.example.email.domain.CmsMail;
import com.example.email.domain.MailVo;
import org.springframework.stereotype.Component;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;

/**
  * @Author 郭兵
  * @Description : 发送邮件工具类
  * @Date  2020/9/30 16:40
 **/
@Component
public class MailUtils {
    // SMTP服务器(这里用的163 SMTP服务器)
    public static final String MEAIL_163_SMTP_HOST = "smtp.163.com";
    public static final String SMTP_163_PORT = "25";// 端口号,这个是163使用到的;QQ的应该是465或者875

    /**
     * 发送邮件
     *
     * @param isSingle   是否单发: true-单发 false-群发
     * @param mailVo     邮件内容
     * @param sendTime   发送时间, 如果为null，表示立即发送
     * @param mailConfig 发件人信息及授权
     */
    public static void sendMail(Boolean isSingle, MailVo mailVo, Date sendTime, CmsMail mailConfig) throws MessagingException {
        Session session = authenticationMail();
        MimeMessage message = getMimeMessage(isSingle, session, mailConfig.getMailAccount(), mailVo.getRecipients());
        message = getContent(message, mailVo.getMailTiTle(), mailVo.getMailContent(), sendTime);

        Transport transport = session.getTransport();
        transport.connect(mailConfig.getMailAccount(), mailConfig.getMailLicense());
        transport.sendMessage(message, message.getAllRecipients());
        transport.close();
    }

    /**
     getMailAccount * 校验发送的邮件内容
     */
    private static MimeMessage getContent(MimeMessage message, String messageTitle, String messageContent, Date sendTime) throws MessagingException {
        if (null == messageTitle) {
            throw new MessagingException("邮件标题不能为空");
        }
        if (null == messageContent) {
            throw new MessagingException("邮件内容不能为空");
        }
        sendTime = sendTime == null ? new Date() : sendTime;
        message.setSubject(messageTitle, "UTF-8");
        message.setContent(messageContent, "text/html;charset=UTF-8");
        message.setSentDate(sendTime);
        return message;
    }

    /**
     * 验证认证信息
     */
    private static Session authenticationMail() throws MessagingException {
        Session session;
        try {
            Properties props = new Properties();
            //设置用户的认证方式
            props.setProperty("mail.smtp.auth", "true");
            //设置传输协议
            props.setProperty("mail.transport.protocol", "smtp");
            //设置发件人的SMTP服务器地址
            props.setProperty("mail.smtp.host",MEAIL_163_SMTP_HOST);
            props.setProperty("mail.smtp.port", SMTP_163_PORT);
            props.setProperty("mail.smtp.socketFactory.port", SMTP_163_PORT);
          //  props.setProperty("mail.smtp.socketFactory.class", "SSL_FACTORY");

//            session = Session.getInstance(props, new Authenticator() {
//                // 设置认证账户信息
//                @Override
//                protected PasswordAuthentication getPasswordAuthentication() {
//                    return new PasswordAuthentication(mailConfig.getMailAccount(), mailConfig.getMailLicense());
//                }
//            });
            session = Session.getInstance(props);
            session.setDebug(true);
        } catch (Exception e) {
            e.printStackTrace();
            throw new MessagingException("认证失败");
        }
        return session;
    }

    /**
     * @param isSingle         是否单发
     *                         <P>true-向指定的一个收件人发送邮件，比如:找回密码、登录验证
     *                         <P>false-向多个收件人群发邮件，比如:优惠活动推送
     *                         <P>群发时多个收件人之间用英文逗号','分割
     * @param senderAddress    发件人地址
     * @param recipientAddress 收件人地址
     */
    private static MimeMessage getMimeMessage(Boolean isSingle, Session session, String senderAddress, String recipientAddress) throws MessagingException {
        MimeMessage message = new MimeMessage(session);
        try {
            message.setFrom(new InternetAddress(senderAddress));
        } catch (MessagingException e) {
            throw new MessagingException("发件人地址错误");
        }
        /*
          设置收件人地址
          MimeMessage.RecipientType.TO:发送
          MimeMessage.RecipientType.CC：抄送
          MimeMessage.RecipientType.BCC：密送
         */
        if (isSingle) {
            message.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(recipientAddress));
        } else {
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientAddress));
        }
        return message;
    }
}
