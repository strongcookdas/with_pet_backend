package com.ajou_nice.with_pet.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/hello")
public class HelloController {

    @Value("${ajou.nice}")
    private String test;

    @GetMapping
    public String hello() {
        return "hello "+test;
    }
}
