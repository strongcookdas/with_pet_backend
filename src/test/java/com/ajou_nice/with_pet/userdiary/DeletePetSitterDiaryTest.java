package com.ajou_nice.with_pet.userdiary;

import com.ajou_nice.with_pet.domain.entity.Category;
import com.ajou_nice.with_pet.domain.entity.Diary;
import com.ajou_nice.with_pet.domain.entity.Dog;
import com.ajou_nice.with_pet.domain.entity.Party;
import com.ajou_nice.with_pet.domain.entity.PetSitter;
import com.ajou_nice.with_pet.domain.entity.User;
import com.ajou_nice.with_pet.domain.entity.UserParty;
import com.ajou_nice.with_pet.domain.entity.embedded.Address;
import com.ajou_nice.with_pet.exception.AppException;
import com.ajou_nice.with_pet.exception.ErrorCode;
import com.ajou_nice.with_pet.fixture.Fixture;
import com.ajou_nice.with_pet.repository.CategoryRepository;
import com.ajou_nice.with_pet.repository.DiaryRepository;
import com.ajou_nice.with_pet.repository.DogRepository;
import com.ajou_nice.with_pet.repository.PartyRepository;
import com.ajou_nice.with_pet.repository.PetSitterRepository;
import com.ajou_nice.with_pet.repository.UserPartyRepository;
import com.ajou_nice.with_pet.repository.UserRepository;
import com.ajou_nice.with_pet.service.PetSitterDiaryService;
import com.ajou_nice.with_pet.service.UserDiaryService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
public class DeletePetSitterDiaryTest {
    @Autowired
    PetSitterDiaryService petSitterDiaryService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PartyRepository partyRepository;

    @Autowired
    UserPartyRepository userPartyRepository;

    @Autowired
    DogRepository dogRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    DiaryRepository diaryRepository;

    @Autowired
    PetSitterRepository petSitterRepository;

    Fixture fixture = new Fixture();

    Address address;
    User boss;
    User member;
    User notMember;
    Party party;

    UserParty userParty1;
    UserParty userParty2;
    Category category;
    Dog dog;
    PetSitter petSitter;
    User petSitterUser;

    @Transactional
    public void initialize() {
        address = fixture.getAddress();
        boss = fixture.getUser1();
        member = fixture.getUser2();
        notMember = fixture.getUser3();
        party = fixture.getParty();
        userParty1 = fixture.getUserParty1();
        userParty2 = fixture.getUserParty2();
        category = fixture.getCategory();
        dog = fixture.getDog();
        petSitterUser = fixture.getUser4();
        petSitter = fixture.getPetSitter1();

        boss = userRepository.save(boss);
        member = userRepository.save(member);
        notMember = userRepository.save(notMember);
        party = partyRepository.save(party);
        userParty1 = userPartyRepository.save(userParty1);
        userParty2 = userPartyRepository.save(userParty2);
        category = categoryRepository.save(category);
        dog = dogRepository.save(dog);
        petSitterUser = userRepository.save(petSitterUser);
        petSitter = petSitterRepository.save(petSitter);
    }

    @Test
    @Transactional
    @DisplayName("일지 삭제 성공")
    void deleteUserDiary_success() {
        //given
        initialize();
        Diary petSitterDiary = fixture.getPetSitterDiary();
        petSitterDiary = diaryRepository.save(petSitterDiary);
        String userId = boss.getId();
        Long diaryId = petSitterDiary.getDiaryId();
        //when
        String result = petSitterDiaryService.deletePetSitterDiary(userId, diaryId);
        //then
        Assertions.assertEquals("일지가 삭제되었습니다.", result);
    }

    @Test
    @Transactional
    @DisplayName("펫시터 일지 삭제_실패1 유저가 존재하지 않는 경우")
    void deleteUserDiary_fail() {
        //given
        initialize();
        Diary petSitterDiary = fixture.getPetSitterDiary();
        petSitterDiary = diaryRepository.save(petSitterDiary);
        String userId = "";
        Long diaryId = petSitterDiary.getDiaryId();
        //when
        AppException appException = Assertions.assertThrows(AppException.class,
                () -> petSitterDiaryService.deletePetSitterDiary(userId, diaryId));
        //then
        Assertions.assertEquals(ErrorCode.USER_NOT_FOUND, appException.getErrorCode());
    }

    @Test
    @Transactional
    @DisplayName("펫시터 일지 삭제_실패2 그룹 리더가 아닌 경우")
    void deleteUserDiary_fail2() {
        //given
        initialize();
        Diary petSitterDiary = fixture.getPetSitterDiary();
        petSitterDiary = diaryRepository.save(petSitterDiary);
        String userId = member.getId();
        Long diaryId = petSitterDiary.getDiaryId();
        //when
        AppException appException = Assertions.assertThrows(AppException.class,
                () -> petSitterDiaryService.deletePetSitterDiary(userId, diaryId));
        //then
        Assertions.assertEquals(ErrorCode.INVALID_PERMISSION, appException.getErrorCode());
    }

    @Test
    @Transactional
    @DisplayName("펫시터 일지 삭제_실패3 일지가 존재하지 않은 경우")
    void deleteUserDiary_fail3() {
        //given
        initialize();
        Diary petSitterDiary = fixture.getPetSitterDiary();
        petSitterDiary = diaryRepository.save(petSitterDiary);
        String userId = boss.getId();
        Long diaryId = Long.MAX_VALUE;
        //when
        AppException appException = Assertions.assertThrows(AppException.class,
                () -> petSitterDiaryService.deletePetSitterDiary(userId, diaryId));
        //then
        Assertions.assertEquals(ErrorCode.DIARY_NOT_FOUND, appException.getErrorCode());
    }
}
