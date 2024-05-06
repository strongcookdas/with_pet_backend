package com.ajou_nice.with_pet.applicant.util;

import com.ajou_nice.with_pet.domain.entity.User;
import com.ajou_nice.with_pet.enums.ApplicantStatus;
import com.ajou_nice.with_pet.exception.AppException;
import com.ajou_nice.with_pet.exception.ErrorCode;
import com.ajou_nice.with_pet.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class ApplicantValidation {
    private final UserRepository userRepository;

    public User applicationValidationById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> {
            throw new AppException(ErrorCode.USER_NOT_FOUND, ErrorCode.USER_NOT_FOUND.getMessage());
        });
        if (Objects.isNull(user.getApplicantStatus())) {
            throw new AppException(ErrorCode.NOT_FOUND_APPLICANT, ErrorCode.NOT_FOUND_APPLICANT.getMessage());
        }
        return user;
    }

    public void validApplicantAccept(User user) {
        if (user.getApplicantStatus().equals(ApplicantStatus.APPROVE)) {
            throw new AppException(ErrorCode.ALREADY_ACCEPT_APPLICANT, ErrorCode.ALREADY_ACCEPT_APPLICANT.getMessage());
        }
    }
}
