package com.ajou_nice.with_pet.service;

import com.ajou_nice.with_pet.domain.dto.diary.DiaryModifyRequest;
import com.ajou_nice.with_pet.domain.dto.diary.DiaryRequest;
import com.ajou_nice.with_pet.domain.dto.diary.PetSitterDiaryListResponse;
import com.ajou_nice.with_pet.domain.dto.diary.PetSitterDiaryResponse;
import com.ajou_nice.with_pet.domain.entity.Category;
import com.ajou_nice.with_pet.domain.entity.Diary;
import com.ajou_nice.with_pet.domain.entity.Dog;
import com.ajou_nice.with_pet.domain.entity.Notification;
import com.ajou_nice.with_pet.domain.entity.PetSitter;
import com.ajou_nice.with_pet.domain.entity.User;
import com.ajou_nice.with_pet.domain.entity.UserParty;
import com.ajou_nice.with_pet.enums.NotificationType;
import com.ajou_nice.with_pet.exception.AppException;
import com.ajou_nice.with_pet.exception.ErrorCode;
import com.ajou_nice.with_pet.repository.CategoryRepository;
import com.ajou_nice.with_pet.repository.DiaryRepository;
import com.ajou_nice.with_pet.repository.DogRepository;
import com.ajou_nice.with_pet.repository.PetSitterRepository;
import com.ajou_nice.with_pet.repository.UserPartyRepository;
import com.ajou_nice.with_pet.repository.UserRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class PetSitterDiaryService {

    private final DiaryRepository diaryRepository;
    private final UserPartyRepository userPartyRepository;
    private final NotificationService notificationService;

    private final ValidateCollection valid;

    public PetSitterDiaryResponse writePetsitterDiary(String userId, DiaryRequest diaryRequest) {

        User user = valid.userValidation(userId);

        PetSitter petSitter = valid.petSitterValidationByUser(user);

        Dog dog = valid.dogValidation(diaryRequest.getDogId());

        Category category = valid.categoryValidation(diaryRequest.getCategoryId());

        Diary diary = diaryRepository.save(Diary.of(diaryRequest, dog, user, category, petSitter));
        // 그룹원에게 알림
        List<UserParty> userParties = userPartyRepository.findAllByParty(dog.getParty());
        List<Notification> notifications = new ArrayList<>();
        userParties.forEach(u -> {
            Notification notification = Notification.of(
                    petSitter.getPetSitterName() + "펫시터 님이 " + dog.getName() + "의 일지를 작성했습니다.",
                    "http://localhost:3000/calendar",
                    NotificationType.펫시터_일지, u.getUser());
            notifications.add(notification);
            notificationService.send(notification);
            notificationService.sendEmail(notification);
        });
        notificationService.saveAllNotification(notifications);

        return PetSitterDiaryResponse.of(diary);
    }

    @Transactional
    public PetSitterDiaryResponse updatePetSitterDiary(String userId,
            DiaryModifyRequest diaryModifyRequest,
            Long diaryId) {
        //유저 체크
        User user = valid.userValidation(userId);
        //일지 체크
        Diary diary = valid.diaryValidation(diaryId);
        //카테고리 체크
        Category category = valid.categoryValidation(diaryModifyRequest.getCategoryId());

        //작성자 비교
        if (!user.equals(diary.getUser())) {
            throw new AppException(ErrorCode.INVALID_PERMISSION, "일지를 수정할 권한이 없습니다.");
        }
        //수정
        diary.update(diaryModifyRequest, category);
        return PetSitterDiaryResponse.of(diary);
    }

    public PetSitterDiaryListResponse getPetSitterDiaries(String userId, Long dogId) {
        //유저 체크
        User user = valid.userValidation(userId);
        //펫시터 체크
        PetSitter petSitter = valid.petSitterValidationByUser(user);
        //반려견 체크
        Dog dog = valid.dogValidation(dogId);
        //펫시터가 작성한 해당 반려견에 대한 일지 조회
        log.info(
                "=======================================펫시터가 작성한 반려견 일지 조회 START=======================================");
        List<Diary> diaries = diaryRepository.findAllByPetSitterAndDog(petSitter, dog);
        log.info(
                "=======================================펫시터가 작성한 반려견 일지 조회 END=======================================");
        return PetSitterDiaryListResponse.of(dog, diaries);
    }
}
