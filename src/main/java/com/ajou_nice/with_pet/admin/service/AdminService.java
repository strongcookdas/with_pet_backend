package com.ajou_nice.with_pet.admin.service;

import com.ajou_nice.with_pet.admin.model.admin.AdminApplicantRequest;
import com.ajou_nice.with_pet.admin.model.admin.AdminApplicantResponse;
import com.ajou_nice.with_pet.admin.util.AdminValidation;
import com.ajou_nice.with_pet.admin.model.criticalservice.AddCriticalServiceRequest;
import com.ajou_nice.with_pet.admin.model.criticalservice.AddCriticalServiceRequest.CriticalServiceModifyRequest;
import com.ajou_nice.with_pet.admin.model.criticalservice.CriticalServiceResponse;
import com.ajou_nice.with_pet.domain.dto.petsitter.PetSitterBasicResponse;
import com.ajou_nice.with_pet.domain.dto.petsitterapplicant.ApplicantBasicInfoResponse;
import com.ajou_nice.with_pet.dto.applicant.PetsitterApplicationResponse;
import com.ajou_nice.with_pet.admin.model.withpetservice.AddWithPetServiceRequest;
import com.ajou_nice.with_pet.admin.model.withpetservice.AddWithPetServiceRequest.WithPetServiceModifyRequest;
import com.ajou_nice.with_pet.admin.model.withpetservice.WithPetServiceResponse;
import com.ajou_nice.with_pet.domain.entity.CriticalService;
import com.ajou_nice.with_pet.domain.entity.Notification;
import com.ajou_nice.with_pet.domain.entity.PetSitter;
import com.ajou_nice.with_pet.domain.entity.User;
import com.ajou_nice.with_pet.domain.entity.WithPetService;
import com.ajou_nice.with_pet.enums.ApplicantStatus;
import com.ajou_nice.with_pet.enums.NotificationType;
import com.ajou_nice.with_pet.enums.UserRole;
import com.ajou_nice.with_pet.exception.AppException;
import com.ajou_nice.with_pet.exception.ErrorCode;
import com.ajou_nice.with_pet.repository.CriticalServiceRepository;
import com.ajou_nice.with_pet.repository.PetSitterRepository;
import com.ajou_nice.with_pet.repository.UserRepository;
import com.ajou_nice.with_pet.repository.WithPetServiceRepository;
import com.ajou_nice.with_pet.service.NotificationService;
import com.ajou_nice.with_pet.service.ValidateCollection;
import com.ajou_nice.with_pet.service.user.UserService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final PetSitterRepository petSitterRepository;
    private final WithPetServiceRepository withPetServiceRepository;

    private final UserService userService;

    private final CriticalServiceRepository criticalServiceRepository;

    private final ValidateCollection validateCollection;
    private final NotificationService notificationService;
    private final AdminValidation adminValidation;

    // == 관리자의 펫시터 지원자 리스트 전체 확인 == //
    // 리팩토링 완 -> Authentication자체가 없어도 되는지도 확인 필요 (나중에 권한 설정한 다음에)
    public List<ApplicantBasicInfoResponse> showApplicants(String email) {

        validateCollection.userValidationByEmail(email);

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

    // == 관리자의 펫시터 지원자 수락 == //
    // 리팩토링 완 -> Authentication 자체가 없어도 되는지도 확인 필요 (나중에 권한 설정한 다음에)
    @Transactional
    public PetSitterBasicResponse createPetsitter(String userId,
                                                  AdminApplicantRequest adminApplicantRequest) {

        validateCollection.userValidation(userId);

        //유저 불러오기
        User findUser = userRepository.findById(adminApplicantRequest.getUserId())
                .orElseThrow(() -> {
                    throw new AppException(ErrorCode.USER_NOT_FOUND,
                            ErrorCode.USER_NOT_FOUND.getMessage());
                });
        //펫시터 지원자 상태 변경 + userRole 변경
        findUser.updateApplicantStatus(ApplicantStatus.APPROVE);
        findUser.updateUserRole(UserRole.ROLE_PETSITTER);

        PetSitter petSitter = PetSitter.toEntity(findUser);
        PetSitter newPetSitter = petSitterRepository.save(petSitter);

        Notification notification = notificationService.sendEmail(String.format(
                        "%s님의 위드펫 지원이 승인되었습니다.\n아래의 URL에 접속하여 펫시터 정보를 기입 후 저장을 하면 메인 페이지에 %s님의 펫시터 정보가 게시됩니다.",
                        findUser.getName(), findUser.getName()),
                "/petsitterShowInfo",
                NotificationType.펫시터_승인, findUser);
        notificationService.saveNotification(notification);

        return PetSitterBasicResponse.of(newPetSitter);
    }

    // == 관리자의 펫시터 지원자 거절 == //
    // 리팩토링 완 -> Authentication 자체가 없어도 되는지도 확인 필요 (나중에 권한 설정한 다음에)
    @Transactional
    public AdminApplicantResponse refuseApplicant(String userId,
                                                  AdminApplicantRequest adminApplicantRequest) {

        validateCollection.userValidation(userId);

        //유저 불러오기
        User findUser = userRepository.findById(adminApplicantRequest.getUserId())
                .orElseThrow(() -> {
                    throw new AppException(ErrorCode.USER_NOT_FOUND,
                            ErrorCode.USER_NOT_FOUND.getMessage());
                });

        //펫시터 지원자 상태 변경 + userRole 변경
        findUser.updateApplicantStatus(ApplicantStatus.REFUSE);
        findUser.updateUserRole(UserRole.ROLE_USER);

        Notification notification = notificationService.sendEmail(String.format(
                "유감스럽지만 %s님의 위드펫 지원이 거절되었습니다.",
                findUser.getName()), "/", NotificationType.펫시터_거절, findUser);
        notificationService.saveNotification(notification);

        return AdminApplicantResponse.of(findUser);
    }


    // == 관리자의 펫시터 지원자 한명 상세정보 확인 == //
    // 리팩토링 완
    public PetsitterApplicationResponse getApplicantInfo(String id, Long userId) {

        validateCollection.userValidation(userId);

        //유저 불러오기
        User findUser = userRepository.findById(userId).orElseThrow(() -> {
            throw new AppException(ErrorCode.USER_NOT_FOUND, ErrorCode.USER_NOT_FOUND.getMessage());
        });

        return PetsitterApplicationResponse.of(findUser);
    }

    // == 관리자의 위드펫 서비스 리스트 조회 == //
    public List<WithPetServiceResponse> showWithPetServices() {

        List<WithPetService> withPetServiceList = withPetServiceRepository.findAll();

        return WithPetServiceResponse.toList(withPetServiceList);
    }

    // == 관리자의 필수 서비스 리스트 조회 == //
    public List<CriticalServiceResponse> showCriticalServices(String userId) {
        validateCollection.userValidation(userId);
        List<CriticalService> criticalServiceList = criticalServiceRepository.findAll();

        return CriticalServiceResponse.toList(criticalServiceList);
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
    // == 관리자의 필수 서비스 수정 == //
    public CriticalServiceResponse updateCriticalService(
            String userId, CriticalServiceModifyRequest criticalServiceModifyRequest) {
        validateCollection.userValidation(userId);
        CriticalService criticalService = validateCollection.criticalServiceValidation(
                criticalServiceModifyRequest.getServiceId());
        criticalService.updateServiceInfo(criticalServiceModifyRequest);

        return CriticalServiceResponse.of(criticalService);
    }

    @Transactional
    // == 관리자의 위드펫에서 제공하는 서비스 수정 == //
    public WithPetServiceResponse updateWithPetService(String userId,
                                                       WithPetServiceModifyRequest withPetServiceModifyRequest) {
        validateCollection.userValidation(userId);
        WithPetService withPetService = validateCollection.withPetServiceValidation(
                withPetServiceModifyRequest.getServiceId());
        withPetService.updateServiceInfo(withPetServiceModifyRequest);

        return WithPetServiceResponse.of(withPetService);
    }

    @Transactional
    // == 관리자의 위드펫에서 제공하는 서비스 삭제 == //
    public List<WithPetServiceResponse> deleteWithPetService(String userId,
                                                             WithPetServiceModifyRequest withPetServiceModifyRequest) {
        validateCollection.userValidation(userId);
        WithPetService withPetService = validateCollection.withPetServiceValidation(
                withPetServiceModifyRequest.getServiceId());

        withPetServiceRepository.delete(withPetService);
        List<WithPetService> withPetServiceList = withPetServiceRepository.findAll();

        return WithPetServiceResponse.toList(withPetServiceList);
    }
}
