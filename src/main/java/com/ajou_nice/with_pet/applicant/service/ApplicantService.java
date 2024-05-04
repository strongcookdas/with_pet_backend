package com.ajou_nice.with_pet.applicant.service;


import com.ajou_nice.with_pet.applicant.model.dto.PetsitterApplicationRequest;
import com.ajou_nice.with_pet.applicant.model.dto.PetsitterApplicationRequest.ApplicantModifyRequest;
import com.ajou_nice.with_pet.applicant.model.dto.PetSitterApplicationResponse;
import com.ajou_nice.with_pet.domain.entity.User;
import com.ajou_nice.with_pet.enums.ApplicantStatus;
import com.ajou_nice.with_pet.enums.UserRole;
import com.ajou_nice.with_pet.exception.AppException;
import com.ajou_nice.with_pet.exception.ErrorCode;
import com.ajou_nice.with_pet.service.ValidateCollection;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ApplicantService {

    private final Integer APPLICATION_MAX_COUNT = 3;
    private final ValidateCollection valid;

    // == 유저의 지원 정보 상세 확인 == //
    //리팩토링 완
    public PetSitterApplicationResponse showApplicateInfo(String userId) {
        User findUser = valid.userValidationById(userId);
        //지원자가 아닐 경우 오류 출력
        if (!findUser.getRole().equals(UserRole.ROLE_APPLICANT)) {
            throw new AppException(ErrorCode.APPLICANT_NOT_FOUND,
                    ErrorCode.APPLICANT_NOT_FOUND.getMessage());
        }

        return PetSitterApplicationResponse.of(findUser);
    }

    // == 유저의 펫시터 지원서류 수정 == //
    //리팩토링 완
    @Transactional
    public PetSitterApplicationResponse modifyApplicateInfo(String userId,
                                                            ApplicantModifyRequest applicantModifyRequest) {
        User findUser = valid.userValidationById(userId);

        // 지원 정보 수정
        findUser.updateApplicantInfo(applicantModifyRequest);

        return PetSitterApplicationResponse.of(findUser);
    }
    @Transactional
    public PetSitterApplicationResponse applyPetsitter(PetsitterApplicationRequest petsitterApplicationRequest,
                                                       String email) {

        User user = valid.userValidationByEmail(email);

        validApplicantStatus(user);
        validApplicationCount(user);

        user.updateApplicantCount();
        user.updateApplicantStatus(ApplicantStatus.WAIT);
        user.updateUserRole(UserRole.ROLE_APPLICANT);
        user.updateApplicantInfo(petsitterApplicationRequest);

        return PetSitterApplicationResponse.of(user);
    }

    /**
     * 지원시 유저의 유효성 체크
     **/
    private void validApplicantStatus(User user) {

        // 유저 펫시터 지원 중복 체크
        // 유저의 role이 지원자이고 , 지원상태가 WAIT일때는 중복 지원 금지
        if (user.getRole().equals(UserRole.ROLE_APPLICANT) && user.getApplicantStatus()
                .equals(ApplicantStatus.WAIT)) {
            throw new AppException(ErrorCode.DUPLICATED_APPLICATION,
                    ErrorCode.DUPLICATED_APPLICATION.getMessage());
        }
    }

    private void validApplicationCount(User user) {
        if (user.getApplicantCount().equals(APPLICATION_MAX_COUNT)) {
            throw new AppException(ErrorCode.TO_MANY_APPLICATE,
                    ErrorCode.TO_MANY_APPLICATE.getMessage());
        }
    }
}