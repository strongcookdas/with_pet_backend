package com.ajou_nice.with_pet.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/hello")
@Api(tags = "Test API")
public class HelloController {

    @Value("${ajou.nice}")
    private String test;
    Response response = new Response("OK", "React SpringBoot!!!!");

    @GetMapping
    @ApiOperation(value = "CICD 테스트 API")
    public ResponseEntity<Response> hello() {
        return ResponseEntity.ok().body(response);
    }

    @AllArgsConstructor
    class Response {
        public String resultCode;
        public String result;
    }
}
