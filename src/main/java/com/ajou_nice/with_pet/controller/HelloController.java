package com.ajou_nice.with_pet.controller;

import com.ajou_nice.with_pet.domain.dto.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/hello")
@Api(tags = "Test API")
public class HelloController {

    /*
    테스트
     */
    @GetMapping
    @ApiOperation(value = "CICD 테스트 API")
    public Response hello(Authentication authentication) {
        return Response.success(authentication.getName());
    }

}
