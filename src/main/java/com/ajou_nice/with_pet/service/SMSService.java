package com.ajou_nice.with_pet.service;

import com.ajou_nice.with_pet.exception.AppException;
import com.ajou_nice.with_pet.exception.ErrorCode;
import com.ajou_nice.with_pet.utils.CookieUtil;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SMSService {

    private final Integer smsCount = 5;
    private final CookieUtil cookieUtil;
    @Value("${SMS.KEY}")
    private String api_key;

    @Value("${SMS.SECRET}")
    private String api_secret;

    @Value("${SMS.PHONE}")
    private String from;

    private String randNum() {
        return RandomStringUtils.randomNumeric(6);
    }

    public String sendOne(String to, HttpServletRequest request, HttpServletResponse response) {

        String sms = cookieUtil.getCookieValue(request, "sms");
        if (sms == null) {
            cookieUtil.addCookie(response, "sms", String.valueOf(1), "/");
        } else {
            if (Integer.parseInt(sms) > 5) {
                throw new AppException(ErrorCode.TOO_MANY_SMS, ErrorCode.TOO_MANY_SMS.getMessage());
            }
            cookieUtil.addCookie(response, "sms", String.valueOf(Integer.parseInt(sms) + 1), "/");
        }

        DefaultMessageService messageService = NurigoApp.INSTANCE.initialize(api_key, api_secret,
                "https://api.coolsms.co.kr");
        Message message = new Message();
        message.setFrom(from);
        message.setTo(to);
        String random = this.randNum();
        message.setText("[위드펫] 인증번호: " + random);

        SingleMessageSentResponse messageSentResponse = messageService.sendOne(
                new SingleMessageSendingRequest(message));
        return random;
    }
}
