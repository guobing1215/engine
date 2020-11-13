package com.example.email.controller;

import com.example.email.domain.MailVo;
import com.example.email.seivice.SendMailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @ClassName : SendMailsController
 * @Description :
 * @Author : 郭兵
 * @Date: 2020-09-30 16:27
 */
@Controller
public class SendMailsController {
    @Autowired
    SendMailsService sendMailsService;

    @PostMapping("/mail/sendMails")
    @ResponseBody
    public Boolean sendMails(MailVo mailVo) {
        mailVo.setMailContent("这是内容");
        mailVo.setMailTiTle("这是标题");
       // mailVo.setRecipients("guobing1215@163.com");
        mailVo.setRecipients("823506390@qq.com");
        return sendMailsService.sendMails(mailVo);
    }
}
