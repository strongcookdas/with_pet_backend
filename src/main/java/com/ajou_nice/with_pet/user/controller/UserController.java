package com.ajou_nice.with_pet.user.controller;

import com.ajou_nice.with_pet.domain.dto.Response;
import com.ajou_nice.with_pet.domain.dto.user.MyInfoModifyRequest;
import com.ajou_nice.with_pet.domain.dto.user.MyInfoModifyResponse;
import com.ajou_nice.with_pet.domain.dto.user.MyInfoResponse;
import com.ajou_nice.with_pet.user.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping("api/v1/users")
@RequiredArgsConstructor
@Api(tags = "User API")
@Slf4j
public class UserController {

    private final UserService userService;

    @GetMapping("/my-info")
    @ApiOperation(value = "회원정보 조회")
    public Response<MyInfoResponse> getMyInfo(@ApiIgnore Authentication authentication) {
        MyInfoResponse myInfoResponse = userService.getMyInfo(authentication.getName());
        log.info("-------------------------user MyInfo Response : {}-----------------------------",
                myInfoResponse);
        return Response.success(myInfoResponse);
    }

    @PutMapping("/my-info")
    @ApiOperation(value = "회원정보 수정")
    public Response modifyMyInfo(@ApiIgnore Authentication authentication,
            @Valid @RequestBody MyInfoModifyRequest myInfoModifyRequest) {
        System.out.println("myInfoModifyRequest : " + myInfoModifyRequest.toString());
        log.info("-------------------------user Info ModifyRequest : {}-----------------------------",
                myInfoModifyRequest);
        MyInfoModifyResponse myInfoModifyResponse = userService.modifyMyInfo(
                authentication.getName(), myInfoModifyRequest);
        log.info("-------------------------user Info ModifyResponse : {}-----------------------------",
                myInfoModifyRequest);
        return Response.success(myInfoModifyResponse);
    }
}
