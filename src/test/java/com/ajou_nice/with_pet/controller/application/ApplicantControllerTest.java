package com.ajou_nice.with_pet.controller.application;

import com.ajou_nice.with_pet.CommonApiTest;
import com.ajou_nice.with_pet.controller.applicant.ApplicantController;
import com.ajou_nice.with_pet.domain.entity.User;
import com.ajou_nice.with_pet.domain.entity.embedded.Address;
import com.ajou_nice.with_pet.dto.applicant.PetsitterApplicationRequest;
import com.ajou_nice.with_pet.dto.applicant.PetsitterApplicationResponse;
import com.ajou_nice.with_pet.enums.ApplicantStatus;
import com.ajou_nice.with_pet.enums.Gender;
import com.ajou_nice.with_pet.enums.UserRole;
import com.ajou_nice.with_pet.fixture.PetsitterApplicationDtoFixture;
import com.ajou_nice.with_pet.fixture.entity.AddressFixture;
import com.ajou_nice.with_pet.fixture.entity.UserFixture;
import com.ajou_nice.with_pet.service.applicant.ApplicationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ApplicantController.class)
@MockBean(JpaMetamodelMappingContext.class)
public class ApplicantControllerTest extends CommonApiTest {

    final String APPLICATIONS_POST_URI = "/api/v2/applications";

    @Autowired
    MockMvc mockMvc;

    @MockBean
    ApplicationService applicationService;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    Validator validator;

    PetsitterApplicationRequest petsitterApplicationRequest;
    PetsitterApplicationResponse petsitterApplicationResponse;
    User user;

    @BeforeEach
    void setUp() {
        Address address = AddressFixture.createAddress("16499", "경기도 수원시 영통구 월드컵로 206 (원천동, 아주대학교)", "팔달관");

        user = UserFixture.createUserByApplication(1L, "user", "email@email.com", "password158!", UserRole.ROLE_APPLICANT, "https://ajounciewithpet.s3.ap-northeast-2.amazonaws.com/default-user.png", "010-0000-0000",
                address, LocalDate.of(2001, 1, 1), false, Gender.FEMALE, true, "animalCareer is very good", "I want to be petsitter", "licenseImg", ApplicantStatus.WAIT, 1);
    }

    @Test
    @DisplayName("펫시터 지원 테스트 성공")
    @WithMockUser(username = "test", roles = "USER")
    void applyPetsitter_success() throws Exception {
        //given
        petsitterApplicationRequest = PetsitterApplicationDtoFixture.createPetsitterApplicationRequest(user.getBirth(), user.getIsSmoking(), user.getGender(), user.getHavingWithPet(), user.getAnimalCareer(), user.getMotivation(), user.getLicenseImg());
        petsitterApplicationResponse = PetsitterApplicationDtoFixture.createPetsitterApplicationResponse(user);

        when(applicationService.applyPetsitter(any(), any())).thenReturn(petsitterApplicationResponse);

        mockMvc.perform(post(APPLICATIONS_POST_URI)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(petsitterApplicationRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.userId").value(petsitterApplicationResponse.getUserId()))
                .andExpect(jsonPath("$.result.name").value(petsitterApplicationResponse.getName()))
                .andExpect(jsonPath("$.result.email").value(petsitterApplicationResponse.getEmail()))
                .andExpect(jsonPath("$.result.profileImg").value(petsitterApplicationResponse.getProfileImg()))
                .andExpect(jsonPath("$.result.phone").value(petsitterApplicationResponse.getPhone()))
                .andExpect(jsonPath("$.result.status").value(petsitterApplicationResponse.getStatus()))
                .andExpect(jsonPath("$.result.birth").value(petsitterApplicationResponse.getBirth()))
                .andExpect(jsonPath("$.result.isSmoking").value(petsitterApplicationResponse.getIsSmoking()))
                .andExpect(jsonPath("$.result.gender").value(petsitterApplicationResponse.getGender()))
                .andExpect(jsonPath("$.result.havingWithPet").value(petsitterApplicationResponse.getHavingWithPet()))
                .andExpect(jsonPath("$.result.animalCareer").value(petsitterApplicationResponse.getAnimalCareer()))
                .andExpect(jsonPath("$.result.motivation").value(petsitterApplicationResponse.getMotivation()))
                .andExpect(jsonPath("$.result.licenseImg").value(petsitterApplicationResponse.getLicenseImg()))
                .andExpect(jsonPath("$.result.streetAdr").value(petsitterApplicationResponse.getStreetAdr()));
    }

    @Test
    @DisplayName("펫시터 지원 유효성 검사")
    void valid_applyPetsitter() {
        //given
        petsitterApplicationRequest = PetsitterApplicationDtoFixture.createPetsitterApplicationRequest(null, null, null, null, "", "", null);
        //when
        Set<ConstraintViolation<PetsitterApplicationRequest>> validate = validator.validate(petsitterApplicationRequest);

        //then
        Iterator<ConstraintViolation<PetsitterApplicationRequest>> iterator = validate.iterator();
        List<String> messages = new ArrayList<>();
        while (iterator.hasNext()) {
            ConstraintViolation<PetsitterApplicationRequest> next = iterator.next();
            messages.add(next.getMessage());
        }

        Assertions.assertThat(messages)
                .contains("올바른 형식의 생년월일을 입력해주세요.", "흡연여부 문항을 체크해주세요.",
                        "성별을 입력해주세요.", "반려 경험 여부 문항을 체크해주세요.", "자격증 업로드를 해주세요.");
    }
}
