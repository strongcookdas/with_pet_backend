package com.ajou_nice.with_pet.admin.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ajou_nice.with_pet.admin.controller.AdminController;
import com.ajou_nice.with_pet.admin.model.AdminApplicantRequest;
import com.ajou_nice.with_pet.admin.service.AdminService;
import com.fasterxml.jackson.databind.ObjectMapper;
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

@WebMvcTest(AdminController.class)
@MockBean(JpaMetamodelMappingContext.class)
public class AdminControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    AdminService adminService;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("관리자의 펫시터 지원자 수락")
    @WithMockUser(username = "admin", roles = "ADMIN")
    void approvalApplicant() throws Exception {
        //given
        AdminApplicantRequest request = AdminApplicantRequest.builder()
                .userId(1L)
                .build();
        //when
        mockMvc.perform(post("/api/v1/admin/accept-petsitter")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andDo(print())
                .andExpect(status().isOk());
        //then
        ArgumentCaptor<String> userNameCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<AdminApplicantRequest> requestCaptor = ArgumentCaptor.forClass(
                AdminApplicantRequest.class);
        verify(adminService).createPetsitter(userNameCaptor.capture(), requestCaptor.capture());
        AdminApplicantRequest requestValue = requestCaptor.getValue();
        String userNameValue = userNameCaptor.getValue();

        assertEquals(request.getUserId(), requestValue.getUserId());
        assertEquals("admin", userNameValue);
    }

    @Test
    @DisplayName("관리자의 필수 서비스 추가")
    @WithMockUser(username = "admin", roles = "ADMIN")
    void addCriticalServiceApiTest() throws Exception {
        //given
        AdminApplicantRequest request = AdminApplicantRequest.builder()
                .userId(1L)
                .build();
        //when
        mockMvc.perform(post("/api/v1/admin/accept-petsitter")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andDo(print())
                .andExpect(status().isOk());
        //then
        ArgumentCaptor<String> userNameCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<AdminApplicantRequest> requestCaptor = ArgumentCaptor.forClass(
                AdminApplicantRequest.class);
        verify(adminService).createPetsitter(userNameCaptor.capture(), requestCaptor.capture());
        AdminApplicantRequest requestValue = requestCaptor.getValue();
        String userNameValue = userNameCaptor.getValue();

        assertEquals(request.getUserId(), requestValue.getUserId());
        assertEquals("admin", userNameValue);
    }
}
