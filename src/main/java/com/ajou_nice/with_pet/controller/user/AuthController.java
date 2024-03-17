package com.ajou_nice.with_pet.controller.user;

import com.ajou_nice.with_pet.domain.dto.Response;
import com.ajou_nice.with_pet.domain.dto.auth.UserSignInRequest;
import com.ajou_nice.with_pet.domain.dto.auth.UserSignInResponse;
import com.ajou_nice.with_pet.domain.dto.auth.UserSignUpRequest;
import com.ajou_nice.with_pet.domain.dto.auth.UserSignUpResponse;
import com.ajou_nice.with_pet.service.user.AuthService;
import com.ajou_nice.with_pet.utils.CookieUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping("api/v2/users")
@RequiredArgsConstructor
@Api(tags = "Auth API")
@Slf4j
public class AuthController {

    private final AuthService userService;
    private final CookieUtil cookieUtil;

    @PostMapping("/sign-up")
    @ApiOperation(value = "회원가입")
    public Response<UserSignUpResponse> signUp(
            @Valid @RequestBody UserSignUpRequest userSignUpRequest) {
        UserSignUpResponse userSignUpResponse = userService.signUp(userSignUpRequest);
        return Response.success(userSignUpResponse);
    }

    @PostMapping("/sign-in")
    @ApiOperation(value = "로그인")
    public Response<UserSignInResponse> signIn(@RequestBody UserSignInRequest userSignInRequest,
                                               HttpServletResponse response) {
        UserSignInResponse userSignInResponse = userService.SignIn(userSignInRequest);
        cookieUtil.addCookie(response, "token", userSignInResponse.getToken(), "/");
        return Response.success(userSignInResponse);
    }

    @GetMapping("/sign-out")
    @ApiOperation(value = "로그아웃")
    public Response<?> signOut(@ApiIgnore Authentication authentication,
                            HttpServletResponse httpServletResponse) {
        cookieUtil.initCookie(httpServletResponse,"token",null,"/");
        return Response.success("로그아웃되었습니다.");
    }
}
