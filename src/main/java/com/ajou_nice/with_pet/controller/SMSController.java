package com.ajou_nice.with_pet.controller;

import com.ajou_nice.with_pet.domain.dto.Response;
import com.ajou_nice.with_pet.service.SMSService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/certification")
@RequiredArgsConstructor
public class SMSController {

    private final SMSService smsService;

    @GetMapping
    public Response sendOne(@RequestParam String to){
        smsService.sendOne(to);
        return Response.success("인증번호를 발송했습니다.");
    }
}
