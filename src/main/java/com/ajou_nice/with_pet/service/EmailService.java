package com.ajou_nice.with_pet.service;

import com.ajou_nice.with_pet.domain.entity.UserParty;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;

    @Async("threadPoolTaskExecutor")
    public void sendEmail(String to, String subject, String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(content);
        javaMailSender.send(message);
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