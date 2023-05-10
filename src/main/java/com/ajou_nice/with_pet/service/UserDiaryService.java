package com.ajou_nice.with_pet.service;

import com.ajou_nice.with_pet.domain.dto.diary.DiaryRequest;
import com.ajou_nice.with_pet.domain.dto.diary.user.UserDiaryResponse;
import com.ajou_nice.with_pet.domain.entity.Dog;
import com.ajou_nice.with_pet.domain.entity.User;
import com.ajou_nice.with_pet.domain.entity.UserDiary;
import com.ajou_nice.with_pet.exception.AppException;
import com.ajou_nice.with_pet.exception.ErrorCode;
import com.ajou_nice.with_pet.repository.DogRepository;
import com.ajou_nice.with_pet.repository.UserDiaryRepository;
import com.ajou_nice.with_pet.repository.UserPartyRepository;
import com.ajou_nice.with_pet.repository.UserRepository;
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
    private final UserDiaryRepository userDiaryRepository;


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

        UserDiary userDiary = userDiaryRepository.save(UserDiary.of(diaryRequest, dog, user));
        return UserDiaryResponse.of(userDiary);
    }

    @Transactional
    public UserDiaryResponse updateUserDiary(String userId, DiaryRequest diaryRequest,
            Long diaryId) {
        //유저 체크
        User user = userRepository.findById(userId).orElseThrow(() -> {
            throw new AppException(ErrorCode.USER_NOT_FOUND, ErrorCode.USER_NOT_FOUND.getMessage());
        });
        //일지 체크
        UserDiary userDiary = userDiaryRepository.findById(diaryId).orElseThrow(() -> {
            throw new AppException(ErrorCode.DIARY_NOT_FOUND,
                    ErrorCode.DIARY_NOT_FOUND.getMessage());
        });
        //반려견 체크
        Dog dog = dogRepository.findById(diaryRequest.getDogId()).orElseThrow(() -> {
            throw new AppException(ErrorCode.DOG_NOT_FOUND, ErrorCode.DOG_NOT_FOUND.getMessage());
        });
        //작성자 비교
        if (!user.equals(userDiary.getUser())) {
            throw new AppException(ErrorCode.INVALID_PERMISSION, "일지를 수정할 권한이 없습니다.");
        }
        //수정
        userDiary.update(diaryRequest, dog);
        return UserDiaryResponse.of(userDiary);
    }

    //일지 조회 고민 좀 해봐야 할 듯
//    public String getUserDiary(String userId, Long dogId) {
//
//        //유저 체크
//        User user = userRepository.findById(userId).orElseThrow(() -> {
//            throw new AppException(ErrorCode.USER_NOT_FOUND, ErrorCode.USER_NOT_FOUND.getMessage());
//        });
//        //반려견 체크
//        Dog dog = dogRepository.findById(dogId).orElseThrow(() -> {
//            throw new AppException(ErrorCode.DOG_NOT_FOUND, ErrorCode.DOG_NOT_FOUND.getMessage());
//        });
//        //반려견 그룹 유저 존재 체크
//        if (!userPartyRepository.existsUserPartyByUserAndParty(user, dog.getParty())) {
//            throw new AppException(ErrorCode.GROUP_NOT_FOUND, "글 작성 권한이 없습니다.");
//        }
//        return null;
//    }
}
