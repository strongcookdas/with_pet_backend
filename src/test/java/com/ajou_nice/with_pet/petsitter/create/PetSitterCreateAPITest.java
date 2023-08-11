package com.ajou_nice.with_pet.petsitter.create;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ajou_nice.with_pet.CommonApiTest;
import com.ajou_nice.with_pet.controller.petsitter.PetSitterCreateAPI;
import com.ajou_nice.with_pet.dto.petsitter.PetSitterCreateRequest;
import com.ajou_nice.with_pet.dto.petsitter.PetSitterCreateResponse;
import com.ajou_nice.with_pet.fixture.Fixture.Request;
import com.ajou_nice.with_pet.fixture.Fixture.Response;
import com.ajou_nice.with_pet.service.petsitter.PetSitterCreateService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
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

@WebMvcTest(PetSitterCreateAPI.class)
@MockBean(JpaMetamodelMappingContext.class)
public class PetSitterCreateAPITest extends CommonApiTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    PetSitterCreateService service;

    @Autowired
    ObjectMapper objectMapper;

    Request requestFixture = new Request();
    Response responseFixture = new Response();
    PetSitterCreateRequest request;
    PetSitterCreateResponse response;

    @BeforeEach
    void setUp() {
        request = PetSitterCreateRequest.builder()
                .petSitterHouses(Arrays.asList(requestFixture.getHouseRequest1(),
                        requestFixture.getHouseRequest2()))
                .petSitterHashTags(Arrays.asList(requestFixture.getHashTagRequest1()))
                .petSitterIntroduction("소개글")
                .petSitterServices(Arrays.asList(requestFixture.getServiceRequest1(),
                        requestFixture.getServiceRequest2()))
                .petSitterCriticalServices(
                        Arrays.asList(requestFixture.getCriticalServiceRequest1()))
                .build();

        response = PetSitterCreateResponse.builder()
                .petSitterHouses(
                        Arrays.asList(responseFixture.getHouse1(), responseFixture.getHouse2()))
                .petSitterHashTags(
                        Arrays.asList(responseFixture.getHashTag1(), responseFixture.getHashTag2()))
                .introduction(request.getPetSitterIntroduction())
                .petSitterLicenseImg("iamge")
                .petSitterCriticalServices(
                        Arrays.asList(responseFixture.getPetSitterCriticalService1(),
                                responseFixture.getPetSitterCriticalService2()))
                .petSitterServices(Arrays.asList(responseFixture.getPetSitterService1(),
                        responseFixture.getPetSitterService2()))
                .build();
    }

    @Test
    @DisplayName("펫시터 정보 등록 테스트")
    @WithMockUser(username = "test", roles = "PESITTER")
    void registerPetSitterInfo_success() throws Exception {

        when(service.registerPetSitterInfo(any(), any())).thenReturn(response);

        mockMvc.perform(post("/api/v2/pet-sitters/info")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.petSitterHouses").exists())
                .andExpect(jsonPath("$.result.petSitterHashTags").exists())
                .andExpect(jsonPath("$.result.introduction").exists())
                .andExpect(jsonPath("$.result.petSitterLicenseImg").exists())
                .andExpect(jsonPath("$.result.petSitterCriticalServices").exists())
                .andExpect(jsonPath("$.result.petSitterServices").exists());

        ArgumentCaptor<PetSitterCreateRequest> requestCaptor = ArgumentCaptor.forClass(
                PetSitterCreateRequest.class);
        ArgumentCaptor<String> emailCaptor = ArgumentCaptor.forClass(String.class);
        verify(service).registerPetSitterInfo(requestCaptor.capture(),emailCaptor.capture());
        PetSitterCreateRequest requestValue = requestCaptor.getValue();
        String emailValue = emailCaptor.getValue();

        Assertions.assertEquals(request.getPetSitterIntroduction(),requestValue.getPetSitterIntroduction());
        Assertions.assertEquals("test",emailValue);

    }
}
