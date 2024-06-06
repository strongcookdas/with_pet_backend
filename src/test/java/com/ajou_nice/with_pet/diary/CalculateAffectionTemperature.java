package com.ajou_nice.with_pet.diary;

import com.ajou_nice.with_pet.domain.dto.diary.DiaryRequest;
import com.ajou_nice.with_pet.domain.entity.Category;
import com.ajou_nice.with_pet.dog.model.entity.Dog;
import com.ajou_nice.with_pet.group.model.entity.Party;
import com.ajou_nice.with_pet.domain.entity.User;
import com.ajou_nice.with_pet.domain.entity.UserParty;
import com.ajou_nice.with_pet.domain.entity.embedded.Address;
import com.ajou_nice.with_pet.fixture.Fixture;
import com.ajou_nice.with_pet.repository.CategoryRepository;
import com.ajou_nice.with_pet.repository.DiaryRepository;
import com.ajou_nice.with_pet.dog.repository.DogRepository;
import com.ajou_nice.with_pet.group.repository.PartyRepository;
import com.ajou_nice.with_pet.petsitter.repository.PetSitterRepository;
import com.ajou_nice.with_pet.reservation.repository.ReservationRepository;
import com.ajou_nice.with_pet.repository.UserPartyRepository;
import com.ajou_nice.with_pet.repository.UserRepository;
import com.ajou_nice.with_pet.service.UserDiaryService;
import java.time.LocalDate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
public class CalculateAffectionTemperature {

    @Autowired
    UserDiaryService userDiaryService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    DogRepository dogRepository;
    @Autowired
    UserPartyRepository userPartyRepository;
    @Autowired
    DiaryRepository diaryRepository;
    @Autowired
    PartyRepository partyRepository;
    @Autowired
    PetSitterRepository petSitterRepository;
    @Autowired
    ReservationRepository reservationRepository;
    @Autowired
    CategoryRepository categoryRepository;
    Fixture fixture = new Fixture();
    Address address;
    User boss;
    Party party;
    Dog dog;
    Category category;

    @Transactional
    public void initialize() {
        address = fixture.getAddress();
        boss = fixture.getUser1();
        party = fixture.getParty();
        dog = fixture.getDog();
        category = fixture.getCategory();
        UserParty userParty = fixture.getUserParty1();

        boss = userRepository.save(boss);
        party = partyRepository.save(party);
        dog = dogRepository.save(dog);
        category = categoryRepository.save(category);
        userParty = userPartyRepository.save(userParty);
    }

    @Test
    @Transactional
    @DisplayName("반려견의 사회성 온도 계산")
    void calculateAffectionTemperature_success() {
        //given 일지가 주어졌을 때
        initialize();
        System.out.println(dog.getDogId());
        DiaryRequest diaryRequest = new DiaryRequest(dog.getDogId(),"제목",category.getCategoryId(),"내용입니다.","image",LocalDate.of(2023,06,12));
        //when 일지를 작성했을 때
        System.out.println("반려견의 사회성 사회 : ");
        userDiaryService.writeUserDiary(boss.getEmail(),diaryRequest);
        //then 일지를 작성한 결과 사회성 온도는 39도가 된다.
        Assertions.assertEquals(39,dog.getDogAffectionTemperature());

    }
}
