package com.ajou_nice.with_pet.admin.controller;

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;

@WebMvcTest(AdminController.class)
@MockBean(JpaMetamodelMappingContext.class)
public class AdminControllerTest {

//    @Autowired
//    MockMvc mockMvc;
//
//    @MockBean
//    AdminService adminService;
//
//    @Autowired
//    ObjectMapper objectMapper;
//
//    @Test
//    @DisplayName("관리자의 펫시터 지원자 수락")
//    @WithMockUser(username = "admin", roles = "ADMIN")
//    void approvalApplicant() throws Exception {
//        //given
//        AdminApplicantRequest request = AdminApplicantRequest.builder()
//                .userId(1L)
//                .build();
//        //when
//        mockMvc.perform(post("/api/v1/admin/accept-petsitter")
//                        .with(csrf())
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsBytes(request)))
//                .andDo(print())
//                .andExpect(status().isOk());
//        //then
//        ArgumentCaptor<String> userNameCaptor = ArgumentCaptor.forClass(String.class);
//        ArgumentCaptor<AdminApplicantRequest> requestCaptor = ArgumentCaptor.forClass(
//                AdminApplicantRequest.class);
//        verify(adminService).acceptApplicant(userNameCaptor.capture(), requestCaptor.capture());
//        AdminApplicantRequest requestValue = requestCaptor.getValue();
//        String userNameValue = userNameCaptor.getValue();
//
//        assertEquals(request.getUserId(), requestValue.getUserId());
//        assertEquals("admin", userNameValue);
//    }
//
//    @Test
//    @DisplayName("관리자의 필수 서비스 추가")
//    @WithMockUser(username = "admin", roles = "ADMIN")
//    void addCriticalServiceApiTest() throws Exception {
//        //given
//        AdminApplicantRequest request = AdminApplicantRequest.builder()
//                .userId(1L)
//                .build();
//        //when
//        mockMvc.perform(post("/api/v1/admin/accept-petsitter")
//                        .with(csrf())
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsBytes(request)))
//                .andDo(print())
//                .andExpect(status().isOk());
//        //then
//        ArgumentCaptor<String> userNameCaptor = ArgumentCaptor.forClass(String.class);
//        ArgumentCaptor<AdminApplicantRequest> requestCaptor = ArgumentCaptor.forClass(
//                AdminApplicantRequest.class);
//        verify(adminService).acceptApplicant(userNameCaptor.capture(), requestCaptor.capture());
//        AdminApplicantRequest requestValue = requestCaptor.getValue();
//        String userNameValue = userNameCaptor.getValue();
//
//        assertEquals(request.getUserId(), requestValue.getUserId());
//        assertEquals("admin", userNameValue);
//    }
}
