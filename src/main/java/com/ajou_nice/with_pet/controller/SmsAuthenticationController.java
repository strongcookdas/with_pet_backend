package com.ajou_nice.with_pet.controller;

import com.ajou_nice.with_pet.domain.dto.Response;
import com.ajou_nice.with_pet.domain.dto.sms.SmsAuthenticationRequest;
import com.ajou_nice.with_pet.service.SmsAuthenticationService;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v2/sms-authentication")
@RequiredArgsConstructor
public class SmsAuthenticationController {
    private final SmsAuthenticationService smsAuthenticationService;

    @GetMapping
    public Response sendSmsAuthenticationNumber(@RequestParam String to, HttpServletRequest request,
                                                HttpServletResponse response) {
        String message = smsAuthenticationService.sendOne(to, request, response);
        return Response.success(message);
    }

    @PostMapping
    public Response compareSmsAuthenticationNumber(@RequestBody SmsAuthenticationRequest smsAuthenticationRequest){
        String message = smsAuthenticationService.compareAuthenticationNumber(smsAuthenticationRequest);
        return Response.success(message);
    }
}
