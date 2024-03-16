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
        checkDuplicatedUserByEmail(userSignUpRequest.getEmail());
        compareSignUpPasswordCheck(userSignUpRequest.getPassword(), userSignUpRequest.getPasswordCheck());

        User user = User.of(userSignUpRequest, encoder);
        User saveUser = userRepository.save(user);
        return UserSignUpResponse.of(saveUser);
    }

    public UserSignInResponse SignIn(UserSignInRequest userSignInRequest) {

        User user = validateCollection.userValidationByEmail(userSignInRequest.getEmail());
        comparePassword(userSignInRequest.getPassword(), user.getPassword());
        String accessToken = jwtTokenUtil.createToken(user.getEmail(),
                user.getRole().name());

        return UserSignInResponse.of(user, accessToken);
    }

//    public void logout(HttpServletResponse response){
//        cookieUtil.initCookie(response,"token",null,"/");
//    }

    private void checkDuplicatedUserByEmail(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new AppException(ErrorCode.DUPLICATED_EMAIL,
                    ErrorCode.DUPLICATED_EMAIL.getMessage());
        }
    }

    private void compareSignUpPasswordCheck(String password, String passwordCheck) {
        if (password.equals(passwordCheck)) {
            throw new AppException(ErrorCode.PASSWORD_COMPARE_FAIL,
                    ErrorCode.PASSWORD_COMPARE_FAIL.getMessage());
        }
    }

    private void comparePassword(String input, String password) {
        if (!encoder.matches(input, password)) {
            throw new AppException(ErrorCode.INVALID_PASSWORD,
                    ErrorCode.INVALID_PASSWORD.getMessage());
        }
    }
}
