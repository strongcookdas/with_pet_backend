package com.ajou_nice.with_pet.controller;

import com.ajou_nice.with_pet.domain.dto.Response;
import com.ajou_nice.with_pet.service.SMSService;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
    public Response sendOne(@RequestParam String to, HttpServletRequest request,
            HttpServletResponse response) {
        String random = smsService.sendOne(to, request, response);
        return Response.success(random);
    }
}
