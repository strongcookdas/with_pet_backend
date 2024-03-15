package com.ajou_nice.with_pet.service;

import com.ajou_nice.with_pet.domain.dto.diary.DiaryRequest;
import com.ajou_nice.with_pet.domain.dto.diary.user.UserDiaryMonthResponse;
import com.ajou_nice.with_pet.domain.dto.diary.user.UserDiaryResponse;
import com.ajou_nice.with_pet.domain.entity.Category;
import com.ajou_nice.with_pet.domain.entity.Dog;
import com.ajou_nice.with_pet.domain.entity.Notification;
import com.ajou_nice.with_pet.domain.entity.User;
import com.ajou_nice.with_pet.domain.entity.Diary;
import com.ajou_nice.with_pet.domain.entity.UserParty;
import com.ajou_nice.with_pet.enums.NotificationType;
import com.ajou_nice.with_pet.exception.AppException;
import com.ajou_nice.with_pet.exception.ErrorCode;
import com.ajou_nice.with_pet.repository.DogRepository;
import com.ajou_nice.with_pet.repository.DiaryRepository;
import com.ajou_nice.with_pet.repository.NotificationRepository;
import com.ajou_nice.with_pet.repository.UserPartyRepository;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserDiaryService {

    private final UserPartyRepository userPartyRepository;
    private final DiaryRepository userDiaryRepository;
    private final NotificationRepository notificationRepository;
    private final NotificationService notificationService;
    private final ValidateCollection valid;
    private final DogRepository dogRepository;


    @Transactional
    public UserDiaryResponse writeUserDiary(String userId, DiaryRequest diaryRequest) {
        //유저 체크
        User user = valid.userValidation(userId);
        //반려견 체크
        Dog dog = valid.dogValidation(diaryRequest.getDogId());
        //반려견 그룹 유저 존재 체크
        if (!userPartyRepository.existsUserPartyByUserAndParty(user, dog.getParty())) {
            throw new AppException(ErrorCode.GROUP_NOT_FOUND, "글 작성 권한이 없습니다.");
        }
        //카테고리 체크
        Category category = valid.categoryValidation(diaryRequest.getCategoryId());

        Diary diary = userDiaryRepository.save(
                Diary.of(diaryRequest, dog, user, category));

        List<UserParty> userParties = userPartyRepository.findAllByParty(dog.getParty());

        dog.updateAffectionTemperature(this.calculateAffectionTemperature(dog));

        log.info("======================debug==========================");

        for (UserParty userParty : userParties) {
            Notification notification = notificationService.sendEmail(
                    user.getName() + "님이 " + dog.getName() + "의 일지를 작성했습니다.",
                    "/calendar",
                    NotificationType.반려인_일지, userParty.getUser());
            notificationService.saveNotification(notification);
//            notificationService.send(notification);
        }
        return UserDiaryResponse.of(diary);
    }


    @Transactional
    public UserDiaryResponse updateUserDiary(String userId, DiaryRequest diaryRequest,
            Long diaryId) {
        //유저 체크
        User user = valid.userValidation(userId);
        //일지 체크
        Diary diary = userDiaryRepository.findById(diaryId).orElseThrow(() -> {
            throw new AppException(ErrorCode.DIARY_NOT_FOUND,
                    ErrorCode.DIARY_NOT_FOUND.getMessage());
        });
        //반려견 체크
        Dog dog = valid.dogValidation(diaryRequest.getDogId());
        //카테고리 체크
        Category category = valid.categoryValidation(diaryRequest.getCategoryId());

        //작성자 비교
        if (!user.equals(diary.getUser())) {
            throw new AppException(ErrorCode.INVALID_PERMISSION, "일지를 수정할 권한이 없습니다.");
        }
        //수정
        diary.update(diaryRequest, dog, category);
        return UserDiaryResponse.of(diary);
    }

    public List<UserDiaryMonthResponse> getUserMonthDiary(String userId, Long dogId,
            Long categoryId,
            String month, String petsitterCheck) {

        //유저 체크
        User user = valid.userValidation(userId);

        List<Diary> userDiaries = userDiaryRepository.findByMonthDate(user.getId(),
                dogId, categoryId, LocalDate.parse(month + "-01"), petsitterCheck);
        return userDiaries.stream().map(UserDiaryMonthResponse::of).collect(Collectors.toList());
    }

    public List<UserDiaryResponse> getUserDayDiary(String userId, Long dogId, Long categoryId,
            String day, String petsitterCheck) {
        //유저체크
        User user = valid.userValidation(userId);

        List<Diary> userDiaries = userDiaryRepository.findByDayDate(user.getId(), dogId,
                categoryId, LocalDate.parse(day), petsitterCheck);
        return userDiaries.stream().map(UserDiaryResponse::of).collect(Collectors.toList());
    }

    @Transactional
    public String deleteUserDiary(String userId, Long diaryId) {

        User user = valid.userValidation(userId);

        Diary diary = userDiaryRepository.findById(diaryId).orElseThrow(() -> {
            throw new AppException(ErrorCode.DIARY_NOT_FOUND,
                    ErrorCode.DIARY_NOT_FOUND.getMessage());
        });

        if (!diary.getUser().getId().equals(user.getId())) {
            throw new AppException(ErrorCode.INVALID_PERMISSION,
                    ErrorCode.INVALID_PERMISSION.getMessage());
        }

        userDiaryRepository.delete(diary);

        return "일지가 삭제되었습니다.";
    }

    @Scheduled(cron = "0 29 4 * * *")
    @Async
    @Transactional
    public void updateAffectionTemperature() {

        List<Dog> dogs = dogRepository.findAll();

        for (Dog dog : dogs) {
            dog.updateAffectionTemperature(this.calculateAffectionTemperature(dog));
        }
    }

    private Double calculateAffectionTemperature(Dog dog) {

        LocalDate createdAt = dog.getCreatedAt().toLocalDate();
        //반려견 등록일과 오늘까지 날짜 구하기
        int days = Period.between(createdAt, LocalDate.now()).getDays();
        //다이어리를 작성한 날짜
        int diaryDayCount = userDiaryRepository.countDiaryDay(dog.getDogId(),
                dog.getCreatedAt().toLocalDate()).intValue();
        //다이어리 작성 개수
        int diaryCount = userDiaryRepository.countDiary(dog.getDogId(),
                dog.getCreatedAt().toLocalDate()).intValue();

        double temp = 37.5 + diaryCount - ((days - diaryDayCount) * 0.5);
        log.info(
                "================= days : {}, diaryDayCount : {}, diaryCount : {}, temp : {} =====================",
                days, diaryDayCount, diaryCount, temp);
        return temp;
    }
}
