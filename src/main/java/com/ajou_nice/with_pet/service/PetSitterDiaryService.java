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

    private final UserRepository userRepository;
    private final PetSitterRepository petSitterRepository;
    private final DiaryRepository diaryRepository;
    private final DogRepository dogRepository;
    private final UserPartyRepository userPartyRepository;
    private final CategoryRepository categoryRepository;
    private final NotificationService notificationService;

    public PetSitterDiaryResponse writePetsitterDiary(String userId, DiaryRequest diaryRequest) {
        User user = userRepository.findById(userId).orElseThrow(() -> {
            throw new AppException(ErrorCode.USER_NOT_FOUND, ErrorCode.USER_NOT_FOUND.getMessage());
        });

        PetSitter petSitter = petSitterRepository.findByUser(user).orElseThrow(() -> {
            throw new AppException(ErrorCode.PETSITTER_NOT_FOUND,
                    ErrorCode.PETSITTER_NOT_FOUND.getMessage());
        });
        Dog dog = dogRepository.findById(diaryRequest.getDogId()).orElseThrow(() -> {
            throw new AppException(ErrorCode.DOG_NOT_FOUND, ErrorCode.DOG_NOT_FOUND.getMessage());
        });
        Category category = categoryRepository.findById(diaryRequest.getCategoryId())
                .orElseThrow(() -> {
                    throw new AppException(ErrorCode.GROUP_NOT_FOUND,
                            ErrorCode.CATEGORY_NOT_FOUND.getMessage());
                });

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
        User user = userRepository.findById(userId).orElseThrow(() -> {
            throw new AppException(ErrorCode.USER_NOT_FOUND, ErrorCode.USER_NOT_FOUND.getMessage());
        });
        //일지 체크
        Diary diary = diaryRepository.findById(diaryId).orElseThrow(() -> {
            throw new AppException(ErrorCode.DIARY_NOT_FOUND,
                    ErrorCode.DIARY_NOT_FOUND.getMessage());
        });
        //카테고리 체크
        Category category = categoryRepository.findById(diaryModifyRequest.getCategoryId())
                .orElseThrow(() -> {
                    throw new AppException(ErrorCode.CATEGORY_NOT_FOUND,
                            ErrorCode.CATEGORY_NOT_FOUND.getMessage());
                });

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
        User user = userRepository.findById(userId).orElseThrow(() -> {
            throw new AppException(ErrorCode.USER_NOT_FOUND, ErrorCode.USER_NOT_FOUND.getMessage());
        });
        //펫시터 체크
        PetSitter petSitter = petSitterRepository.findByUser(user).orElseThrow(() -> {
            throw new AppException(ErrorCode.PETSITTER_NOT_FOUND,
                    ErrorCode.PETSITTER_NOT_FOUND.getMessage());
        });
        //반려견 체크
        Dog dog = dogRepository.findById(dogId).orElseThrow(() -> {
            throw new AppException(ErrorCode.DOG_NOT_FOUND, ErrorCode.DOG_NOT_FOUND.getMessage());
        });
        //펫시터가 작성한 해당 반려견에 대한 일지 조회
        log.info(
                "=======================================펫시터가 작성한 반려견 일지 조회 START=======================================");
        List<Diary> diaries = diaryRepository.findAllByPetSitterAndDog(petSitter, dog);
        log.info(
                "=======================================펫시터가 작성한 반려견 일지 조회 END=======================================");
        return PetSitterDiaryListResponse.of(dog, diaries);
    }

    public String deletePetSitterDiary(String userId, Long diaryId) {
        User user = userRepository.findById(userId).orElseThrow(() -> {
            throw new AppException(ErrorCode.USER_NOT_FOUND, ErrorCode.USER_NOT_FOUND.getMessage());
        });

        Diary diary = diaryRepository.findById(diaryId).orElseThrow(() -> {
            throw new AppException(ErrorCode.DIARY_NOT_FOUND, ErrorCode.USER_NOT_FOUND.getMessage());
        });

        if(!diary.getDog().getParty().getUser().getId().equals(user.getId())){
            throw new AppException(ErrorCode.INVALID_PERMISSION, ErrorCode.INVALID_TOKEN.getMessage());
        }

        diaryRepository.delete(diary);
        return "일지가 삭제되었습니다.";
    }
}
