package com.ajou_nice.with_pet.party;

import com.ajou_nice.with_pet.domain.dto.party.PartyInfoResponse;
import com.ajou_nice.with_pet.domain.dto.party.PartyRequest;
import com.ajou_nice.with_pet.exception.AppException;
import com.ajou_nice.with_pet.service.PartyService;
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

    @Test
    @Transactional
    @DisplayName("파티 생성 성공")
    void createParty() {
        //given
        String userId = "user1";
        PartyRequest partyRequest = new PartyRequest("party1", "dog_img", "흰둥이", "말티즈", "male",
                true,
                LocalDate.of(2020, 10, 17), 5.0f, "123456789");
        //when
        PartyInfoResponse result = partyService.createParty(userId, partyRequest);
        //then

        Assertions.assertEquals("party1", result.getPartyName());
    }

    @Test
    @Transactional
    @DisplayName("파티 생성 실패 - 파티 생성하려는 유저가 DB에 존재하지 않을 때")
    void createParty_fail_1() {
        //given
        String userId = "";
        PartyRequest partyRequest = new PartyRequest("party1", "dog_img", "흰둥이", "말티즈", "male",
                true,
                LocalDate.of(2020, 10, 17), 5.0f, "123456789");
        //when then
        Assertions.assertThrows(AppException.class, () -> {
            partyService.createParty(userId, partyRequest);
        });
    }
}
