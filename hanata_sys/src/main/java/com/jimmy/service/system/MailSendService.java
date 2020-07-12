package com.jimmy.service.system;

import com.jimmy.bean.Article;
import com.jimmy.bean.Message;
import com.jimmy.bean.redis.SubModel;
import com.jimmy.repository.SubscribeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.Map;

/**
 * @author Jimmy He
 * @date 2020-06-17
 */

@Service
public class MailSendService {

    @Autowired
    private JavaMailSenderImpl mailSender;

    @Autowired
    SubscribeRepository subscribeRepository;

    @Value("${hanata.redirectAddr}")
    private String serverUrl;

    @Async
    public void sendMail(String title,String content,String articleId) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
        mimeMessageHelper.setFrom("hanata2020@163.com");


        MessageFormat messageFormat = new MessageFormat("hanata新文章{0}");
        // title and pre content

        String subject = messageFormat.format(new Object[]{title});

        mimeMessageHelper.setSubject(subject);

        Map<String, SubModel> subListMap = subscribeRepository.getSubList();
        for (Map.Entry<String, SubModel> subInfo : subListMap.entrySet()) {
            String userId = subInfo.getKey();
            SubModel subModel = subInfo.getValue();
            String mail = subModel.getMail();
            String nickname = subModel.getNickname();
            mimeMessageHelper.setTo(mail);

            String cancelSubUrl = serverUrl + "/api/redirect/cancel?userId=" + userId;
            String articleUrl = serverUrl + "/api/redirect/article?articleId=" + articleId;


            MessageFormat contentFormat = new MessageFormat("<html><head></head><body>" +
                    "<p><h2>{2}，您好</h2></p>" +
                    "<p><h2>{0}</h2><p>" +
                    "<p>{1}</p>" +
                    "<p><a href=\"{3}\">点击查看详情</a><p>" +
                    "<h5><a href=\"{4}\">退订此类邮件</a></h5>" +
                    "</body></html>");
            String contentHtml = contentFormat.format(new Object[]{title, content,nickname,articleUrl,cancelSubUrl});

            mimeMessageHelper.setText(contentHtml,true);

            mailSender.send(mimeMessage);
        }
    }


    @Async
    public void resetPassword(String url,String mail) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
        mimeMessageHelper.setFrom("hanata2020@163.com");


//        MessageFormat messageFormat = new MessageFormat("hanata新文章{0}");
        // title and pre content

        String subject = "您好，尊敬的hanata用户";

        mimeMessageHelper.setSubject(subject);

        MessageFormat contentFormat = new MessageFormat("<html><head></head><body>" +
                "<p><h2>您正在请求修改密码</h2><p>" +
                "<p><a href=\"{0}\">请点击此处修改密码,十五分钟内有效</a><p>" +
                "<h5>如非您本人请忽略此邮件，抱歉。</h5>" +
                "</body></html>");
        String contentHtml = contentFormat.format(new Object[]{url});

        mimeMessageHelper.setText(contentHtml,true);
        mimeMessageHelper.setTo(mail);

        mailSender.send(mimeMessage);
        }

}
