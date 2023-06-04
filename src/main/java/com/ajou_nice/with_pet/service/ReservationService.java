package com.ajou_nice.with_pet.service;

import com.ajou_nice.with_pet.domain.dto.dog.DogSocializationRequest;
import com.ajou_nice.with_pet.domain.dto.reservation.ReservationCreateResponse;
import com.ajou_nice.with_pet.domain.dto.reservation.ReservationDetailResponse;
import com.ajou_nice.with_pet.domain.dto.reservation.ReservationRequest;
import com.ajou_nice.with_pet.domain.dto.reservation.ReservationResponse;
import com.ajou_nice.with_pet.domain.entity.Dog;
import com.ajou_nice.with_pet.domain.entity.PetSitter;
import com.ajou_nice.with_pet.domain.entity.PetSitterCriticalService;
import com.ajou_nice.with_pet.domain.entity.PetSitterWithPetService;
import com.ajou_nice.with_pet.domain.entity.Reservation;
import com.ajou_nice.with_pet.domain.entity.ReservationPetSitterService;
import com.ajou_nice.with_pet.domain.entity.User;
import com.ajou_nice.with_pet.enums.ReservationStatus;
import com.ajou_nice.with_pet.exception.AppException;
import com.ajou_nice.with_pet.exception.ErrorCode;
import com.ajou_nice.with_pet.repository.DogRepository;
import com.ajou_nice.with_pet.repository.PetSitterCriticalServiceRepository;
import com.ajou_nice.with_pet.repository.PetSitterRepository;
import com.ajou_nice.with_pet.repository.PetSitterServiceRepository;
import com.ajou_nice.with_pet.repository.ReservationPetSitterServiceRepository;
import com.ajou_nice.with_pet.repository.ReservationRepository;
import com.ajou_nice.with_pet.repository.UserRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final DogRepository dogRepository;
    private final PetSitterRepository petSitterRepository;
    private final PetSitterCriticalServiceRepository petSitterCriticalServiceRepository;
    private final PetSitterServiceRepository serviceRepository;
    private final ReservationPetSitterServiceRepository reservationServiceRepository;

    private final List<ReservationStatus> reservationStatuses = new ArrayList<>(
            List.of(ReservationStatus.USE, ReservationStatus.APPROVAL,
                    ReservationStatus.WAIT));

    //리팩토링이 절대적으로 필요해 보인다......
    @Transactional
    public ReservationCreateResponse createReservation(String userId, ReservationRequest reservationRequest) {
        int cost = 0;
        User user = userRepository.findById(userId).orElseThrow(() -> {
            throw new AppException(ErrorCode.USER_NOT_FOUND, ErrorCode.USER_NOT_FOUND.getMessage());
        });
        Dog dog = dogRepository.findById(reservationRequest.getDogId()).orElseThrow(() -> {
            throw new AppException(ErrorCode.DOG_NOT_FOUND, ErrorCode.DOG_NOT_FOUND.getMessage());
        });

        //반려견 예약 유효 체크
        isDuplicatedReservation(reservationRequest.getCheckIn(), reservationRequest.getCheckOut(),
                dog, reservationStatuses);

        //펫시터 유효 체크
        PetSitter petSitter = petSitterRepository.findById(reservationRequest.getPetsitterId())
                .orElseThrow(() -> {
                    throw new AppException(ErrorCode.PETSITTER_NOT_FOUND,
                            ErrorCode.PETSITTER_NOT_FOUND.getMessage());
                });

        //펫시터 예약 유효 체크
        isConflictReservation(reservationRequest.getCheckIn(), reservationRequest.getCheckOut(),
                petSitter, reservationStatuses);

        //소형견, 중형견, 대형견 옵션 선택
        PetSitterCriticalService criticalService = petSitterCriticalServiceRepository.findByPetSitterAndCriticalService_ServiceName(
                petSitter, dog.getDogSize().name()).orElseThrow(() -> {
            throw new AppException(ErrorCode.PETSITTER_SERVICE_NOT_FOUND, "해당 옵션이 존재하지 않습니다.");
        });

        cost += criticalService.getPrice();

        List<PetSitterWithPetService> withPetServices = serviceRepository.findAllById(
                reservationRequest.getOptionId());

        //시간에 따라 요금 계산 (날짜 * 필수 옵션 가격)
        Period diff = Period.between(reservationRequest.getCheckIn().toLocalDate(),
                reservationRequest.getCheckOut().toLocalDate());
        cost *= diff.getDays();
        Reservation reservation = reservationRepository.save(
                Reservation.of(reservationRequest, user, dog, petSitter, criticalService));

        List<ReservationPetSitterService> reservationServices = new ArrayList<>();

        for (PetSitterWithPetService withPetService : withPetServices) {
            if (!withPetService.getPetSitter().getId().equals(petSitter.getId())) {
                throw new AppException(ErrorCode.PETSITTER_SERVICE_NOT_FOUND,
                        "해당 펫시터가 제공하는 서비스가 아닙니다.");
            }
            ReservationPetSitterService reservationService =
                    ReservationPetSitterService.of(reservation, withPetService);
            reservationServices.add(reservationService);
            cost += withPetService.getPrice();
        }

        reservationServiceRepository.saveAll(reservationServices);
        reservation.updateReservationServices(reservationServices);
        reservation.updateTotalPrice(cost);

        return ReservationCreateResponse.of(reservation);
    }

    private void isDuplicatedReservation(LocalDateTime checkIn, LocalDateTime checkOut, Dog dog,
            List<ReservationStatus> statuses) {
        if (reservationRepository.existsByCheckInBetweenAndDogAndReservationStatusIn(checkIn,
                checkOut, dog, statuses)) {
            throw new AppException(ErrorCode.DUPLICATED_RESERVATION,
                    ErrorCode.DUPLICATED_RESERVATION.getMessage());
        }

        if (reservationRepository.existsByCheckOutBetweenAndDogAndReservationStatusIn(checkIn,
                checkOut, dog, statuses)) {
            throw new AppException(ErrorCode.DUPLICATED_RESERVATION,
                    ErrorCode.DUPLICATED_RESERVATION.getMessage());
        }
    }

    //펫시터 입장에서 validation
    private void isConflictReservation(LocalDateTime checkIn, LocalDateTime checkOut,
            PetSitter petSitter, List<ReservationStatus> reservationStatuses) {
        if (reservationRepository.existsByCheckInBetweenAndPetSitterAndReservationStatusIn(checkIn,
                checkOut, petSitter, reservationStatuses)) {
            throw new AppException(ErrorCode.RESERVATION_CONFLICT,
                    ErrorCode.RESERVATION_CONFLICT.getMessage());
        }

        if (reservationRepository.existsByCheckOutBetweenAndPetSitterAndReservationStatusIn(checkIn,
                checkOut, petSitter, reservationStatuses)) {
            throw new AppException(ErrorCode.RESERVATION_CONFLICT,
                    ErrorCode.RESERVATION_CONFLICT.getMessage());
        }
    }


    @Transactional
    // 펫시터가 완료된 예약의 반려견의 사회화 온도 평가
    public void modifyDogTemp(String userId, Long reservationId,
            DogSocializationRequest dogSocializationRequest) {

        //reservation
        Reservation reservation = reservationRepository.findById(reservationId).orElseThrow(() -> {
            throw new AppException(ErrorCode.RESERVATION_NOT_FOUND,
                    ErrorCode.RESERVATION_NOT_FOUND.getMessage());
        });

        //user가 펫시터인지 검증
        User user = userRepository.findById(userId).orElseThrow(() -> {
            throw new AppException(ErrorCode.USER_NOT_FOUND, ErrorCode.USER_NOT_FOUND.getMessage());
        });

        //예약의 펫시터가 아니라면
        if (!reservation.getPetSitter().getUser().equals(user)) {
            throw new AppException(ErrorCode.PETSITTER_NOT_FOUND,
                    ErrorCode.PETSITTER_NOT_FOUND.getMessage());
        }

        // 예약의 펫시터가 맞다면
        Dog dog = dogRepository.findById(reservation.getDog().getDogId()).orElseThrow(() -> {
            throw new AppException(ErrorCode.DOG_NOT_FOUND, ErrorCode.DOG_NOT_FOUND.getMessage());
        });

        float score = (dogSocializationRequest.getQ1() + dogSocializationRequest.getQ2()
                + dogSocializationRequest.getQ3() +
                dogSocializationRequest.getQ4() + dogSocializationRequest.getQ5()) / 5;

        if (score < 2.5) {
            score = -score;
            dog.updateSocializationTemperature(score);
        } else {
            dog.updateSocializationTemperature(score);
        }
    }

    // 이 코드 중 왜 reservationStatuses로 가져오는지 확인 필요 -> approval하나로만 생각하면 된다.
    public List<String> getUnavailableDates(String userId, Long petsitterId, String month) {

        List<String> unavailableDates = new ArrayList<>();

        PetSitter petSitter = petSitterRepository.findById(petsitterId).orElseThrow(() -> {
            throw new AppException(ErrorCode.PETSITTER_NOT_FOUND,
                    ErrorCode.PETSITTER_NOT_FOUND.getMessage());
        });

        List<Reservation> reservations = reservationRepository.findAllByPetsitterAndMonthAndStatus(
                petSitter,
                LocalDate.parse(month + "-01"), reservationStatuses);

        for (Reservation reservation : reservations) {
            unavailableDates.addAll(getDateRange(reservation.getCheckIn().toLocalDate(),
                    reservation.getCheckOut().toLocalDate()));
        }
        return unavailableDates;
    }

    private List<String> getDateRange(LocalDate startDate, LocalDate endDate) {
        List<String> dateRange = new ArrayList<>();

        while (!startDate.isAfter(endDate)) {
            dateRange.add(startDate.toString());
            startDate = startDate.plusDays(1);
        }

        return dateRange;
    }

    /*
      예약에 대한 상태가 많아서 승인/거절에 대한 메소드를 분리한 것이 아닌 그냥 상태를 수정하는 메소드를 선언
      이게 맞는건지는 고민이 필요....
     */
    @Transactional
    public ReservationDetailResponse approveReservation(String userId, Long reservationId, String status) {

        //상태 변경 값이 잘 들어왔는지 유효 체크
        if(!status.equals(ReservationStatus.APPROVAL.toString())){
            throw new AppException(ErrorCode.BAD_REQUEST_RESERVATION_STATSUS, ErrorCode.BAD_REQUEST_RESERVATION_STATSUS.getMessage());
        }

        User user = userRepository.findById(userId).orElseThrow(() -> {
            throw new AppException(ErrorCode.USER_NOT_FOUND, ErrorCode.USER_NOT_FOUND.getMessage());
        });

        Reservation reservation = reservationRepository.findById(reservationId).orElseThrow(() -> {
            throw new AppException(ErrorCode.RESERVATION_NOT_FOUND,
                    ErrorCode.RESERVATION_NOT_FOUND.getMessage());
        });

        // 이 검증을 굳이 해야할 필요가 있나?
        if (!reservation.getPetSitter().getUser().getId().equals(userId)) {
            throw new AppException(ErrorCode.UNAUTHORIZED_RESERVATION,
                    ErrorCode.UNAUTHORIZED_RESERVATION.getMessage());
        }

        reservation.updateStatus(status);
        return ReservationDetailResponse.of(reservation);
    }

    /*
    예약 내역 페이지(유저) -> 결제를 위해 reservation_status가 approval인 예약 리스트 불러오기
     */
    public List<ReservationResponse> showApprovalReservation(String userId){

        User user = userRepository.findById(userId).orElseThrow(() -> {
            throw new AppException(ErrorCode.USER_NOT_FOUND, ErrorCode.USER_NOT_FOUND.getMessage());
        });

        List<Reservation> reservations = reservationRepository.findReservationByStatus(userId,
                String.valueOf(ReservationStatus.APPROVAL));

        return reservations.stream().map(ReservationResponse::of).collect(Collectors.toList());
    }

    //펫시터 입장에서 ReservationList를 불러올때 reservationStatus가 payed인 List만 불러오는게 좋을 것 같다.
    public List<ReservationResponse> getMonthlyReservations(String userId, String month) {
        User user = userRepository.findById(userId).orElseThrow(() -> {
            throw new AppException(ErrorCode.USER_NOT_FOUND, ErrorCode.USER_NOT_FOUND.getMessage());
        });

        PetSitter petSitter = petSitterRepository.findByUser(user).orElseThrow(() -> {
            throw new AppException(ErrorCode.PETSITTER_NOT_FOUND,
                    ErrorCode.PETSITTER_NOT_FOUND.getMessage());
        });

        List<Reservation> reservations = reservationRepository.findAllByPetsitterAndMonth(petSitter,
                LocalDate.parse(month + "-01"));

        return reservations.stream().map(ReservationResponse::of).collect(Collectors.toList());
    }
}
