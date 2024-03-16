package com.ajou_nice.with_pet.service;

import com.ajou_nice.with_pet.domain.dto.sms.SmsAuthenticationRequest;
import com.ajou_nice.with_pet.exception.AppException;
import com.ajou_nice.with_pet.exception.ErrorCode;
import com.ajou_nice.with_pet.utils.CookieUtil;
import java.time.Duration;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SmsAuthenticationService {
    private final Integer SMS_MAXIMUM_COUNT = 5;
    private final String SMS_COOKIE_KEY = "withpet_sms_authentication";
    private final String COOL_SMS_DOMAIN = "https://api.coolsms.co.kr";
    private final String SMS_CONTENT = "[위드펫] 인증번호: ";
    private final Duration DURATION = Duration.ofMinutes(3);
    private final String SMS_MESSAGE = "인증번호가 발송되었습니다.";
    private final String SMS_VERIFY_MESSAGE = "인증되었습니다.";
    private final CookieUtil cookieUtil;
    private final RedisTemplate<String, String> redisTemplate;

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
        checkSmsAuthenticationShipmentCount(request, response);
        String authenticationNumber = sendSmsAuthenticationNumber(to);
        saveSmsAuthenticationNumber(to, authenticationNumber);
        return SMS_MESSAGE;
    }

    public String compareAuthenticationNumber(SmsAuthenticationRequest smsAuthenticationRequest) {
        String authenticationNumber = getSmsAuthenticationNumber(smsAuthenticationRequest.getPhone());
        if(!authenticationNumber.equals(smsAuthenticationRequest.getAuthenticationNumber())){
            throw new AppException(ErrorCode.INCONSISTENCY_AUTHENTICATION_NUMBER, ErrorCode.INCONSISTENCY_AUTHENTICATION_NUMBER.getMessage());
        }
        return SMS_VERIFY_MESSAGE;
    }

    private void checkSmsAuthenticationShipmentCount(HttpServletRequest request, HttpServletResponse response) {
        String smsAuthenticationNumberShipmentCount = cookieUtil.getCookieValue(request, SMS_COOKIE_KEY);
        if (smsAuthenticationNumberShipmentCount == null) {
            cookieUtil.addCookie(response, SMS_COOKIE_KEY, String.valueOf(1), "/");
        } else {
            if (Integer.parseInt(smsAuthenticationNumberShipmentCount) > SMS_MAXIMUM_COUNT) {
                throw new AppException(ErrorCode.TOO_MANY_SMS, ErrorCode.TOO_MANY_SMS.getMessage());
            }
            cookieUtil.addCookie(response, SMS_COOKIE_KEY,
                    String.valueOf(Integer.parseInt(smsAuthenticationNumberShipmentCount) + 1), "/");
        }
    }

    private String sendSmsAuthenticationNumber(String to) {
        DefaultMessageService messageService = NurigoApp.INSTANCE.initialize(api_key, api_secret, COOL_SMS_DOMAIN);
        Message message = new Message();
        message.setFrom(from);
        message.setTo(to);
        String random = this.randNum();
        message.setText(SMS_CONTENT + random);
        messageService.sendOne(new SingleMessageSendingRequest(message));
        return random;
    }

    private void saveSmsAuthenticationNumber(String to, String authenticationNumber) {
        ValueOperations<String, String> values = redisTemplate.opsForValue();
        values.set(to, authenticationNumber, DURATION);
    }

    private String getSmsAuthenticationNumber(String to) {
        ValueOperations<String, String> values = redisTemplate.opsForValue();
        if (values.get(to) == null) {
            return "false";
        }
        return (String) values.get(to);
    }
}
