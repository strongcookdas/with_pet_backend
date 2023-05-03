package com.ajou_nice.with_pet.service;

import com.ajou_nice.with_pet.domain.dto.user.UserLoginRequest;
import com.ajou_nice.with_pet.domain.dto.user.UserLoginResponse;
import com.ajou_nice.with_pet.domain.dto.user.UserSignUpRequest;
import com.ajou_nice.with_pet.domain.dto.user.UserSignUpResponse;
import com.ajou_nice.with_pet.domain.entity.User;
import com.ajou_nice.with_pet.exception.AppException;
import com.ajou_nice.with_pet.exception.ErrorCode;
import com.ajou_nice.with_pet.repository.UserRepository;
import com.ajou_nice.with_pet.utils.CookieUtil;
import com.ajou_nice.with_pet.utils.JwtTokenUtil;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;
    private final JwtTokenUtil jwtTokenUtil;

    @Value("${jwt.token.secret")
    private String key;

    public UserSignUpResponse signUp(UserSignUpRequest userSignUpRequest) {

        userRepository.findById(userSignUpRequest.getUserId()).ifPresent(user -> {
            throw new AppException(ErrorCode.DUPLICATED_USER_ID,
                    ErrorCode.DUPLICATED_USER_ID.getMessage());
        });
        if (!userSignUpRequest.getUserPassword().equals(userSignUpRequest.getUserPasswordCheck())) {
            throw new AppException(ErrorCode.PASSWORD_COMPARE_FAIL,
                    ErrorCode.PASSWORD_COMPARE_FAIL.getMessage());
        }
        User user = userSignUpRequest.toUserEntity(encoder);
        User saveUser = userRepository.save(user);
        return UserSignUpResponse.of(saveUser);
    }

    public UserLoginResponse login(UserLoginRequest userLoginRequest,
            HttpServletResponse response) {

        User findUser = userRepository.findById(userLoginRequest.getId()).orElseThrow(() -> {
            throw new AppException(ErrorCode.USER_NOT_FOUND, ErrorCode.USER_NOT_FOUND.getMessage());
        });

        if (!encoder.matches(userLoginRequest.getPassword(), findUser.getPassword())) {
            throw new AppException(ErrorCode.INVALID_PASSWORD,
                    ErrorCode.INVALID_PASSWORD.getMessage());
        }

        String accessToken = jwtTokenUtil.createToken(findUser.getId(),
                findUser.getRole().name());

        CookieUtil.savePathCookie(response, "token", accessToken, "/");

        //테스트로 반환
        return UserLoginResponse.of("token : " + accessToken);
    }
}
