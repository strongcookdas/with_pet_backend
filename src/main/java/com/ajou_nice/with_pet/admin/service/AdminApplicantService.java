package com.ajou_nice.with_pet.admin.service;

import com.ajou_nice.with_pet.admin.model.dto.AdminApplicantResponse;
import com.ajou_nice.with_pet.applicant.model.dto.ApplicantBasicInfoResponse;
import com.ajou_nice.with_pet.applicant.model.dto.PetSitterApplicationResponse;
import com.ajou_nice.with_pet.domain.dto.petsitter.PetSitterBasicResponse;
import com.ajou_nice.with_pet.domain.entity.PetSitter;
import com.ajou_nice.with_pet.domain.entity.User;
import com.ajou_nice.with_pet.enums.ApplicantStatus;
import com.ajou_nice.with_pet.enums.UserRole;
import com.ajou_nice.with_pet.exception.AppException;
import com.ajou_nice.with_pet.exception.ErrorCode;
import com.ajou_nice.with_pet.repository.PetSitterRepository;
import com.ajou_nice.with_pet.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AdminApplicantService {
    private final UserRepository userRepository;
    private final PetSitterRepository petSitterRepository;

//    private final NotificationService notificationService;

    public List<ApplicantBasicInfoResponse> showApplicants(String email) {

        adminValidation(email);

        List<User> petSitterApplicantList = userRepository.findApplicantAllInQuery(
                UserRole.ROLE_APPLICANT, ApplicantStatus.WAIT);

        return ApplicantBasicInfoResponse.toList(petSitterApplicantList);
    }

    @Transactional
    public PetSitterBasicResponse acceptApplicant(String email, Long applicantId) {
        adminValidation(email);
        User applicant = applicationValidationById(applicantId);

        validApplicantAccept(applicant);
        applicant.updateApplicantRoleAndStatus(UserRole.ROLE_PETSITTER, ApplicantStatus.APPROVE);

        PetSitter petSitter = PetSitter.toEntity(applicant);
        PetSitter newPetSitter = petSitterRepository.save(petSitter);

        /* 메일 알림 메서드 리팩토링 필요
        Notification notification = notificationService.sendEmail(String.format(
                        "%s님의 위드펫 지원이 승인되었습니다.\n아래의 URL에 접속하여 펫시터 정보를 기입 후 저장을 하면 메인 페이지에 %s님의 펫시터 정보가 게시됩니다.",
                        findUser.getName(), findUser.getName()),
                "/petsitterShowInfo",
                NotificationType.펫시터_승인, findUser);
        notificationService.saveNotification(notification);
        */

        return PetSitterBasicResponse.of(newPetSitter);
    }

    @Transactional
    public AdminApplicantResponse refuseApplicant(String email, Long applicantId) {
        adminValidation(email);

        User findUser = applicationValidationById(applicantId);

        findUser.updateApplicantRoleAndStatus(UserRole.ROLE_USER, ApplicantStatus.REFUSE);

        /*
        Notification notification = notificationService.sendEmail(String.format(
                "유감스럽지만 %s님의 위드펫 지원이 거절되었습니다.",
                findUser.getName()), "/", NotificationType.펫시터_거절, findUser);
        notificationService.saveNotification(notification);
        */

        return AdminApplicantResponse.of(findUser);
    }

    public PetSitterApplicationResponse getApplicantDetailInfo(String email, Long userId) {
        adminValidation(email);
        User findUser = applicationValidationById(userId);
        return PetSitterApplicationResponse.of(findUser);
    }

    private void adminValidation(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> {
            throw new AppException(ErrorCode.USER_NOT_FOUND, ErrorCode.USER_NOT_FOUND.getMessage());
        });

        if (!user.getRole().equals(UserRole.ROLE_ADMIN)) {

            throw new AppException(ErrorCode.UNAUTHORIZED_ADMIN, ErrorCode.UNAUTHORIZED_ADMIN.getMessage());
        }
    }

    private User applicationValidationById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> {
            throw new AppException(ErrorCode.USER_NOT_FOUND, ErrorCode.USER_NOT_FOUND.getMessage());
        });
        if (Objects.isNull(user.getApplicantStatus())) {
            throw new AppException(ErrorCode.NOT_FOUND_APPLICANT, ErrorCode.NOT_FOUND_APPLICANT.getMessage());
        }
        return user;
    }

    private void validApplicantAccept(User user) {
        if (user.getApplicantStatus().equals(ApplicantStatus.APPROVE)) {
            throw new AppException(ErrorCode.ALREADY_ACCEPT_APPLICANT, ErrorCode.ALREADY_ACCEPT_APPLICANT.getMessage());
        }
    }
}
