package com.ajou_nice.with_pet.petsitter.main;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ajou_nice.with_pet.CommonApiTest;
import com.ajou_nice.with_pet.petsitter.controller.PetSitterController;
import com.ajou_nice.with_pet.petsitter.service.PetSitterService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@WebMvcTest(PetSitterController.class)
@MockBean(JpaMetamodelMappingContext.class)
public class PetSitterMainControllerTest extends CommonApiTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    PetSitterService petSitterService;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("메인페이지 펫시터 리스트 컨트롤러 테스트")
    @WithMockUser
    void petSitterMainList() throws Exception {
        //given
        MultiValueMap<String, String> info = new LinkedMultiValueMap<>();

        info.add("page", "0");
        info.add("size", "10");
        info.add("sort", "createdAt,desc");
        info.add("dogSize", "소형견");
        info.add("address", "원천동");
        info.add("service", "산책");
        info.add("service", "목욕");
        info.add("service", "미용");

        //when
        mockMvc.perform(get("/api/v1/show-petsitter")
                        .with(csrf())
                        .params(info))
                .andDo(print())
                .andExpect(status().isOk());

        //then
        ArgumentCaptor<Pageable> pageableArgumentCaptor = ArgumentCaptor.forClass(Pageable.class);
        ArgumentCaptor<String> dogSizeCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<List<String>> servicesCaptor = ArgumentCaptor.forClass(List.class);
        ArgumentCaptor<String> addressCaptor = ArgumentCaptor.forClass(String.class);
        verify(petSitterService).getPetSitters(pageableArgumentCaptor.capture(),dogSizeCaptor.capture(),servicesCaptor.capture(),addressCaptor.capture());
        PageRequest pageRequest = (PageRequest) pageableArgumentCaptor.getValue();
        List<String> serviceList = servicesCaptor.getValue();

        assertEquals(0, pageRequest.getPageNumber());
        assertEquals(10,pageRequest.getPageSize());
        assertEquals(Sort.by(Direction.DESC, "createdAt"), pageRequest.getSort());
        assertEquals("소형견",dogSizeCaptor.getValue());
        assertEquals("원천동",addressCaptor.getValue());
        assertEquals("산책", serviceList.get(0));
        assertEquals("목욕", serviceList.get(1));
        assertEquals("미용", serviceList.get(2));

    }
}
