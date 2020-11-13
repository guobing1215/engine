package com.example.email.seivice;

import com.example.email.domain.CmsMail;
import com.example.email.utils.MailUtils;
import com.example.email.domain.MailVo;
import org.springframework.stereotype.Service;

/**
 * @ClassName : sendMailsService
 * @Description :
 * @Author : 郭兵
 * @Date: 2020-09-30 16:25
 */
@Service
public class SendMailsService {

    public Boolean sendMails(MailVo mailVo) {
//        if (null == mailVo) {
//            return ResponseBean.createInstance(Boolean.FALSE, 400, "参数异常");
//        }
//        // 获取当前正在使用的发件人配置
//        List<CmsMailConfig> mailConfigList = mailConfigMapper.selectCurrUseMailConfig();
//        if (null == mailConfigList || mailConfigList.size() != 1) {
//            return ResponseBean.createInstance(Boolean.FALSE, 400, "参数异常");
//        }

        try {
            CmsMail cmsMail=new CmsMail();
            cmsMail.setMailAccount("gbing1995@163.com");
            cmsMail.setMailLicense("PLXSMIHJVTUNUGBA");
            // 发送邮件
            MailUtils.sendMail(Boolean.TRUE, mailVo, null, cmsMail);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
