package com.ajou_nice.with_pet.controller;

import com.ajou_nice.with_pet.domain.dto.Response;
import com.ajou_nice.with_pet.domain.dto.user.UserSignUpRequest;
import com.ajou_nice.with_pet.domain.dto.user.UserSignUpResponse;
import com.ajou_nice.with_pet.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/users")
@RequiredArgsConstructor
@Api
@Slf4j
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    @ApiOperation(value = "회원가입")
    public Response<UserSignUpResponse> signUp(@RequestBody UserSignUpRequest userSignUpRequest) {
        log.info(userSignUpRequest.toString());
        UserSignUpResponse userSignUpResponse = userService.signUp(userSignUpRequest);
        return Response.success(userSignUpResponse);
    }
}
