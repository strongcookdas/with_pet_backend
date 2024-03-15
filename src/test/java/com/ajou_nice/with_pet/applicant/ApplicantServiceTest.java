package com.ajou_nice.with_pet.applicant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.ajou_nice.with_pet.dto.applicant.ApplicantCreateRequest;
import com.ajou_nice.with_pet.dto.applicant.ApplicantCreateResponse;
import com.ajou_nice.with_pet.domain.entity.User;
import com.ajou_nice.with_pet.domain.entity.embedded.Address;
import com.ajou_nice.with_pet.enums.ApplicantStatus;
import com.ajou_nice.with_pet.enums.Gender;
import com.ajou_nice.with_pet.enums.UserRole;
import com.ajou_nice.with_pet.exception.AppException;
import com.ajou_nice.with_pet.exception.ErrorCode;
import com.ajou_nice.with_pet.service.ValidateCollection;
import com.ajou_nice.with_pet.service.applicant.ApplicantCreateService;
import java.time.LocalDate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ApplicantServiceTest {

    ApplicantCreateService service;
    ValidateCollection validateCollection = mock(ValidateCollection.class);

    User user;
    Address address;
    ApplicantCreateRequest request;

    @BeforeEach
    public void setUp() {
        address = Address.builder()
                .zipcode("111111")
                .streetAdr("월드컵로")
                .detailAdr("팔달관")
                .build();

        service = new ApplicantCreateService(validateCollection);
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
        request = ApplicantCreateRequest.builder()
                .birth(LocalDate.now())
                .isSmoking(false)
                .gender(Gender.FEMALE)
                .havingWithPet(true)
                .animalCareer("커리어")
                .motivation("동기")
                .licenseImg("이미지")
                .build();
        //when
        when(validateCollection.userValidationByEmail(user.getEmail())).thenReturn(user);
        ApplicantCreateResponse result = service.registerApplicant(request, user.getEmail());
        //then
        Assertions.assertEquals(user.getId(),result.getUserId());
        Assertions.assertEquals(request.getMotivation(), result.getMotivation());
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
        request = ApplicantCreateRequest.builder()
                .birth(LocalDate.now())
                .isSmoking(false)
                .gender(Gender.FEMALE)
                .havingWithPet(true)
                .animalCareer("커리어")
                .motivation("동기")
                .licenseImg("이미지")
                .build();
        //when
        when(validateCollection.userValidationByEmail(user.getEmail())).thenReturn(user);
        AppException exception = assertThrows(AppException.class,
                () -> service.registerApplicant(request, user.getEmail()));
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
        request = ApplicantCreateRequest.builder()
                .birth(LocalDate.now())
                .isSmoking(false)
                .gender(Gender.FEMALE)
                .havingWithPet(true)
                .animalCareer("커리어")
                .motivation("동기")
                .licenseImg("이미지")
                .build();
        //when
        when(validateCollection.userValidationByEmail(user.getEmail())).thenReturn(user);
        AppException exception = assertThrows(AppException.class,
                () -> service.registerApplicant(request, user.getEmail()));
        //then
        assertEquals(ErrorCode.TO_MANY_APPLICATE, exception.getErrorCode());
    }
}
