package com.ajou_nice.with_pet.admin.service;

import com.ajou_nice.with_pet.admin.model.dto.*;
import com.ajou_nice.with_pet.admin.model.dto.AddCriticalServiceRequest.CriticalServiceModifyRequest;
import com.ajou_nice.with_pet.admin.model.dto.AddWithPetServiceRequest.WithPetServiceModifyRequest;
import com.ajou_nice.with_pet.admin.util.AdminValidation;
import com.ajou_nice.with_pet.applicant.util.ApplicantValidation;
import com.ajou_nice.with_pet.critical_service.model.dto.CriticalServiceResponse;
import com.ajou_nice.with_pet.critical_service.model.entity.CriticalService;
import com.ajou_nice.with_pet.critical_service.repository.CriticalServiceRepository;
import com.ajou_nice.with_pet.domain.dto.petsitter.PetSitterBasicResponse;
import com.ajou_nice.with_pet.applicant.model.dto.ApplicantBasicInfoResponse;
import com.ajou_nice.with_pet.domain.entity.Notification;
import com.ajou_nice.with_pet.domain.entity.PetSitter;
import com.ajou_nice.with_pet.domain.entity.User;
import com.ajou_nice.with_pet.applicant.model.dto.PetSitterApplicationResponse;
import com.ajou_nice.with_pet.enums.ApplicantStatus;
import com.ajou_nice.with_pet.enums.NotificationType;
import com.ajou_nice.with_pet.enums.UserRole;
import com.ajou_nice.with_pet.exception.AppException;
import com.ajou_nice.with_pet.exception.ErrorCode;
import com.ajou_nice.with_pet.repository.PetSitterRepository;
import com.ajou_nice.with_pet.repository.UserRepository;
import com.ajou_nice.with_pet.service.NotificationService;
import com.ajou_nice.with_pet.service.ValidateCollection;
import com.ajou_nice.with_pet.withpet_service.model.dto.WithPetServiceResponse;
import com.ajou_nice.with_pet.withpet_service.model.entity.WithPetService;
import com.ajou_nice.with_pet.withpet_service.repository.WithPetServiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final PetSitterRepository petSitterRepository;
    private final WithPetServiceRepository withPetServiceRepository;
    private final CriticalServiceRepository criticalServiceRepository;

//    private final NotificationService notificationService;

    private final ValidateCollection validateCollection;
    private final AdminValidation adminValidation;
    private final ApplicantValidation applicantValidation;

    public List<ApplicantBasicInfoResponse> showApplicants(String email) {

        adminValidation.adminValidation(email);
        // user가 admin인지 확인하는 유효성 체크인데... 필요가 있는지 의문

        List<User> petSitterApplicantList = userRepository.findApplicantAllInQuery(
                UserRole.ROLE_APPLICANT, ApplicantStatus.WAIT);

        return ApplicantBasicInfoResponse.toList(petSitterApplicantList);
    }

    // == 관리자의 펫시터 리스트 전체 확인 == //
    public List<PetSitterBasicResponse> showPetSitters(String userId) {
//		valid.userValidation(userId);
        validateCollection.userValidationByEmail(userId);
        List<PetSitter> petSitters = petSitterRepository.findAll();

        return PetSitterBasicResponse.toList(petSitters);
    }

    @Transactional
    public PetSitterBasicResponse acceptApplicant(String email, Long applicantId) {

        adminValidation.adminValidation(email);

        User applicant = applicantValidation.applicationValidationById(applicantId);

        applicantValidation.validApplicantAccept(applicant);
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

    // == 관리자의 펫시터 지원자 거절 == //
    // 리팩토링 완 -> Authentication 자체가 없어도 되는지도 확인 필요 (나중에 권한 설정한 다음에)
    @Transactional
    public AdminApplicantResponse refuseApplicant(String email, Long applicantId) {

        adminValidation.adminValidation(email);

        User findUser = applicantValidation.applicationValidationById(applicantId);

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
        adminValidation.adminValidation(email);
        User findUser = applicantValidation.applicationValidationById(userId);
        return PetSitterApplicationResponse.of(findUser);
    }

    @Transactional
    public CriticalServiceResponse addCriticalService(String email,
                                                      AddCriticalServiceRequest addCriticalServiceRequest) {
        adminValidation.adminValidation(email);
        adminValidation.existCriticalServiceValidation(addCriticalServiceRequest);

        CriticalService criticalService = CriticalService.toEntity(addCriticalServiceRequest);
        CriticalService newCriticalService = criticalServiceRepository.save(criticalService);

        return CriticalServiceResponse.of(newCriticalService);
    }

    @Transactional
    public WithPetServiceResponse addWithPetService(String email,
                                                    AddWithPetServiceRequest addWithPetServiceRequest) {
        adminValidation.adminValidation(email);
        WithPetService withPetService = WithPetService.toEntity(addWithPetServiceRequest);
        WithPetService newWithPetService = withPetServiceRepository.save(withPetService);

        return WithPetServiceResponse.of(newWithPetService);
    }

    @Transactional
    public UpdateCriticalServiceResponse updateCriticalService(String email, Long serviceId, UpdateCriticalServiceRequest updateCriticalServiceRequest) {
        adminValidation.adminValidation(email);
        CriticalService criticalService = criticalServiceValidation(serviceId);
        criticalService.updateServiceInfo(updateCriticalServiceRequest);
        return UpdateCriticalServiceResponse.of(criticalService);
    }

    @Transactional
    public WithPetServiceResponse updateWithPetService(String email,Long serviceId, UpdateWithPetServiceRequest updateWithPetServiceRequest) {
        adminValidation.adminValidation(email);
        WithPetService withPetService = withPetServiceValidation(serviceId);
        withPetService.updateServiceInfo(updateWithPetServiceRequest);
        return WithPetServiceResponse.of(withPetService);
    }

    @Transactional
    // == 관리자의 위드펫에서 제공하는 서비스 삭제 == //
    public List<WithPetServiceResponse> deleteWithPetService(String userId,
                                                             WithPetServiceModifyRequest withPetServiceModifyRequest) {
        validateCollection.userValidationById(userId);
        WithPetService withPetService = validateCollection.withPetServiceValidation(
                withPetServiceModifyRequest.getServiceId());

        withPetServiceRepository.delete(withPetService);
        List<WithPetService> withPetServiceList = withPetServiceRepository.findAll();

        return WithPetServiceResponse.toList(withPetServiceList);
    }

    private CriticalService criticalServiceValidation(Long serviceId) {
        CriticalService criticalService = criticalServiceRepository.findById(serviceId)
                .orElseThrow(() -> {
                    throw new AppException(ErrorCode.CRITICAL_SERVICE_NOT_FOUND,
                            ErrorCode.CRITICAL_SERVICE_NOT_FOUND.getMessage());
                });

        return criticalService;
    }

    private WithPetService withPetServiceValidation(Long serviceId) {
        WithPetService withPetService = withPetServiceRepository.findById(serviceId)
                .orElseThrow(() -> {
                    throw new AppException(ErrorCode.WITH_PET_SERVICE_NOT_FOUND,
                            ErrorCode.WITH_PET_SERVICE_NOT_FOUND.getMessage());
                });

        return withPetService;
    }
}
