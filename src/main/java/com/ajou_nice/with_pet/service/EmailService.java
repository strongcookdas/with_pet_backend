package com.ajou_nice.with_pet.service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;

    @Async("threadPoolTaskExecutor")
    public void sendEmail(String to, String subject, String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(content);
        javaMailSender.send(message);
    }

    @Async("threadPoolTaskExecutor")
    public void sendHtmlEmail(String to, String subject, String photoUrl, String content, String linkUrl) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(to);
            helper.setSubject(subject);

            // Thymeleaf 템플릿 엔진을 사용하여 HTML 템플릿을 렌더링합니다.
            Context context = new Context();
            context.setVariable("title", subject);
            context.setVariable("image", photoUrl);
            context.setVariable("content", content);
            context.setVariable("url", linkUrl);
            String htmlContent = templateEngine.process("mail", context);

            helper.setText(htmlContent, true);
            javaMailSender.send(message);
        } catch (MessagingException e) {
            // 예외 처리
        }
    }

//    @Async
//    public void sendEmails(List<UserParty> userParties, String subject, String content) {
//        userParties.forEach(u -> {
//            SimpleMailMessage message = new SimpleMailMessage();
//            message.setTo(u.getUser().getEmail());
//            message.setSubject(subject);
//            message.setText(content);
//            javaMailSender.send(message);
//        });
//    }
}