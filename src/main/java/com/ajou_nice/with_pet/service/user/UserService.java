package com.ajou_nice.with_pet.service.user;

import com.ajou_nice.with_pet.domain.dto.user.MyInfoModifyRequest;
import com.ajou_nice.with_pet.domain.dto.user.MyInfoModifyResponse;
import com.ajou_nice.with_pet.domain.dto.user.MyInfoResponse;
import com.ajou_nice.with_pet.domain.entity.User;
import com.ajou_nice.with_pet.exception.AppException;
import com.ajou_nice.with_pet.exception.ErrorCode;
import com.ajou_nice.with_pet.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;

    public MyInfoResponse getMyInfo(String userId) {

        User findUser = userRepository.findById(userId).orElseThrow(() -> {
            throw new AppException(ErrorCode.USER_NOT_FOUND, ErrorCode.USER_NOT_FOUND.getMessage());
        });

        return MyInfoResponse.toMyInfoResponse(findUser);
    }

    @Transactional
    public MyInfoModifyResponse modifyMyInfo(String userId,
            MyInfoModifyRequest myInfoModifyRequest) {

        User findUser = userRepository.findById(userId).orElseThrow(() -> {
            throw new AppException(ErrorCode.USER_NOT_FOUND, ErrorCode.USER_NOT_FOUND.getMessage());
        });

        findUser.updateUser(myInfoModifyRequest, encoder);
        return MyInfoModifyResponse.toMyInfoModifyResponse(findUser);
    }
}
