package com.ajou_nice.with_pet.service.applicant;


import com.ajou_nice.with_pet.dto.applicant.ApplicantCreateRequest.ApplicantModifyRequest;
import com.ajou_nice.with_pet.dto.applicant.ApplicantCreateResponse;
import com.ajou_nice.with_pet.domain.entity.User;
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


    private final ValidateCollection valid;

    // == 유저의 지원 정보 상세 확인 == //
    //리팩토링 완
    public ApplicantCreateResponse showApplicateInfo(String userId) {
        User findUser = valid.userValidation(userId);
        //지원자가 아닐 경우 오류 출력
        if (!findUser.getRole().equals(UserRole.ROLE_APPLICANT)) {
            throw new AppException(ErrorCode.APPLICANT_NOT_FOUND,
                    ErrorCode.APPLICANT_NOT_FOUND.getMessage());
        }

        return ApplicantCreateResponse.of(findUser);
    }

    // == 유저의 펫시터 지원서류 수정 == //
    //리팩토링 완
    @Transactional
    public ApplicantCreateResponse modifyApplicateInfo(String userId,
            ApplicantModifyRequest applicantModifyRequest) {
        User findUser = valid.userValidation(userId);

        // 지원 정보 수정
        findUser.updateApplicantInfo(applicantModifyRequest);

        return ApplicantCreateResponse.of(findUser);
    }
}