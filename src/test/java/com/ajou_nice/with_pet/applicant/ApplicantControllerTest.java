package com.ajou_nice.with_pet.applicant;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ajou_nice.with_pet.CommonApiTest;
import com.ajou_nice.with_pet.controller.applicant.ApplicantCreateAPI;
import com.ajou_nice.with_pet.dto.applicant.ApplicantCreateRequest;
import com.ajou_nice.with_pet.dto.applicant.ApplicantCreateResponse;
import com.ajou_nice.with_pet.enums.ApplicantStatus;
import com.ajou_nice.with_pet.enums.Gender;
import com.ajou_nice.with_pet.service.applicant.ApplicantCreateService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
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

@WebMvcTest(ApplicantCreateAPI.class)
@MockBean(JpaMetamodelMappingContext.class)
public class ApplicantControllerTest extends CommonApiTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    ApplicantCreateService service;

    @Autowired
    ObjectMapper objectMapper;

    ApplicantCreateRequest request;
    ApplicantCreateResponse response;

    @BeforeEach
    void setUp(){
        request = ApplicantCreateRequest.builder()
                .birth(LocalDate.now())
                .isSmoking(false)
                .gender(Gender.FEMALE)
                .havingWithPet(true)
                .animalCareer("커리어")
                .motivation("동기")
                .licenseImg("이미지")
                .build();

        response = ApplicantCreateResponse.builder()
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

        when(service.registerApplicant(any(),any())).thenReturn(response);

        mockMvc.perform(post("/api/v2/applicants")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
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


        ArgumentCaptor<ApplicantCreateRequest> requestCaptor = ArgumentCaptor.forClass(
                ApplicantCreateRequest.class);
        ArgumentCaptor<String> userNameCaptor = ArgumentCaptor.forClass(String.class);
        verify(service).registerApplicant(requestCaptor.capture(),userNameCaptor.capture());
        ApplicantCreateRequest requestValue = requestCaptor.getValue();
        String userNameValue = userNameCaptor.getValue();

        Assertions.assertEquals(requestValue.getMotivation(),request.getMotivation());
        Assertions.assertEquals("test",userNameValue);

    }
}
