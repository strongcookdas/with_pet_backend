package com.ajou_nice.with_pet.party;

import com.ajou_nice.with_pet.enums.Gender;
import com.ajou_nice.with_pet.group.model.dto.add.PartyAddRequest;
import com.ajou_nice.with_pet.domain.entity.User;
import com.ajou_nice.with_pet.domain.entity.embedded.Address;
import com.ajou_nice.with_pet.exception.AppException;
import com.ajou_nice.with_pet.exception.ErrorCode;
import com.ajou_nice.with_pet.fixture.Fixture;
import com.ajou_nice.with_pet.group.model.dto.add.PartyAddResponse;
import com.ajou_nice.with_pet.repository.UserRepository;
import com.ajou_nice.with_pet.group.service.PartyService;
import java.time.LocalDate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
public class PartyTest {

    @Autowired
    PartyService partyService;

    @Autowired
    UserRepository userRepository;

    Fixture fixture = new Fixture();

    Address address;
    User user;

    @Transactional
    public void initialize() {
        address = fixture.getAddress();
        user = fixture.getUser1();

        userRepository.save(user);
    }

    @Test
    @Transactional
    @DisplayName("파티 생성 성공")
    void createParty() {
        //given
        initialize();
        String userId = user.getEmail();
        PartyAddRequest partyAddRequest = new PartyAddRequest("party1", "dog_img", "흰둥이", "말티즈", Gender.FEMALE, true, LocalDate.of(2020, 10, 17), 5.0f);
        //when
        PartyAddResponse result = partyService.createParty(userId, partyAddRequest);
        //then

        Assertions.assertEquals("party1", result.getPartyName());
    }

    @Test
    @Transactional
    @DisplayName("파티 생성 실패 - 파티 생성하려는 유저가 DB에 존재하지 않을 때")
    void createParty_fail_1() {
        //given
        String userId = "";
        PartyAddRequest partyAddRequest = new PartyAddRequest("party1", "dog_img", "흰둥이", "말티즈", Gender.FEMALE, true, LocalDate.of(2020, 10, 17), 5.0f);
        //when then
        Assertions.assertThrows(AppException.class, () -> {
            partyService.createParty(userId, partyAddRequest);
        });
    }

    @Test
    @Transactional
    @DisplayName("파티 생성 실패 - 그룹 생성 횟수 제한")
    void createParty_fail_2() {
        //given
        initialize();
        user.updatePartyCount(5);
        userRepository.saveAndFlush(user);
        String userId = user.getEmail();

        PartyAddRequest partyAddRequest = new PartyAddRequest("party1", "dog_img", "흰둥이", "말티즈", Gender.FEMALE, true, LocalDate.of(2020, 10, 17), 5.0f);
        //when
        AppException exception = Assertions.assertThrows(AppException.class, () -> {
            partyService.createParty(userId, partyAddRequest);
        });
        //then
        Assertions.assertEquals(ErrorCode.TOO_MANY_GROUP, ErrorCode.TOO_MANY_GROUP);
    }
}
