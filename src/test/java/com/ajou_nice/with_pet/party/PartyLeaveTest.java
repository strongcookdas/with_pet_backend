package com.ajou_nice.with_pet.party;

import com.ajou_nice.with_pet.domain.entity.Party;
import com.ajou_nice.with_pet.domain.entity.User;
import com.ajou_nice.with_pet.domain.entity.UserParty;
import com.ajou_nice.with_pet.domain.entity.embedded.Address;
import com.ajou_nice.with_pet.exception.AppException;
import com.ajou_nice.with_pet.exception.ErrorCode;
import com.ajou_nice.with_pet.fixture.Fixture;
import com.ajou_nice.with_pet.repository.DogRepository;
import com.ajou_nice.with_pet.repository.PartyRepository;
import com.ajou_nice.with_pet.repository.UserPartyRepository;
import com.ajou_nice.with_pet.repository.UserRepository;
import com.ajou_nice.with_pet.service.PartyService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
public class PartyLeaveTest {

    @Autowired
    PartyService partyService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PartyRepository partyRepository;

    @Autowired
    UserPartyRepository userPartyRepository;

    @Autowired
    DogRepository dogRepository;

    Fixture fixture = new Fixture();

    Address address;
    User boss;
    User member;
    User notMember;
    Party party;

    UserParty userParty1;
    UserParty userParty2;

    @Transactional
    public void initialize() {
        address = fixture.getAddress();
        boss = fixture.getUser1();
        member = fixture.getUser2();
        notMember = fixture.getUser3();
        party = fixture.getParty();
        userParty1 = fixture.getUserParty1();
        userParty2 = fixture.getUserParty2();

        boss = userRepository.save(boss);
        member = userRepository.save(member);
        notMember = userRepository.save(notMember);
        party = partyRepository.save(party);
        userParty1 = userPartyRepository.save(userParty1);
        userParty2 = userPartyRepository.save(userParty2);
    }

    @Test
    @Transactional
    @DisplayName("그룹 탈퇴 성공 - 그룹원일때")
    void leaveParty_success2() {
        //given
        initialize();
        String userId = member.getId();
        Long partyId = party.getPartyId();
        //when
        String result = partyService.leaveParty(userId, partyId);
        //then
        Assertions.assertEquals("그룹에서 탈퇴되었습니다.", result);
    }

    @Test
    @Transactional
    @DisplayName("그룹 탈퇴 성공 - 방장이 탈퇴할 때")
    void leaveParty_success1() {
        //given
        initialize();
        String userId = boss.getId();
        Long partyId = party.getPartyId();
        //when
        String result = partyService.leaveParty(userId, partyId);
        //then
        Assertions.assertEquals(member.getName()+"님이 방장이 되었습니다.", result);
    }

    @Test
    @Transactional
    @DisplayName("그룹 탈퇴 실패 - 그룹원이 아닐 때")
    void leaveParty_fail1() {
        //given
        initialize();
        String userId = fixture.getUser3().getId();
        Long partyId = party.getPartyId();
        //when
        AppException exception = Assertions.assertThrows(AppException.class,
                () -> partyService.leaveParty(userId, partyId));
        //then
        Assertions.assertEquals(ErrorCode.NOT_FOUND_GROUP_MEMBER, exception.getErrorCode());
    }

    @Test
    @Transactional
    @DisplayName("그룹 탈퇴 실패 - 유저가 없을 때")
    void leaveParty_fail2() {
        //given
        initialize();
        String userId = "";
        Long partyId = party.getPartyId();
        //when
        AppException exception = Assertions.assertThrows(AppException.class,
                () -> partyService.leaveParty(userId, partyId));
        //then
        Assertions.assertEquals(ErrorCode.USER_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    @Transactional
    @DisplayName("그룹 탈퇴 실패 - 그룹이 없을 때")
    void leaveParty_fail3() {
        //given
        initialize();
        String userId = member.getId();
        Long partyId = Long.MAX_VALUE;
        //when
        AppException exception = Assertions.assertThrows(AppException.class,
                () -> partyService.leaveParty(userId, partyId));
        //then
        Assertions.assertEquals(ErrorCode.GROUP_NOT_FOUND, exception.getErrorCode());
    }
}
