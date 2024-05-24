package com.ajou_nice.with_pet.dog;

import com.ajou_nice.with_pet.domain.entity.Dog;
import com.ajou_nice.with_pet.group.model.entity.Party;
import com.ajou_nice.with_pet.petsitter.model.entity.PetSitter;
import com.ajou_nice.with_pet.domain.entity.Reservation;
import com.ajou_nice.with_pet.domain.entity.User;
import com.ajou_nice.with_pet.domain.entity.UserParty;
import com.ajou_nice.with_pet.domain.entity.embedded.Address;
import com.ajou_nice.with_pet.exception.AppException;
import com.ajou_nice.with_pet.exception.ErrorCode;
import com.ajou_nice.with_pet.fixture.Fixture;
import com.ajou_nice.with_pet.repository.DogRepository;
import com.ajou_nice.with_pet.group.repository.PartyRepository;
import com.ajou_nice.with_pet.petsitter.repository.PetSitterRepository;
import com.ajou_nice.with_pet.repository.ReservationRepository;
import com.ajou_nice.with_pet.repository.UserPartyRepository;
import com.ajou_nice.with_pet.repository.UserRepository;
import com.ajou_nice.with_pet.service.DogService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
public class DogDeleteTest {

    @Autowired
    DogService dogService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    DogRepository dogRepository;
    @Autowired
    UserPartyRepository userPartyRepository;
    @Autowired
    PartyRepository partyRepository;
    @Autowired
    PetSitterRepository petSitterRepository;
    @Autowired
    ReservationRepository reservationRepository;

    Fixture fixture = new Fixture();
    Address address;
    User boss;
    User member;
    User notMember;
    User petsitterUser;
    PetSitter petSitter;
    Party party;
    UserParty userParty1;
    UserParty userParty2;
    Dog dog;
    Reservation reservation;

    @Transactional
    public void initialize() {
        address = fixture.getAddress();
        boss = fixture.getUser1();
        member = fixture.getUser2();
        notMember = fixture.getUser3();
        party = fixture.getParty();
        userParty1 = fixture.getUserParty1();
        userParty2 = fixture.getUserParty2();
        dog = fixture.getDog();
        petsitterUser = fixture.getUser4();
        petSitter = fixture.getPetSitter1();
        reservation = fixture.getReservation1();

        boss = userRepository.save(boss);
        member = userRepository.save(member);
        notMember = userRepository.save(notMember);
        petsitterUser = userRepository.save(petsitterUser);
        petSitter = petSitterRepository.save(petSitter);
        party = partyRepository.save(party);
        userParty1 = userPartyRepository.save(userParty1);
        userParty2 = userPartyRepository.save(userParty2);
        dog = dogRepository.save(dog);

    }

    @Test
    @Transactional
    @DisplayName("반려견 삭제 성공")
    void deleteDog_success() {
        //given
        initialize();
        String userId = boss.getEmail();
        Long dogId = dog.getDogId();
        //when
        Boolean deleteParty = dogService.deleteDog(userId, dogId);
        //then
        Assertions.assertEquals(true, deleteParty);
    }

    @Test
    @Transactional
    @DisplayName("반려견 삭제 실패1_유저가 존재하지 않음")
    void deleteDog_fail1() {
        //given
        initialize();
        String userId = "";
        Long dogId = dog.getDogId();
        //when
        AppException exception = Assertions.assertThrows(AppException.class,
                () -> dogService.deleteDog(userId, dogId));
        //then
        Assertions.assertEquals(ErrorCode.USER_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    @Transactional
    @DisplayName("반려견 삭제 실패2_유저가 방장이 아닌 경우")
    void deleteDog_fail2() {
        //given
        initialize();
        String userId = member.getEmail();
        Long dogId = dog.getDogId();
        //when
        AppException exception = Assertions.assertThrows(AppException.class,
                () -> dogService.deleteDog(userId, dogId));
        //then
        Assertions.assertEquals(ErrorCode.INVALID_PERMISSION, exception.getErrorCode());
    }

    @Test
    @Transactional
    @DisplayName("반려견 삭제 실패3_반려견이 존재하지 않는 경우")
    void deleteDog_fail3() {
        //given
        initialize();
        String userId = boss.getEmail();
        Long dogId = dog.getDogId();
        //when
        AppException exception = Assertions.assertThrows(AppException.class,
                () -> dogService.deleteDog(userId, Long.MAX_VALUE));
        //then
        Assertions.assertEquals(ErrorCode.DOG_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    @Transactional
    @DisplayName("반려견 삭제 실패3_반려견에게 PAYED, USE, APPROVAL 존재하는 경우")
    void deleteDog_fail4() {
        //given
        initialize();
        String userId = boss.getEmail();
        Long dogId = dog.getDogId();
        Reservation reservation1 = fixture.getReservation1();
        reservation1 = reservationRepository.save(reservation1);
        //when
        AppException exception = Assertions.assertThrows(AppException.class,
                () -> dogService.deleteDog(userId, dogId));
        //then
        Assertions.assertEquals(ErrorCode.CAN_NOT_DELETE_DOG, exception.getErrorCode());
    }
}
