package com.ajou_nice.with_pet.applicant;

import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ajou_nice.with_pet.CommonApiTest;
import com.ajou_nice.with_pet.controller.applicant.ApplicantController;
import com.ajou_nice.with_pet.domain.dto.petsitterapplicant.ApplicantInfoRequest;
import com.ajou_nice.with_pet.service.applicant.ApplicantService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
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

@WebMvcTest(ApplicantController.class)
@MockBean(JpaMetamodelMappingContext.class)
public class ApplicantControllerTest extends CommonApiTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    ApplicantService applicantService;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("지원자 등록 테스트")
    @WithMockUser(username = "test", roles = "USER")
    void registerApplicant() throws Exception {
        //given
        ApplicantInfoRequest request = ApplicantInfoRequest.builder()
                .applicant_identification("000000-1000000")
                .applicant_motivate("동기")
                .applicant_is_smoking(false)
                .applicant_license_img("image")
                .applicant_care_experience("반려동물 케어 경험")
                .applicant_petsitter_career("커리어")
                .applicant_animal_career("반려동물 관련 커리어")
                .applicant_having_with_pet(true)
                .build();
        //when
        mockMvc.perform(post("/api/v2/applicants")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andDo(print())
                .andExpect(status().isOk());
        //then
        ArgumentCaptor<ApplicantInfoRequest> requestCaptor = ArgumentCaptor.forClass(ApplicantInfoRequest.class);
        ArgumentCaptor<String> userNameCaptor = ArgumentCaptor.forClass(String.class);
        verify(applicantService).registerApplicant(requestCaptor.capture(),userNameCaptor.capture());
        ApplicantInfoRequest requestValue = requestCaptor.getValue();
        String userNameValue = userNameCaptor.getValue();

        Assertions.assertEquals(requestValue.getApplicant_motivate(),request.getApplicant_motivate());
        Assertions.assertEquals(requestValue.getApplicant_identification(),request.getApplicant_identification());
        Assertions.assertEquals("test",userNameValue);

    }
}
