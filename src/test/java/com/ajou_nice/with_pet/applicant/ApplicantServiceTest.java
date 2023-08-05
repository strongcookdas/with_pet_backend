package com.ajou_nice.with_pet.applicant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.ajou_nice.with_pet.domain.dto.petsitterapplicant.ApplicantInfoRequest;
import com.ajou_nice.with_pet.domain.dto.petsitterapplicant.ApplicantInfoResponse;
import com.ajou_nice.with_pet.domain.entity.User;
import com.ajou_nice.with_pet.domain.entity.embedded.Address;
import com.ajou_nice.with_pet.enums.ApplicantStatus;
import com.ajou_nice.with_pet.enums.UserRole;
import com.ajou_nice.with_pet.exception.AppException;
import com.ajou_nice.with_pet.exception.ErrorCode;
import com.ajou_nice.with_pet.service.applicant.ApplicantService;
import com.ajou_nice.with_pet.service.ValidateCollection;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ApplicantServiceTest {

    ApplicantService applicantService;
    ValidateCollection validateCollection = mock(ValidateCollection.class);

    User user;
    Address address;
    ApplicantInfoRequest request;
    ApplicantInfoResponse response;

    @BeforeEach
    public void setUp() {
        address = Address.builder()
                .zipcode("111111")
                .streetAdr("월드컵로")
                .detailAdr("팔달관")
                .build();

        applicantService = new ApplicantService(validateCollection);
    }

    @Test
    @DisplayName("펫시터 지원 성공")
    void registerApplicant() {
        //given
        user = User.builder()
                .userId(1L)
                .name("user1")
                .email("email@email.com")
                .password("password")
                .role(UserRole.ROLE_USER)
                .profileImg("image")
                .phone("010-0000-0000")
                .applicantCount(0)
                .address(address)
                .build();
        request = ApplicantInfoRequest.builder()
                .applicant_identification("000000-1000000")
                .applicant_motivate("동기")
                .applicant_is_smoking(false)
                .applicant_license_img("image")
                .applicant_care_experience("반려동물 케어 경험")
                .applicant_petsitter_career("커리어")
                .applicant_animal_career("반려동물 관련 커리어")
                .applicant_having_with_pet(true)
                .build();
        response = ApplicantInfoResponse.builder()
                .applicant_identification(request.getApplicant_identification())
                .applicant_license_img(request.getApplicant_license_img())
                .applicant_is_smoking(request.getApplicant_is_smoking())
                .applicant_care_experience(request.getApplicant_care_experience())
                .applicant_having_with_pet(request.getApplicant_having_with_pet())
                .applicant_animal_career(request.getApplicant_animal_career())
                .applicant_motivate(request.getApplicant_motivate())
                .applicant_streetAdr(user.getAddress().getStreetAdr())
                .build();
        //when
        when(validateCollection.userValidationByEmail(user.getEmail())).thenReturn(user);
        ApplicantInfoResponse result = applicantService.registerApplicant(request, user.getEmail());
        //then
        Assertions.assertEquals(response.getApplicant_identification(),result.getApplicant_identification());
        Assertions.assertEquals(response.getApplicant_motivate(), result.getApplicant_motivate());
    }

    @Test
    @DisplayName("펫시터 지원 실패1 : 지원자이고 지원 상태가 WAIT일 때")
    void registerApplicant_fail1() {
        //given
        user = User.builder()
                .userId(1L)
                .name("user1")
                .email("email@email.com")
                .password("password")
                .role(UserRole.ROLE_APPLICANT)
                .profileImg("image")
                .phone("010-0000-0000")
                .applicantCount(0)
                .applicantStatus(ApplicantStatus.WAIT)
                .address(address)
                .build();
        request = ApplicantInfoRequest.builder()
                .applicant_identification("000000-1000000")
                .applicant_motivate("동기")
                .applicant_is_smoking(false)
                .applicant_license_img("image")
                .applicant_care_experience("반려동물 케어 경험")
                .applicant_petsitter_career("커리어")
                .applicant_animal_career("반려동물 관련 커리어")
                .applicant_having_with_pet(true)
                .build();
        response = ApplicantInfoResponse.builder()
                .applicant_identification(request.getApplicant_identification())
                .applicant_license_img(request.getApplicant_license_img())
                .applicant_is_smoking(request.getApplicant_is_smoking())
                .applicant_care_experience(request.getApplicant_care_experience())
                .applicant_having_with_pet(request.getApplicant_having_with_pet())
                .applicant_animal_career(request.getApplicant_animal_career())
                .applicant_motivate(request.getApplicant_motivate())
                .applicant_streetAdr(user.getAddress().getStreetAdr())
                .build();
        //when
        when(validateCollection.userValidationByEmail(user.getEmail())).thenReturn(user);
        AppException exception = assertThrows(AppException.class,
                () -> applicantService.registerApplicant(request, user.getEmail()));
        //then
        assertEquals(ErrorCode.DUPLICATED_APPLICATION, exception.getErrorCode());
    }

    @Test
    @DisplayName("펫시터 지원 실패2 : 지원 횟수가 3회 초과했을 때")
    void registerApplicant_fail2() {
        //given
        user = User.builder()
                .userId(1L)
                .name("user1")
                .email("email@email.com")
                .password("password")
                .role(UserRole.ROLE_USER)
                .profileImg("image")
                .phone("010-0000-0000")
                .applicantCount(4)
                .address(address)
                .build();
        request = ApplicantInfoRequest.builder()
                .applicant_identification("000000-1000000")
                .applicant_motivate("동기")
                .applicant_is_smoking(false)
                .applicant_license_img("image")
                .applicant_care_experience("반려동물 케어 경험")
                .applicant_petsitter_career("커리어")
                .applicant_animal_career("반려동물 관련 커리어")
                .applicant_having_with_pet(true)
                .build();
        response = ApplicantInfoResponse.builder()
                .applicant_identification(request.getApplicant_identification())
                .applicant_license_img(request.getApplicant_license_img())
                .applicant_is_smoking(request.getApplicant_is_smoking())
                .applicant_care_experience(request.getApplicant_care_experience())
                .applicant_having_with_pet(request.getApplicant_having_with_pet())
                .applicant_animal_career(request.getApplicant_animal_career())
                .applicant_motivate(request.getApplicant_motivate())
                .applicant_streetAdr(user.getAddress().getStreetAdr())
                .build();
        //when
        when(validateCollection.userValidationByEmail(user.getEmail())).thenReturn(user);
        AppException exception = assertThrows(AppException.class,
                () -> applicantService.registerApplicant(request, user.getEmail()));
        //then
        assertEquals(ErrorCode.TO_MANY_APPLICATE, exception.getErrorCode());
    }
}
