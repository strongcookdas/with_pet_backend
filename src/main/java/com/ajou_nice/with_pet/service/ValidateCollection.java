package com.ajou_nice.with_pet.service;


import com.ajou_nice.with_pet.domain.entity.Category;
import com.ajou_nice.with_pet.domain.entity.ChatRoom;
import com.ajou_nice.with_pet.domain.entity.CriticalService;
import com.ajou_nice.with_pet.domain.entity.Diary;
import com.ajou_nice.with_pet.domain.entity.Dog;
import com.ajou_nice.with_pet.domain.entity.Party;
import com.ajou_nice.with_pet.domain.entity.PetSitter;
import com.ajou_nice.with_pet.domain.entity.Reservation;
import com.ajou_nice.with_pet.domain.entity.User;
import com.ajou_nice.with_pet.domain.entity.WithPetService;
import com.ajou_nice.with_pet.exception.AppException;
import com.ajou_nice.with_pet.exception.ErrorCode;
import com.ajou_nice.with_pet.repository.CategoryRepository;
import com.ajou_nice.with_pet.repository.ChatRoomRepository;
import com.ajou_nice.with_pet.repository.CriticalServiceRepository;
import com.ajou_nice.with_pet.repository.DiaryRepository;
import com.ajou_nice.with_pet.repository.DogRepository;
import com.ajou_nice.with_pet.repository.PartyRepository;
import com.ajou_nice.with_pet.repository.PetSitterRepository;
import com.ajou_nice.with_pet.repository.ReservationRepository;
import com.ajou_nice.with_pet.repository.UserRepository;
import com.ajou_nice.with_pet.repository.WithPetServiceRepository;
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
	public User userValidation(String userId){
		User findUser = userRepository.findById(userId).orElseThrow(()->{
			throw new AppException(ErrorCode.USER_NOT_FOUND, ErrorCode.USER_NOT_FOUND.getMessage());
		});

		return findUser;
	}

	// 유저 검증 by PK(id)
	public User userValidation(Long id){
		User findUser = userRepository.findByUserId(id).orElseThrow(()->{
			throw new AppException(ErrorCode.USER_NOT_FOUND, ErrorCode.USER_NOT_FOUND.getMessage());
		});
		return findUser;
	}

	// 위드펫 서비스 검증
	public WithPetService withPetServiceValidation(Long serviceId){
		WithPetService withPetService = withPetServiceRepository.findById(serviceId)
				.orElseThrow(()->{
					throw new AppException(ErrorCode.WITH_PET_SERVICE_NOT_FOUND, ErrorCode.WITH_PET_SERVICE_NOT_FOUND.getMessage());
				});

		return withPetService;
	}

	// 필수 서비스 검증 (소형견, 중형견, 대형견)
	public CriticalService criticalServiceValidation(Long serviceId){
		CriticalService criticalService = criticalServiceRepository.findById(serviceId)
				.orElseThrow(()->{
					throw new AppException(ErrorCode.CRITICAL_SERVICE_NOT_FOUND, ErrorCode.CRITICAL_SERVICE_NOT_FOUND.getMessage());
				});

		return criticalService;
	}

	// 펫시터 유효검증 by User
	public PetSitter petSitterValidationByUser(User user){
		PetSitter petSitter = petSitterRepository.findByUser(user).orElseThrow(() -> {
			throw new AppException(ErrorCode.PETSITTER_NOT_FOUND,
					ErrorCode.PETSITTER_NOT_FOUND.getMessage());
		});

		return petSitter;
	}

	// 펫시터 검증 By PK (petSitterId)
	public PetSitter petSitterValidation(Long petSitterId){
		PetSitter findPetSitter = petSitterRepository.findById(petSitterId).orElseThrow(()->{
			throw new AppException(ErrorCode.PETSITTER_NOT_FOUND, ErrorCode.PETSITTER_NOT_FOUND.getMessage());
		});
		return findPetSitter;
	}

	// 파티(그룹) 검증
	public Party partyValidation(Long partyId){
		Party party = partyRepository.findById(partyId).orElseThrow(() -> {
			throw new AppException(ErrorCode.GROUP_NOT_FOUND,
					ErrorCode.GROUP_NOT_FOUND.getMessage());
		});
		return party;
	}

	// 반려견 검증
	public Dog dogValidation(Long dogId){
		Dog dog = dogRepository.findById(dogId).orElseThrow(() -> {
			throw new AppException(ErrorCode.DOG_NOT_FOUND, ErrorCode.DOG_NOT_FOUND.getMessage());
		});
		return dog;
	}

	// 채팅룸 검증
	public ChatRoom chatRoomValidation(Long chatRoomId){
		ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId).orElseThrow(()->{
			throw new AppException(ErrorCode.CHATROOM_NOT_FOUND, ErrorCode.CHATROOM_NOT_FOUND.getMessage());
		});
		return chatRoom;
	}

	// 예약 검증
	public Reservation reservationValidation(Long reservationId){
		Reservation reservation = reservationRepository.findById(reservationId).orElseThrow(()->{
			throw new AppException(ErrorCode.RESERVATION_NOT_FOUND, ErrorCode.RESERVATION_NOT_FOUND.getMessage());
		});

		return reservation;
	}

	// 카테고리(일지) 검증
	public Category categoryValidation(Long categoryId){

		Category category = categoryRepository.findById(categoryId)
				.orElseThrow(() -> {
					throw new AppException(ErrorCode.GROUP_NOT_FOUND,
							ErrorCode.CATEGORY_NOT_FOUND.getMessage());
				});
		return category;
	}

	// 일지 검증
	public Diary diaryValidation(Long diaryId){

		Diary diary = diaryRepository.findById(diaryId).orElseThrow(() -> {
			throw new AppException(ErrorCode.DIARY_NOT_FOUND,
					ErrorCode.DIARY_NOT_FOUND.getMessage());
		});
		return diary;
	}


}
