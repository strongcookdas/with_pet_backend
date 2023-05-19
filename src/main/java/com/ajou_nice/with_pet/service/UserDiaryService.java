package com.ajou_nice.with_pet.service;

import com.ajou_nice.with_pet.domain.dto.diary.DiaryRequest;
import com.ajou_nice.with_pet.domain.dto.diary.user.UserDiaryMonthResponse;
import com.ajou_nice.with_pet.domain.dto.diary.user.UserDiaryResponse;
import com.ajou_nice.with_pet.domain.entity.Category;
import com.ajou_nice.with_pet.domain.entity.Dog;
import com.ajou_nice.with_pet.domain.entity.User;
import com.ajou_nice.with_pet.domain.entity.Diary;
import com.ajou_nice.with_pet.exception.AppException;
import com.ajou_nice.with_pet.exception.ErrorCode;
import com.ajou_nice.with_pet.repository.CategoryRepository;
import com.ajou_nice.with_pet.repository.DogRepository;
import com.ajou_nice.with_pet.repository.DiaryRepository;
import com.ajou_nice.with_pet.repository.UserPartyRepository;
import com.ajou_nice.with_pet.repository.UserRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserDiaryService {

    private final UserRepository userRepository;
    private final DogRepository dogRepository;
    private final UserPartyRepository userPartyRepository;
    private final DiaryRepository userDiaryRepository;
    private final CategoryRepository categoryRepository;


    public UserDiaryResponse writeUserDiary(String userId, DiaryRequest diaryRequest) {
        //유저 체크
        User user = userRepository.findById(userId).orElseThrow(() -> {
            throw new AppException(ErrorCode.USER_NOT_FOUND, ErrorCode.USER_NOT_FOUND.getMessage());
        });
        //반려견 체크
        Dog dog = dogRepository.findById(diaryRequest.getDogId()).orElseThrow(() -> {
            throw new AppException(ErrorCode.DOG_NOT_FOUND, ErrorCode.DOG_NOT_FOUND.getMessage());
        });
        //반려견 그룹 유저 존재 체크
        if (!userPartyRepository.existsUserPartyByUserAndParty(user, dog.getParty())) {
            throw new AppException(ErrorCode.GROUP_NOT_FOUND, "글 작성 권한이 없습니다.");
        }
        //카테고리 체크
        Category category = categoryRepository.findById(diaryRequest.getCategoryId())
                .orElseThrow(() -> {
                    throw new AppException(ErrorCode.CATEGORY_NOT_FOUND,
                            ErrorCode.CATEGORY_NOT_FOUND.getMessage());
                });

        Diary diary = userDiaryRepository.save(
                Diary.of(diaryRequest, dog, user, category));
        return UserDiaryResponse.of(diary);
    }

    @Transactional
    public UserDiaryResponse updateUserDiary(String userId, DiaryRequest diaryRequest,
            Long diaryId) {
        //유저 체크
        User user = userRepository.findById(userId).orElseThrow(() -> {
            throw new AppException(ErrorCode.USER_NOT_FOUND, ErrorCode.USER_NOT_FOUND.getMessage());
        });
        //일지 체크
        Diary diary = userDiaryRepository.findById(diaryId).orElseThrow(() -> {
            throw new AppException(ErrorCode.DIARY_NOT_FOUND,
                    ErrorCode.DIARY_NOT_FOUND.getMessage());
        });
        //반려견 체크
        Dog dog = dogRepository.findById(diaryRequest.getDogId()).orElseThrow(() -> {
            throw new AppException(ErrorCode.DOG_NOT_FOUND, ErrorCode.DOG_NOT_FOUND.getMessage());
        });
        //카테고리 체크
        Category category = categoryRepository.findById(diaryRequest.getCategoryId())
                .orElseThrow(() -> {
                    throw new AppException(ErrorCode.CATEGORY_NOT_FOUND,
                            ErrorCode.CATEGORY_NOT_FOUND.getMessage());
                });

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
            String month) {

        //유저 체크
        User user = userRepository.findById(userId).orElseThrow(() -> {
            throw new AppException(ErrorCode.USER_NOT_FOUND, ErrorCode.USER_NOT_FOUND.getMessage());
        });

        List<Diary> userDiaries = userDiaryRepository.findByMonthDate(user.getUserId(),
                dogId, categoryId, LocalDate.parse(month + "-01"));
        return userDiaries.stream().map(UserDiaryMonthResponse::of).collect(Collectors.toList());
    }

    public List<UserDiaryResponse> getUserDayDiary(String userId, Long dogId, Long categoryId,
            String day) {
        //유저체크
        User user = userRepository.findById(userId).orElseThrow(() -> {
            throw new AppException(ErrorCode.USER_NOT_FOUND, ErrorCode.USER_NOT_FOUND.getMessage());
        });

        List<Diary> userDiaries = userDiaryRepository.findByDayDate(user.getUserId(), dogId,
                categoryId, LocalDate.parse(day));
        return userDiaries.stream().map(UserDiaryResponse::of).collect(Collectors.toList());
    }
}
