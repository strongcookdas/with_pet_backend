package com.ajou_nice.with_pet.diary;

import com.ajou_nice.with_pet.diary.model.entity.Category;
import com.ajou_nice.with_pet.diary.model.entity.Diary;
import com.ajou_nice.with_pet.dog.model.entity.Dog;
import com.ajou_nice.with_pet.group.model.entity.Party;
import com.ajou_nice.with_pet.domain.entity.User;
import com.ajou_nice.with_pet.domain.entity.UserParty;
import com.ajou_nice.with_pet.domain.entity.embedded.Address;
import com.ajou_nice.with_pet.exception.AppException;
import com.ajou_nice.with_pet.exception.ErrorCode;
import com.ajou_nice.with_pet.fixture.Fixture;
import com.ajou_nice.with_pet.repository.CategoryRepository;
import com.ajou_nice.with_pet.diary.repository.DiaryRepository;
import com.ajou_nice.with_pet.dog.repository.DogRepository;
import com.ajou_nice.with_pet.group.repository.PartyRepository;
import com.ajou_nice.with_pet.repository.UserPartyRepository;
import com.ajou_nice.with_pet.repository.UserRepository;
import com.ajou_nice.with_pet.diary.service.UserDiaryService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
public class DeleteUserDiaryTest {

    @Autowired
    UserDiaryService userDiaryService;

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

        boss = userRepository.save(boss);
        member = userRepository.save(member);
        notMember = userRepository.save(notMember);
        party = partyRepository.save(party);
        userParty1 = userPartyRepository.save(userParty1);
        userParty2 = userPartyRepository.save(userParty2);
        category = categoryRepository.save(category);
        dog = dogRepository.save(dog);
    }

    @Test
    @Transactional
    @DisplayName("일지 삭제 성공")
    void deleteUserDiary_success() {
        //given
        initialize();
        Diary userDiary = fixture.getUserDiary();
        userDiary = diaryRepository.save(userDiary);
        String userId = boss.getEmail();
        Long userDiaryId = userDiary.getDiaryId();
        //when
        String result = userDiaryService.deleteUserDiary(userId, userDiaryId);
        //then
        Assertions.assertEquals("일지가 삭제되었습니다.", result);
    }

    @Test
    @Transactional
    @DisplayName("일지 삭제_실패1 유저가 존재하지 않는 경우")
    void deleteUserDiary_fail() {
        //given
        initialize();
        Diary userDiary = fixture.getUserDiary();
        userDiary = diaryRepository.save(userDiary);
        String userId = "";
        Long userDiaryId = userDiary.getDiaryId();
        //when
        AppException appException = Assertions.assertThrows(AppException.class,
                () -> userDiaryService.deleteUserDiary(userId, userDiaryId));
        //then
        Assertions.assertEquals(ErrorCode.USER_NOT_FOUND, appException.getErrorCode());
    }

    @Test
    @Transactional
    @DisplayName("일지 삭제_실패2 작성자가 아닌 경우")
    void deleteUserDiary_fail2() {
        //given
        initialize();
        Diary userDiary = fixture.getUserDiary();
        userDiary = diaryRepository.save(userDiary);
        String userId = member.getEmail();
        Long userDiaryId = userDiary.getDiaryId();
        //when
        AppException appException = Assertions.assertThrows(AppException.class,
                () -> userDiaryService.deleteUserDiary(userId, userDiaryId));
        //then
        Assertions.assertEquals(ErrorCode.INVALID_PERMISSION, appException.getErrorCode());
    }

    @Test
    @Transactional
    @DisplayName("일지 삭제_실패3 일지가 존재하지 않는 경우")
    void deleteUserDiary_fail3() {
        //given
        initialize();
        Diary userDiary = fixture.getUserDiary();
        userDiary = diaryRepository.save(userDiary);
        String userId = boss.getEmail();
        Long userDiaryId = Long.MAX_VALUE;
        //when
        AppException appException = Assertions.assertThrows(AppException.class,
                () -> userDiaryService.deleteUserDiary(userId, userDiaryId));
        //then
        Assertions.assertEquals(ErrorCode.DIARY_NOT_FOUND, appException.getErrorCode());
    }
}

