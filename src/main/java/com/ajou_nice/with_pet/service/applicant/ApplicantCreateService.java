package com.ajou_nice.with_pet.service.applicant;


import com.ajou_nice.with_pet.dto.applicant.ApplicantCreateRequest;
import com.ajou_nice.with_pet.dto.applicant.ApplicantCreateResponse;
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
public class ApplicantCreateService {
    private final ValidateCollection valid;


    /**
     * 유저의 펫시터 지원자 등록
     **/
    @Transactional
    public ApplicantCreateResponse registerApplicant(ApplicantCreateRequest applicantCreateRequest,
            String email) {

        User findUser = valid.userValidationByEmail(email);

        //유저 지원 유효성 체크 (중복지원, 지원 제한 횟수)
        applicantValidCheck(findUser);

        findUser.updateApplicantCount();

        //유저 지원 상태 wait으로 변경
        findUser.updateApplicantStatus(ApplicantStatus.WAIT);
        findUser.updateUserRole(UserRole.ROLE_APPLICANT);

        findUser.registerApplicantInfo(applicantCreateRequest);

        return ApplicantCreateResponse.of(findUser);
    }

    /**
     * 지원시 유저의 유효성 체크
     **/
    private void applicantValidCheck(User user) {

        // 유저 펫시터 지원 중복 체크
        // 유저의 role이 지원자이고 , 지원상태가 WAIT일때는 중복 지원 금지
        if (user.getRole().equals(UserRole.ROLE_APPLICANT) && user.getApplicantStatus()
                .equals(ApplicantStatus.WAIT)) {
            throw new AppException(ErrorCode.DUPLICATED_APPLICATION,
                    ErrorCode.DUPLICATED_APPLICATION.getMessage());
        }

        //지원 횟수가 3번을 초과했다면 지원 x
        if (user.getApplicantCount().equals(4)) {
            throw new AppException(ErrorCode.TO_MANY_APPLICATE,
                    ErrorCode.TO_MANY_APPLICATE.getMessage());
        }

    }
}
