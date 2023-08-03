package com.ajou_nice.with_pet.user.auth.signup;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ajou_nice.with_pet.CommonApiTest;
import com.ajou_nice.with_pet.controller.user.UserAuthController;
import com.ajou_nice.with_pet.domain.dto.auth.UserSignUpRequest;
import com.ajou_nice.with_pet.domain.dto.auth.UserSignUpResponse;
import com.ajou_nice.with_pet.domain.dto.embedded.AddressDto;
import com.ajou_nice.with_pet.service.user.UserAuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
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

@WebMvcTest(UserAuthController.class)
@MockBean(JpaMetamodelMappingContext.class)
public class SignUpControllerTest extends CommonApiTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserAuthService userAuthService;

    @Autowired
    ObjectMapper objectMapper;

    UserSignUpRequest userSignUpRequest;
    UserSignUpResponse userSignUpResponse;
    AddressDto address;

    @BeforeEach
    public void setUp(){
        address = AddressDto.builder()
                .zipcode("111111")
                .streetAdr("월드컵로")
                .detailAdr("팔달관")
                .build();

        userSignUpRequest = UserSignUpRequest.builder()
                .userName("user1")
                .userEmail("email@email.com")
                .userPassword("password")
                .userPasswordCheck("password")
                .profileImg("image")
                .phoneNum("010-0000-0000")
                .address(address)
                .build();

        userSignUpResponse = UserSignUpResponse.builder()
                .userId(1L)
                .userName("user1")
                .build();
    }

    @Test
    @DisplayName("회원가입")
    @WithMockUser
    void signUp_success() throws Exception {
        when(userAuthService.signUp(userSignUpRequest))
                .thenReturn(userSignUpResponse);

        mockMvc.perform(post("/api/v2/users/signup")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(userSignUpRequest)))
                .andDo(print())
                .andExpect(status().isOk());
    }
}
