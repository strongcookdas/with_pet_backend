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
public class PartyMemberExpelTest {

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
    @DisplayName("그룹 방출 성공")
    void expelMember_success() {
        //given
        initialize();
        String userId = boss.getId();
        Long partyId = party.getPartyId();
        Long memberId = member.getId();
        //when
        String result = partyService.expelMember(userId, partyId, memberId);
        //then
        Assertions.assertEquals(member.getName() + "님이 그룹에서 방출되었습니다.", result);
    }

    @Test
    @Transactional
    @DisplayName("그룹 방출 실패 - 그룹원이 삭제하는 경우")
    void expelMember_fail1() {
        //given
        initialize();
        String userId = member.getId();
        Long partyId = party.getPartyId();
        Long memberId = member.getId();
        //when
        AppException exception = Assertions.assertThrows(AppException.class,
                () -> partyService.expelMember(userId, partyId, memberId));
        //then
        Assertions.assertEquals(ErrorCode.INVALID_PERMISSION, exception.getErrorCode());
    }

    @Test
    @Transactional
    @DisplayName("그룹 방출 실패 - 삭제하려는 그룹원이 없는 경우")
    void expelMember_fail2() {
        //given
        initialize();
        String userId = boss.getId();
        Long partyId = party.getPartyId();
        Long memberId = Long.MAX_VALUE;
        //when
        AppException exception = Assertions.assertThrows(AppException.class,
                () -> partyService.expelMember(userId, partyId, memberId));
        //then
        Assertions.assertEquals(ErrorCode.USER_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    @Transactional
    @DisplayName("그룹 방출 실패 - 삭제하는 방장이 존재하지 않는 경우")
    void expelMember_fail3() {
        //given
        initialize();
        String userId = "";
        Long partyId = party.getPartyId();
        Long memberId = member.getId();
        //when
        AppException exception = Assertions.assertThrows(AppException.class,
                () -> partyService.expelMember(userId, partyId, memberId));
        //then
        Assertions.assertEquals(ErrorCode.USER_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    @Transactional
    @DisplayName("그룹 방출 실패 - 삭제하려는 그룹원이 그룹에 없는 경우")
    void expelMember_fail4() {
        //given
        initialize();
        String userId = boss.getId();
        Long partyId = party.getPartyId();
        Long memberId = notMember.getId();
        //when
        AppException exception = Assertions.assertThrows(AppException.class,
                () -> partyService.expelMember(userId, partyId, memberId));
        //then
        Assertions.assertEquals(ErrorCode.NOT_FOUND_GROUP_MEMBER, exception.getErrorCode());
    }

    @Test
    @Transactional
    @DisplayName("그룹 방출 실패 - 그룹이 없는 경우")
    void expelMember_fail5() {
        //given
        initialize();
        String userId = boss.getId();
        Long partyId = Long.MAX_VALUE;
        Long memberId = notMember.getId();
        //when
        AppException exception = Assertions.assertThrows(AppException.class,
                () -> partyService.expelMember(userId, partyId, memberId));
        //then
        Assertions.assertEquals(ErrorCode.GROUP_NOT_FOUND, exception.getErrorCode());
    }
}
