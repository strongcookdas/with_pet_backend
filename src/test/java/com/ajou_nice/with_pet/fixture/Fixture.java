package com.ajou_nice.with_pet.fixture;

import com.ajou_nice.with_pet.domain.entity.Category;
import com.ajou_nice.with_pet.domain.entity.Diary;
import com.ajou_nice.with_pet.domain.entity.Dog;
import com.ajou_nice.with_pet.domain.entity.Party;
import com.ajou_nice.with_pet.domain.entity.PetSitter;
import com.ajou_nice.with_pet.domain.entity.Reservation;
import com.ajou_nice.with_pet.domain.entity.User;
import com.ajou_nice.with_pet.domain.entity.UserParty;
import com.ajou_nice.with_pet.domain.entity.embedded.Address;
import com.ajou_nice.with_pet.dto.critical_service.CriticalServiceRequest;
import com.ajou_nice.with_pet.dto.hashtag.HashTagRequest;
import com.ajou_nice.with_pet.dto.hashtag.HashTagResponse;
import com.ajou_nice.with_pet.dto.house.HouseRequest;
import com.ajou_nice.with_pet.dto.house.HouseResponse;
import com.ajou_nice.with_pet.dto.petsitter_critical_service.PetSitterCriticalServiceCreateResponse;
import com.ajou_nice.with_pet.dto.petsitter_withpet_service.PetSitterWithPetServiceCreateResponse;
import com.ajou_nice.with_pet.dto.withpet_service.ServiceRequest;
import com.ajou_nice.with_pet.enums.DogSize;
import com.ajou_nice.with_pet.enums.ReservationStatus;
import com.ajou_nice.with_pet.enums.UserRole;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class Fixture {

    @Getter
    @NoArgsConstructor
    public static class Request {

        //house
        private HouseRequest houseRequest1 = HouseRequest.builder()
                .houseImg("houseImg1")
                .isRepresentative(true)
                .build();
        private HouseRequest houseRequest2 = HouseRequest.builder()
                .houseImg("houseImg2")
                .isRepresentative(false)
                .build();

        //hashTag
        private HashTagRequest hashTagRequest1 = HashTagRequest.builder()
                .hashTagName("#친절")
                .build();
        private HashTagRequest hashTagRequest2 = HashTagRequest.builder()
                .hashTagName("#배려")
                .build();
        //service
        private ServiceRequest serviceRequest1 = ServiceRequest.builder()
                .serviceId(1L)
                .price(10000)
                .build();
        private ServiceRequest serviceRequest2 = ServiceRequest.builder()
                .serviceId(2L)
                .price(20000)
                .build();
        //criticalService
        private CriticalServiceRequest criticalServiceRequest1 = CriticalServiceRequest.builder()
                .criticalServiceId(1L)
                .price(30000)
                .build();
        private CriticalServiceRequest criticalServiceRequest2 = CriticalServiceRequest.builder()
                .criticalServiceId(2L)
                .price(40000)
                .build();
    }

    @Getter
    @NoArgsConstructor
    public static class Response {

        private HouseResponse house1 = HouseResponse.builder()
                .houseId(1L)
                .houseImg("houseImg1")
                .isRepresentative(true)
                .build();

        private HouseResponse house2 = HouseResponse.builder()
                .houseId(2L)
                .houseImg("houseImg2")
                .isRepresentative(false)
                .build();

        private HashTagResponse hashTag1 = HashTagResponse.builder()
                .hashTagId(1L)
                .hashTagName("#친절")
                .build();

        private HashTagResponse hashTag2 = HashTagResponse.builder()
                .hashTagId(2L)
                .hashTagName("#배려")
                .build();

        private PetSitterCriticalServiceCreateResponse petSitterCriticalServiceCreate1 = PetSitterCriticalServiceCreateResponse.builder()
                .isPetSitterCriticalService(true)
                .petSitterCriticalServiceId(1L)
                .criticalServiceId(1L)
                .criticalServiceName("소형견")
                .criticalServiceIntroduction("소형견 서비스입니다.")
                .criticalServiceImg("image")
                .criticalServicePrice(30000)
                .build();

        private PetSitterCriticalServiceCreateResponse petSitterCriticalServiceCreate2 = PetSitterCriticalServiceCreateResponse.builder()
                .isPetSitterCriticalService(false)
                .petSitterCriticalServiceId(null)
                .criticalServiceId(2L)
                .criticalServiceName("중형견")
                .criticalServiceIntroduction("중형견 서비스입니다.")
                .criticalServiceImg("image")
                .criticalServicePrice(null)
                .build();

        private PetSitterWithPetServiceCreateResponse petSitterService1 = PetSitterWithPetServiceCreateResponse.builder()
                .isPetSitterService(true)
                .petSitterServiceId(1L)
                .serviceId(1L)
                .serviceName("산책")
                .serviceIntroduction("산책 서비스입니다.")
                .serviceImg("image")
                .servicePrice(10000)
                .build();

        private PetSitterWithPetServiceCreateResponse petSitterService2 = PetSitterWithPetServiceCreateResponse.builder()
                .isPetSitterService(false)
                .petSitterServiceId(null)
                .serviceId(2L)
                .serviceName("미용")
                .serviceIntroduction("미용 서비스입니다.")
                .serviceImg("image")
                .servicePrice(20000)
                .build();
    }

    //주소
    private Address address = address = Address.simpleAddressGenerator("123456", "아주로", "팔달관");

    //유저
    private User user1 = User.simpleUserForTest("유저", "testUser1", "password123!",
            "email@gmail.com",
            UserRole.ROLE_USER, "010-0302-0001", address, 0);
    private User user2 = User.simpleUserForTest("유저2", "testUser2", "password123!",
            "email@gmail.com",
            UserRole.ROLE_USER, "010-0302-0002", address, 0);
    private User user3 = User.simpleUserForTest("유저3", "testUser3", "password123!",
            "email@gmail.com",
            UserRole.ROLE_USER, "010-0302-0003", address, 0);

    private User user4 = User.simpleUserForTest("유저4", "testUser4", "password123!",
            "email@gmail.com",
            UserRole.ROLE_PETSITTER, "010-0302-0004", address, 0);

    //파티
    private Party party = Party.of(user1, "party1");

    //반려견
    Dog dog = Dog.simpleDogForTest("흰둥이", "male", party, true, LocalDate.now(), 5.0f, "image",
            "말티즈", "123456789",
            DogSize.소형견);

    //유저파티
    UserParty userParty1 = UserParty.of(user1, party);
    UserParty userParty2 = UserParty.of(user2, party);

    //펫시터
    PetSitter petSitter1 = PetSitter.simplePetSitterForTest(user4.getName(), user4.getPhone(),
            "image", "123456", "아주로", "팔달관", user4);

    //예약
    LocalDateTime checkIn = LocalDateTime.of(2023, 6, 13, 5, 13);
    LocalDateTime checkOut = LocalDateTime.of(2023, 6, 14, 6, 13);
    Reservation reservation1 = Reservation.forSimpleTest(checkIn, checkOut, user2, petSitter1,
            35000,
            ReservationStatus.PAYED, dog);


    //카테고리
    Category category = Category.simpleCategoryForTest("산책");

    //유저 일지
    Diary userDiary = Diary.simpleUserDiaryForTest(category, "산책했어용", "산책산책", user1, dog,
            LocalDate.of(2023, 6, 12), null);

    //펫시터 일지
    Diary petSitterDiary = Diary.simpleUserDiaryForTest(category, "산책", "산책했어용", user4, dog,
            LocalDate.now(), petSitter1);

}
