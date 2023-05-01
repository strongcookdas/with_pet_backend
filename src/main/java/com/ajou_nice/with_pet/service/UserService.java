package com.ajou_nice.with_pet.service;

import com.ajou_nice.with_pet.domain.dto.user.UserSignUpRequest;
import com.ajou_nice.with_pet.domain.dto.user.UserSignUpResponse;
import com.ajou_nice.with_pet.domain.entity.User;
import com.ajou_nice.with_pet.exception.AppException;
import com.ajou_nice.with_pet.exception.ErrorCode;
import com.ajou_nice.with_pet.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserSignUpResponse signUp(UserSignUpRequest userSignUpRequest) {
        userRepository.findById(userSignUpRequest.getUserId()).ifPresent(user -> {
            throw new AppException(ErrorCode.DUPLICATED_USER_ID,
                    ErrorCode.DUPLICATED_USER_ID.getMessage());
        });

        if (!userSignUpRequest.getUserPassword().equals(userSignUpRequest.getUserPasswordCheck())) {
            throw new AppException(ErrorCode.PASSWORD_COMPARE_FAIL,
                    ErrorCode.PASSWORD_COMPARE_FAIL.getMessage());
        }
        User user = User.of(userSignUpRequest);
        User saveUser = userRepository.save(user);
        return UserSignUpResponse.of(saveUser);
    }
}
