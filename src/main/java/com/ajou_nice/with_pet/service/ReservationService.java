package com.ajou_nice.with_pet.service;

import com.ajou_nice.with_pet.domain.dto.dog.DogSocializationRequest;
import com.ajou_nice.with_pet.domain.dto.reservation.ReservationRequest;
import com.ajou_nice.with_pet.domain.dto.reservation.ReservationResponse;
import com.ajou_nice.with_pet.domain.entity.Dog;
import com.ajou_nice.with_pet.domain.entity.PetSitter;
import com.ajou_nice.with_pet.domain.entity.PetSitterApplicant;
import com.ajou_nice.with_pet.domain.entity.PetSitterCriticalService;
import com.ajou_nice.with_pet.domain.entity.PetSitterWithPetService;
import com.ajou_nice.with_pet.domain.entity.Reservation;
import com.ajou_nice.with_pet.domain.entity.ReservationPetsitterService;
import com.ajou_nice.with_pet.domain.entity.User;
import com.ajou_nice.with_pet.enums.ReservationStatus;
import com.ajou_nice.with_pet.exception.AppException;
import com.ajou_nice.with_pet.exception.ErrorCode;
import com.ajou_nice.with_pet.repository.DogRepository;
import com.ajou_nice.with_pet.repository.PetSitterApplicantRepository;
import com.ajou_nice.with_pet.repository.PetSitterCriticalServiceRepository;
import com.ajou_nice.with_pet.repository.PetSitterRepository;
import com.ajou_nice.with_pet.repository.PetSitterServiceRepository;
import com.ajou_nice.with_pet.repository.ReservationPetsitterServiceRepository;
import com.ajou_nice.with_pet.repository.ReservationRepository;
import com.ajou_nice.with_pet.repository.UserRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
    private final PetSitterApplicantRepository applicantRepository;
    private final PetSitterCriticalServiceRepository petSitterCriticalServiceRepository;
    private final PetSitterServiceRepository serviceRepository;
    private final ReservationPetsitterServiceRepository reservationServiceRepository;

    private final List<ReservationStatus> reservationStatuses = new ArrayList<>(
            List.of(ReservationStatus.USE, ReservationStatus.APPROVAL,
                    ReservationStatus.WAIT));

    //리팩토링이 절대적으로 필요해 보인다......
    public void createReservation(String userId, ReservationRequest reservationRequest) {
        User user = userRepository.findById(userId).orElseThrow(() -> {
            throw new AppException(ErrorCode.USER_NOT_FOUND, ErrorCode.USER_NOT_FOUND.getMessage());
        });
        Dog dog = dogRepository.findById(reservationRequest.getDogId()).orElseThrow(() -> {
            throw new AppException(ErrorCode.DOG_NOT_FOUND, ErrorCode.DOG_NOT_FOUND.getMessage());
        });

        //반려견 예약 유효 체크
        isDuplicatedReservation(reservationRequest.getCheckIn(), reservationRequest.getCheckOut(),
                dog, reservationStatuses);

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

        List<PetSitterWithPetService> withPetServices = serviceRepository.findAllById(
                reservationRequest.getOptionId());

        Reservation reservation = reservationRepository.save(
                Reservation.of(reservationRequest, user, dog, petSitter, criticalService));

        for (PetSitterWithPetService withPetService : withPetServices) {
            if (!withPetService.getPetSitter().getId().equals(petSitter.getId())) {
                throw new AppException(ErrorCode.PETSITTER_SERVICE_NOT_FOUND,
                        "해당 펫시터가 제공하는 서비스가 아닙니다.");
            }
            reservationServiceRepository.save(
                    ReservationPetsitterService.of(reservation, withPetService));
        }

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
        if (!reservation.getPetSitter().getApplicant().getUser().equals(user)) {
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

    /*예약에 대한 상태가 많아서 승인/거절에 대한 메소드를 분리한 것이 아닌 그냥 상태를 수정하는 메소드를 선언
      이게 맞는건지는 고민이 필요....
     */
    @Transactional
    public void approveReservation(String userId, Long reservationId, String status) {

        User user = userRepository.findById(userId).orElseThrow(() -> {
            throw new AppException(ErrorCode.USER_NOT_FOUND, ErrorCode.USER_NOT_FOUND.getMessage());
        });

        Reservation reservation = reservationRepository.findById(reservationId).orElseThrow(() -> {
            throw new AppException(ErrorCode.RESERVATION_NOT_FOUND,
                    ErrorCode.RESERVATION_NOT_FOUND.getMessage());
        });

        if (!reservation.getPetSitter().getApplicant().getUser().getId().equals(userId)) {
            throw new AppException(ErrorCode.UNAUTHORIZED_RESERVATION,
                    ErrorCode.UNAUTHORIZED_RESERVATION.getMessage());
        }

        reservation.updateStatus(status);
    }

    public List<ReservationResponse> getMonthlyReservations(String userId, String month) {
        User user = userRepository.findById(userId).orElseThrow(() -> {
            throw new AppException(ErrorCode.USER_NOT_FOUND, ErrorCode.USER_NOT_FOUND.getMessage());
        });

        PetSitterApplicant applicant = applicantRepository.findByUser(user).orElseThrow(() -> {
            throw new AppException(ErrorCode.APPLICANT_NOT_FOUND,
                    ErrorCode.APPLICANT_NOT_FOUND.getMessage());
        });

        PetSitter petSitter = petSitterRepository.findByApplicant(applicant).orElseThrow(() -> {
            throw new AppException(ErrorCode.PETSITTER_NOT_FOUND,
                    ErrorCode.PETSITTER_NOT_FOUND.getMessage());
        });

        List<Reservation> reservations = reservationRepository.findAllByPetsitterAndMonth(petSitter,
                LocalDate.parse(month + "-01"));

        return reservations.stream().map(ReservationResponse::of).collect(Collectors.toList());
    }
}
