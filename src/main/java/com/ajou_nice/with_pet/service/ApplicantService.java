package com.ajou_nice.with_pet.service;


import com.ajou_nice.with_pet.domain.dto.petsitterapplicant.ApplicantInfoRequest;
import com.ajou_nice.with_pet.domain.dto.petsitterapplicant.ApplicantInfoRequest.ApplicantModifyRequest;
import com.ajou_nice.with_pet.domain.dto.petsitterapplicant.ApplicantInfoResponse;
import com.ajou_nice.with_pet.domain.entity.User;
import com.ajou_nice.with_pet.enums.ApplicantStatus;
import com.ajou_nice.with_pet.enums.UserRole;
import com.ajou_nice.with_pet.exception.AppException;
import com.ajou_nice.with_pet.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ApplicantService {

    private final ValidateCollection valid;


    /**
     * 유저의 펫시터 지원자 등록
     **/
    @Transactional
    public ApplicantInfoResponse registerApplicant(ApplicantInfoRequest applicantInfoRequest,
            String email) {

        User findUser = valid.userValidationByEmail(email);

        //유저 지원 유효성 체크 (중복지원, 지원 제한 횟수)
        applicantValidCheck(findUser);

        findUser.updateApplicateCount();

        //유저 지원 상태 wait으로 변경
        findUser.updateApplicantStatus(ApplicantStatus.WAIT);
        findUser.updateUserRole(UserRole.ROLE_APPLICANT);

        findUser.registerApplicantInfo(applicantInfoRequest);

        return ApplicantInfoResponse.ofAll(findUser);
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

    // == 유저의 지원 정보 상세 확인 == //
    //리팩토링 완
    public ApplicantInfoResponse showApplicateInfo(String userId) {
        User findUser = valid.userValidation(userId);
        //지원자가 아닐 경우 오류 출력
        if (!findUser.getRole().equals(UserRole.ROLE_APPLICANT)) {
            throw new AppException(ErrorCode.APPLICANT_NOT_FOUND,
                    ErrorCode.APPLICANT_NOT_FOUND.getMessage());
        }

        return ApplicantInfoResponse.ofAll(findUser);
    }

    // == 유저의 펫시터 지원서류 수정 == //
    //리팩토링 완
    @Transactional
    public ApplicantInfoResponse modifyApplicateInfo(String userId,
            ApplicantModifyRequest applicantModifyRequest) {
        User findUser = valid.userValidation(userId);

        // 지원 정보 수정
        findUser.updateApplicantInfo(applicantModifyRequest);

        return ApplicantInfoResponse.ofAll(findUser);
    }
}