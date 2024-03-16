package com.ajou_nice.with_pet.controller.auth;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ajou_nice.with_pet.CommonApiTest;
import com.ajou_nice.with_pet.controller.user.AuthController;
import com.ajou_nice.with_pet.domain.dto.auth.UserSignUpRequest;
import com.ajou_nice.with_pet.domain.dto.auth.UserSignUpResponse;
import com.ajou_nice.with_pet.domain.dto.embedded.AddressDto;
import com.ajou_nice.with_pet.fixture.AddressDtoFixture;
import com.ajou_nice.with_pet.fixture.UserDtoFixtures;
import com.ajou_nice.with_pet.service.user.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
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

@WebMvcTest(AuthController.class)
@MockBean(JpaMetamodelMappingContext.class)
public class SignUpControllerTest extends CommonApiTest {

    final String USER_SIGN_UP_POST_API = "/api/v2/users/sign-up";
    @Autowired
    MockMvc mockMvc;

    @MockBean
    AuthService authService;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    Validator validator;

    UserSignUpRequest userSignUpRequest;
    UserSignUpResponse userSignUpResponse;
    AddressDto address;

    @BeforeEach
    public void setUp() {
        address = AddressDtoFixture.createAddressDto("16499", "경기도 수원시 영통구 월드컵로 206 (원천동, 아주대학교)", "팔달관");
    }

    @Test
    @DisplayName("회원가입 성공")
    @WithMockUser
    void signUp_success() throws Exception {
        //given
        userSignUpRequest = UserDtoFixtures.createUserSignUpRequest("user", "email@email.com", "password0302!", "password0302!",
                "https://ajounciewithpet.s3.ap-northeast-2.amazonaws.com/default-user.png", "010-0000-0000", address);
        userSignUpResponse = UserDtoFixtures.createUserSignUpResponse(1L, "user");
        //when
        when(authService.signUp(userSignUpRequest))
                .thenReturn(userSignUpResponse);
        //then
        mockMvc.perform(post(USER_SIGN_UP_POST_API)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(userSignUpRequest)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("회원가입 유효성 검사")
    void validSignUp() {
        //given
        userSignUpRequest = UserDtoFixtures.createUserSignUpRequest("$", "email", "password","password0302!",
                "https://ajounciewithpet.s3.ap-northeast-2.amazonaws.com/default-user.png", "123", address);
        //when
        Set<ConstraintViolation<UserSignUpRequest>> validate = validator.validate(userSignUpRequest);

        //then
        Iterator<ConstraintViolation<UserSignUpRequest>> iterator = validate.iterator();
        List<String> messages = new ArrayList<>();
        while (iterator.hasNext()) {
            ConstraintViolation<UserSignUpRequest> next = iterator.next();
            messages.add(next.getMessage());
        }

        Assertions.assertThat(messages)
                .contains("올바른 이메일 형식을 입력하세요.", "올바른 이름을 입력하세요.",
                        "영문, 특수문자, 숫자 포함 8자 이상의 패스워드를 입력하세요.", "올바른 형식의 전화번호를 입력하세요.");
    }
}
