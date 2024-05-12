package com.ajou_nice.with_pet.service;


import com.ajou_nice.with_pet.critical_service.repository.CriticalServiceRepository;
import com.ajou_nice.with_pet.domain.entity.*;
import com.ajou_nice.with_pet.exception.AppException;
import com.ajou_nice.with_pet.exception.ErrorCode;
import com.ajou_nice.with_pet.petsitter.model.entity.PetSitter;
import com.ajou_nice.with_pet.petsitter.repository.PetSitterRepository;
import com.ajou_nice.with_pet.repository.*;
import com.ajou_nice.with_pet.withpet_service.repository.WithPetServiceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ValidateCollection {

    private final UserRepository userRepository;
    private final CriticalServiceRepository criticalServiceRepository;
    private final WithPetServiceRepository withPetServiceRepository;
    private final PetSitterRepository petSitterRepository;
    private final PartyRepository partyRepository;
    private final DogRepository dogRepository;
    private final ReservationRepository reservationRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final CategoryRepository categoryRepository;
    private final DiaryRepository diaryRepository;

    // 유저 검증 by UserId(with token)
    public User userValidationById(String email) {
        User findUser = userRepository.findByEmail(email).orElseThrow(() -> {
            throw new AppException(ErrorCode.USER_NOT_FOUND, ErrorCode.USER_NOT_FOUND.getMessage());
        });

        return findUser;
    }

    // 유저 검증 by PK(id)
    public User userValidationById(Long id) {
        User findUser = userRepository.findById(id).orElseThrow(() -> {
            throw new AppException(ErrorCode.USER_NOT_FOUND, ErrorCode.USER_NOT_FOUND.getMessage());
        });
        return findUser;
    }

    // 유저 검증 by email
    public User userValidationByEmail(String email) {
        User findUser = userRepository.findByEmail(email).orElseThrow(() -> {
            throw new AppException(ErrorCode.USER_NOT_FOUND, ErrorCode.USER_NOT_FOUND.getMessage());
        });

        return findUser;
    }

    // 펫시터 유효검증 by User
    public PetSitter petSitterValidationByUser(User user) {
        PetSitter petSitter = petSitterRepository.findByUser(user).orElseThrow(() -> {
            throw new AppException(ErrorCode.PETSITTER_NOT_FOUND,
                    ErrorCode.PETSITTER_NOT_FOUND.getMessage());
        });

        return petSitter;
    }

    // 펫시터 검증 By PK (petSitterId)
    public PetSitter petSitterValidation(Long petSitterId) {
        PetSitter findPetSitter = petSitterRepository.findById(petSitterId).orElseThrow(() -> {
            throw new AppException(ErrorCode.PETSITTER_NOT_FOUND, ErrorCode.PETSITTER_NOT_FOUND.getMessage());
        });
        return findPetSitter;
    }

    // 파티(그룹) 검증
    public Party partyValidation(Long partyId) {
        Party party = partyRepository.findById(partyId).orElseThrow(() -> {
            throw new AppException(ErrorCode.GROUP_NOT_FOUND,
                    ErrorCode.GROUP_NOT_FOUND.getMessage());
        });
        return party;
    }

    // 반려견 검증
    public Dog dogValidation(Long dogId) {
        Dog dog = dogRepository.findById(dogId).orElseThrow(() -> {
            throw new AppException(ErrorCode.DOG_NOT_FOUND, ErrorCode.DOG_NOT_FOUND.getMessage());
        });
        return dog;
    }

    // 채팅룸 검증
    public ChatRoom chatRoomValidation(Long chatRoomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId).orElseThrow(() -> {
            throw new AppException(ErrorCode.CHATROOM_NOT_FOUND, ErrorCode.CHATROOM_NOT_FOUND.getMessage());
        });
        return chatRoom;
    }

    // 예약 검증
    public Reservation reservationValidation(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId).orElseThrow(() -> {
            throw new AppException(ErrorCode.RESERVATION_NOT_FOUND, ErrorCode.RESERVATION_NOT_FOUND.getMessage());
        });

        return reservation;
    }

    // 카테고리(일지) 검증
    public Category categoryValidation(Long categoryId) {

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> {
                    throw new AppException(ErrorCode.GROUP_NOT_FOUND,
                            ErrorCode.CATEGORY_NOT_FOUND.getMessage());
                });
        return category;
    }

    // 일지 검증
    public Diary diaryValidation(Long diaryId) {

        Diary diary = diaryRepository.findById(diaryId).orElseThrow(() -> {
            throw new AppException(ErrorCode.DIARY_NOT_FOUND,
                    ErrorCode.DIARY_NOT_FOUND.getMessage());
        });
        return diary;
    }


}
