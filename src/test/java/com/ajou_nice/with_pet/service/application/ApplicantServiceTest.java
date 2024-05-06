package com.ajou_nice.with_pet.service.application;

import com.ajou_nice.with_pet.domain.entity.User;
import com.ajou_nice.with_pet.domain.entity.embedded.Address;
import com.ajou_nice.with_pet.applicant.model.dto.PetsitterApplicationRequest;
import com.ajou_nice.with_pet.applicant.model.dto.PetSitterApplicationResponse;
import com.ajou_nice.with_pet.enums.ApplicantStatus;
import com.ajou_nice.with_pet.enums.Gender;
import com.ajou_nice.with_pet.enums.UserRole;
import com.ajou_nice.with_pet.exception.AppException;
import com.ajou_nice.with_pet.exception.ErrorCode;
import com.ajou_nice.with_pet.fixture.PetsitterApplicationDtoFixture;
import com.ajou_nice.with_pet.fixture.entity.AddressFixture;
import com.ajou_nice.with_pet.fixture.entity.UserFixture;
import com.ajou_nice.with_pet.service.ValidateCollection;
import com.ajou_nice.with_pet.service.applicant.ApplicationService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ApplicantServiceTest {
    ApplicationService applicationService;
    ValidateCollection validateCollection = mock(ValidateCollection.class);

    User user;
    Address address;
    PetsitterApplicationRequest petsitterApplicationRequest;

    @BeforeEach
    public void setUp() {
        address = AddressFixture.createAddress("16499", "경기도 수원시 영통구 월드컵로 206 (원천동, 아주대학교)", "팔달관");
        applicationService = new ApplicationService(validateCollection);
    }

    @Test
    @DisplayName("펫시터 지원 성공")
    void registerApplicant() {
        //given
        user = UserFixture.createUser(1L,"user","email@email.com","password258!",UserRole.ROLE_USER,"https://ajounciewithpet.s3.ap-northeast-2.amazonaws.com/default-user.png", "010-0000-0000");
        petsitterApplicationRequest = PetsitterApplicationDtoFixture.createPetsitterApplicationRequest(LocalDate.of(2001,1,1), false, Gender.FEMALE, true, "animalCareer", "motivation", "licenseImg");
        //when
        when(validateCollection.userValidationByEmail(user.getEmail())).thenReturn(user);
        PetSitterApplicationResponse petsitterApplicationResponse = applicationService.applyPetsitter(petsitterApplicationRequest, user.getEmail());
        //then
        Assertions.assertEquals(user.getId(),petsitterApplicationResponse.getUserId());
        Assertions.assertEquals(user.getName(),petsitterApplicationResponse.getName());
        Assertions.assertEquals(user.getEmail(),petsitterApplicationResponse.getEmail());
        Assertions.assertEquals(user.getProfileImg(),petsitterApplicationResponse.getProfileImg());
        Assertions.assertEquals(user.getPhone(),petsitterApplicationResponse.getPhone());
        Assertions.assertEquals(user.getApplicantStatus().name(),petsitterApplicationResponse.getStatus());
        Assertions.assertEquals(petsitterApplicationRequest.getMotivation(), petsitterApplicationResponse.getMotivation());
    }

    @Test
    @DisplayName("펫시터 지원 실패 : 지원자이고 지원 상태가 WAIT일 때")
    void registerApplicant_fail_applicant_status_WAIT() {
        //given
        user = UserFixture.createUserWithApplicantStatus(1L,"user","email@email.com","password258!",UserRole.ROLE_APPLICANT,"https://ajounciewithpet.s3.ap-northeast-2.amazonaws.com/default-user.png", "010-0000-0000",1,ApplicantStatus.WAIT,address);
        petsitterApplicationRequest = PetsitterApplicationDtoFixture.createPetsitterApplicationRequest(LocalDate.of(2001,1,1), false, Gender.FEMALE, true, "animalCareer", "motivation", "licenseImg");
        //when
        when(validateCollection.userValidationByEmail(user.getEmail())).thenReturn(user);
        AppException exception = assertThrows(AppException.class,
                () ->applicationService.applyPetsitter(petsitterApplicationRequest, user.getEmail()));
        //then
        assertEquals(ErrorCode.DUPLICATED_APPLICATION, exception.getErrorCode());
    }

    @Test
    @DisplayName("펫시터 지원 실패 : 지원 횟수가 3회 초과했을 때")
    void registerApplicant_fail_max_application_count() {
        //given
        user = UserFixture.createUserWithApplicantStatus(1L,"user","email@email.com","password258!",UserRole.ROLE_USER,"https://ajounciewithpet.s3.ap-northeast-2.amazonaws.com/default-user.png", "010-0000-0000",3,ApplicantStatus.WAIT,address);
        petsitterApplicationRequest = PetsitterApplicationDtoFixture.createPetsitterApplicationRequest(LocalDate.of(2001,1,1), false, Gender.FEMALE, true, "animalCareer", "motivation", "licenseImg");
        //when
        when(validateCollection.userValidationByEmail(user.getEmail())).thenReturn(user);
        AppException exception = assertThrows(AppException.class,
                () -> applicationService.applyPetsitter(petsitterApplicationRequest, user.getEmail()));
        //then
        assertEquals(ErrorCode.TO_MANY_APPLICATE, exception.getErrorCode());
    }
}
