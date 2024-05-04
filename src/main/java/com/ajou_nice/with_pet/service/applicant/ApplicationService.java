package com.ajou_nice.with_pet.service.applicant;


import com.ajou_nice.with_pet.applicant.model.dto.PetsitterApplicationRequest;
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
public class ApplicationService {
    private final Integer APPLICATION_MAX_COUNT = 3;

    private final ValidateCollection valid;

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
