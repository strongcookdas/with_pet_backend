package com.ajou_nice.with_pet.service.user;

import com.ajou_nice.with_pet.domain.dto.user.MyInfoModifyRequest;
import com.ajou_nice.with_pet.domain.dto.user.MyInfoModifyResponse;
import com.ajou_nice.with_pet.domain.dto.user.MyInfoResponse;
import com.ajou_nice.with_pet.domain.entity.User;
import com.ajou_nice.with_pet.exception.AppException;
import com.ajou_nice.with_pet.exception.ErrorCode;
import com.ajou_nice.with_pet.repository.UserRepository;
import com.ajou_nice.with_pet.service.ValidateCollection;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;
    private final ValidateCollection valid;

    public MyInfoResponse getMyInfo(String userId) {

        User findUser = valid.userValidation(userId);

        return MyInfoResponse.toMyInfoResponse(findUser);
    }

    @Transactional
    public MyInfoModifyResponse modifyMyInfo(String userId,
            MyInfoModifyRequest myInfoModifyRequest) {

        User findUser = valid.userValidation(userId);

        findUser.updateUser(myInfoModifyRequest, encoder);
        return MyInfoModifyResponse.toMyInfoModifyResponse(findUser);
    }
}
