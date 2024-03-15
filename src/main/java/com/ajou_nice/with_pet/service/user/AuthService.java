package com.ajou_nice.with_pet.service.user;

import com.ajou_nice.with_pet.domain.dto.auth.UserSignInRequest;
import com.ajou_nice.with_pet.domain.dto.auth.UserSignInResponse;
import com.ajou_nice.with_pet.domain.dto.auth.UserSignUpRequest;
import com.ajou_nice.with_pet.domain.dto.auth.UserSignUpResponse;
import com.ajou_nice.with_pet.domain.entity.User;
import com.ajou_nice.with_pet.exception.AppException;
import com.ajou_nice.with_pet.exception.ErrorCode;
import com.ajou_nice.with_pet.repository.UserRepository;
import com.ajou_nice.with_pet.service.ValidateCollection;
import com.ajou_nice.with_pet.utils.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;
    private final JwtTokenUtil jwtTokenUtil;
    private final ValidateCollection validateCollection;

    public UserSignUpResponse signUp(UserSignUpRequest userSignUpRequest) {

        //아이디 중복확인
        if (userRepository.existsByEmail(userSignUpRequest.getEmail())) {
            throw new AppException(ErrorCode.DUPLICATED_EMAIL,
                    ErrorCode.DUPLICATED_EMAIL.getMessage());
        }

        //비밀번호와 비밀번호 확인 비교
        if (!userSignUpRequest.getPassword().equals(userSignUpRequest.getPasswordCheck())) {
            throw new AppException(ErrorCode.PASSWORD_COMPARE_FAIL,
                    ErrorCode.PASSWORD_COMPARE_FAIL.getMessage());
        }

        User user = User.toUserEntity(userSignUpRequest, encoder);
        User saveUser = userRepository.save(user);
        return UserSignUpResponse.of(saveUser);
    }

    public UserSignInResponse login(UserSignInRequest userSignInRequest) {

        User findUser = validateCollection.userValidation(userSignInRequest.getEmail());

        if (!encoder.matches(userSignInRequest.getPassword(), findUser.getPassword())) {
            throw new AppException(ErrorCode.INVALID_PASSWORD,
                    ErrorCode.INVALID_PASSWORD.getMessage());
        }

        String accessToken = jwtTokenUtil.createToken(findUser.getEmail(),
                findUser.getRole().name());

        return UserSignInResponse.of(findUser, accessToken);
    }

//    public void logout(HttpServletResponse response){
//        cookieUtil.initCookie(response,"token",null,"/");
//    }
}
