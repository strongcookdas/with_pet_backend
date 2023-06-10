package com.ajou_nice.with_pet.service;

import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SMSService {

    @Value("${SMS.KEY}")
    private String api_key;

    @Value("${SMS.SECRET}")
    private String api_secret;

    @Value("${SMS.PHONE}")
    private String from;

    private String randNum(){
        return RandomStringUtils.randomNumeric(6);
    }

    public SingleMessageSentResponse sendOne(String to){
        DefaultMessageService messageService = NurigoApp.INSTANCE.initialize(api_key, api_secret, "https://api.coolsms.co.kr");
        Message message = new Message();
        message.setFrom(from);
        message.setTo(to);
        message.setText("[위드펫] 인증번호: "+ this.randNum());

        SingleMessageSentResponse response = messageService.sendOne(new SingleMessageSendingRequest(message));
        return response;
    }
}
