package com.ajou_nice.with_pet.applicant;

import com.ajou_nice.with_pet.CommonApiTest;
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
import com.ajou_nice.with_pet.service.applicant.ApplicantService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ApplicantControllerTest.class)
@MockBean(JpaMetamodelMappingContext.class)
public class ApplicantControllerTest extends CommonApiTest {

    final String APPLICANT_APPLY_POST_URI = "/api/v2//applications";

    @Autowired
    MockMvc mockMvc;

    @MockBean
    ApplicantService applicantService;

    @Autowired
    ObjectMapper objectMapper;

    PetsitterApplicationRequest petsitterApplicationRequest;
    PetsitterApplicationResponse petsitterApplicationResponse;
    User user;

    @BeforeEach
    void setUp() {
        Address address = AddressFixture.createAddress("16499", "경기도 수원시 영통구 월드컵로 206 (원천동, 아주대학교)", "팔달관");

        user = UserFixture.createUserByApplication(1L, "user", "email@email.com", "password158!", UserRole.ROLE_APPLICANT, "https://ajounciewithpet.s3.ap-northeast-2.amazonaws.com/default-user.png", "010-0000-0000",
                address, LocalDate.of(2001, 1, 1), false, Gender.FEMALE, true, "animalCareer is very good", "I want to be petsitter", "licenseImg", ApplicantStatus.WAIT, 1);


        petsitterApplicationRequest = PetsitterApplicationRequest.builder()
                .birth(LocalDate.now())
                .isSmoking(false)
                .gender(Gender.FEMALE)
                .havingWithPet(true)
                .animalCareer("커리어")
                .motivation("동기")
                .licenseImg("이미지")
                .build();

        petsitterApplicationResponse = PetsitterApplicationResponse.builder()
                .userId(1L)
                .name("user1")
                .email("email@email.com")
                .profileImg("이미지")
                .phone("010-0000-0000")
                .status(ApplicantStatus.WAIT.name())
                .birth(LocalDate.now())
                .isSmoking(true)
                .gender(Gender.FEMALE)
                .havingWithPet(true)
                .animalCareer("커리어")
                .motivation("동기")
                .licenseImg("이미지")
                .streetAdr("원천동")
                .build();
    }

    @Test
    @DisplayName("지원자 등록 테스트")
    @WithMockUser(username = "test", roles = "USER")
    void registerApplicant() throws Exception {
        //given
        petsitterApplicationRequest = PetsitterApplicationDtoFixture.createPetsitterApplicationRequest(LocalDate.of(2001, 1, 1), false, Gender.FEMALE, true, "animalCareer is very good", "I want to be petsitter", "licenseImg");
        when(applicantService.registerApplicant(any(), any())).thenReturn(petsitterApplicationResponse);

        mockMvc.perform(post("/api/v2/applicants")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(petsitterApplicationRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.userId").exists())
                .andExpect(jsonPath("$.result.name").exists())
                .andExpect(jsonPath("$.result.email").exists())
                .andExpect(jsonPath("$.result.profileImg").exists())
                .andExpect(jsonPath("$.result.phone").exists())
                .andExpect(jsonPath("$.result.status").exists())
                .andExpect(jsonPath("$.result.birth").exists())
                .andExpect(jsonPath("$.result.isSmoking").exists())
                .andExpect(jsonPath("$.result.gender").exists())
                .andExpect(jsonPath("$.result.havingWithPet").exists())
                .andExpect(jsonPath("$.result.animalCareer").exists())
                .andExpect(jsonPath("$.result.motivation").exists())
                .andExpect(jsonPath("$.result.licenseImg").exists())
                .andExpect(jsonPath("$.result.streetAdr").exists());


        ArgumentCaptor<PetsitterApplicationRequest> requestCaptor = ArgumentCaptor.forClass(
                PetsitterApplicationRequest.class);
        ArgumentCaptor<String> userNameCaptor = ArgumentCaptor.forClass(String.class);
        verify(applicantService).registerApplicant(requestCaptor.capture(), userNameCaptor.capture());
        PetsitterApplicationRequest requestValue = requestCaptor.getValue();
        String userNameValue = userNameCaptor.getValue();

        Assertions.assertEquals(requestValue.getMotivation(), petsitterApplicationRequest.getMotivation());
        Assertions.assertEquals("test", userNameValue);

    }
}
