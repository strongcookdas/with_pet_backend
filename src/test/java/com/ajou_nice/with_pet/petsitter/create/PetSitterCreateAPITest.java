package com.ajou_nice.with_pet.petsitter.create;

import com.ajou_nice.with_pet.CommonApiTest;
import com.ajou_nice.with_pet.controller.petsitter.PetSitterCreateAPI;
import com.ajou_nice.with_pet.dto.petsitter.PetSitterCreateRequest;
import com.ajou_nice.with_pet.dto.petsitter.PetSitterCreateResponse;
import com.ajou_nice.with_pet.fixture.Fixture.Request;
import com.ajou_nice.with_pet.service.petsitter.PetSitterCreateService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
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
    PetSitterCreateRequest request;
    PetSitterCreateResponse response;

    @BeforeEach
    void setUp(){
        request = PetSitterCreateRequest.builder()
                .petSitterHouses(Arrays.asList(requestFixture.getHouseRequest1(),requestFixture.getHouseRequest2()))
                .petSitterHashTags(Arrays.asList(requestFixture.getHashTagRequest1(),requestFixture.getHashTagRequest2()))
                .petSitterIntroduction("소개글")
                .petSitterServices(Arrays.asList(requestFixture.getServiceRequest1(),requestFixture.getServiceRequest2()))
                .petSitterCriticalServices(Arrays.asList(requestFixture.getCriticalServiceRequest1(),requestFixture.getCriticalServiceRequest2()))
                .build();
    }


}
