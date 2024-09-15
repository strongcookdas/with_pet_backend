package com.ajou_nice.with_pet.reservation.service;

import com.ajou_nice.with_pet.critical_service.model.entity.PetSitterCriticalService;
import com.ajou_nice.with_pet.critical_service.repository.PetSitterCriticalServiceRepository;
import com.ajou_nice.with_pet.dog.model.entity.Dog;
import com.ajou_nice.with_pet.dog.service.DogValidationService;
import com.ajou_nice.with_pet.domain.entity.User;
import com.ajou_nice.with_pet.enums.ReservationStatus;
import com.ajou_nice.with_pet.exception.AppException;
import com.ajou_nice.with_pet.exception.ErrorCode;
import com.ajou_nice.with_pet.petsitter.model.entity.PetSitter;
import com.ajou_nice.with_pet.petsitter.service.PetSitterValidationService;
import com.ajou_nice.with_pet.repository.ReservationPetSitterServiceRepository;
import com.ajou_nice.with_pet.reservation.model.dto.ReservationCreateRequest;
import com.ajou_nice.with_pet.reservation.model.dto.UserReservationCreateResponse;
import com.ajou_nice.with_pet.reservation.model.dto.UserReservationGetInfosResponse;
import com.ajou_nice.with_pet.reservation.model.entity.Reservation;
import com.ajou_nice.with_pet.reservation.model.entity.ReservationPetSitterService;
import com.ajou_nice.with_pet.reservation.repository.ReservationRepository;
import com.ajou_nice.with_pet.user.service.UserValidationService;
import com.ajou_nice.with_pet.withpet_service.model.entity.PetSitterWithPetService;
import com.ajou_nice.with_pet.withpet_service.repository.PetSitterServiceRepository;
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
public class UserReservationService {

    private final UserValidationService userValidationService;
    private final DogValidationService dogValidationService;
    private final PetSitterValidationService petSitterValidationService;


    private final ReservationRepository reservationRepository;
    private final PetSitterCriticalServiceRepository petSitterCriticalServiceRepository;
    private final PetSitterServiceRepository serviceRepository;
    private final ReservationPetSitterServiceRepository reservationServiceRepository;


    private final List<ReservationStatus> reservationStatuses = new ArrayList<>(
            List.of(ReservationStatus.APPROVAL, ReservationStatus.PAYED,
                    ReservationStatus.USE, ReservationStatus.WAIT));

    public UserReservationGetInfosResponse getUserReservations(String email) {
        User user = userValidationService.userValidationByEmail(email);
        List<Reservation> userReservations = reservationRepository.findAllByUser(user).get();

        return filterReservationsByStatus(userReservations);
    }

    public UserReservationGetInfosResponse filterReservationsByStatus(List<Reservation> userReservations) {
        List<Reservation> waitReservations = filterByStatus(userReservations, ReservationStatus.WAIT);
        List<Reservation> payedReservations = filterByStatus(userReservations, ReservationStatus.PAYED);
        List<Reservation> approveReservations = filterByStatus(userReservations, ReservationStatus.APPROVAL);
        List<Reservation> useReservations = filterByStatus(userReservations, ReservationStatus.USE);
        List<Reservation> doneReservations = filterByStatus(userReservations, ReservationStatus.DONE);

        return UserReservationGetInfosResponse.of(waitReservations, payedReservations, approveReservations,
                useReservations, doneReservations);
    }

    private List<Reservation> filterByStatus(List<Reservation> reservations, ReservationStatus status) {
        return reservations.stream()
                .filter(reservation -> reservation.getReservationStatus().equals(status))
                .collect(Collectors.toList());
    }

    @Transactional
    public UserReservationCreateResponse createReservation(String email,
                                                           ReservationCreateRequest reservationCreateRequest) {

        User user = userValidationService.userValidationByEmail(email);
        Dog dog = dogValidationService.dogValidationById(reservationCreateRequest.getDogId());
        PetSitter petSitter = petSitterValidationService.petSitterValidationByPetSitterId(
                reservationCreateRequest.getPetSitterId());

        //반려견 예약 유효 체크
        isDuplicatedDogReservation(reservationCreateRequest.getReservationCheckIn(),
                reservationCreateRequest.getReservationCheckOut(), dog, reservationStatuses);
        //펫시터 예약 유효 체크
        isConflictPetSitterReservation(reservationCreateRequest.getReservationCheckIn(),
                reservationCreateRequest.getReservationCheckOut(), petSitter, reservationStatuses);

        //소형견, 중형견, 대형견 옵션 선택
        PetSitterCriticalService criticalService = petSitterCriticalServiceRepository.findByPetSitterAndCriticalService_ServiceName(
                petSitter, dog.getDogSize().name()).orElseThrow(() -> new AppException(
                ErrorCode.PETSITTER_SERVICE_NOT_FOUND, "해당 옵션이 존재하지 않습니다."));
        //부가 옵션
        List<PetSitterWithPetService> withPetServices = serviceRepository.findAllById(
                reservationCreateRequest.getReservationOptionIdList());
        int cost = calculatorReservationCost(reservationCreateRequest, criticalService, withPetServices);

        Reservation reservation = reservationRepository.save(
                Reservation.of(reservationCreateRequest, user, dog, petSitter, criticalService));
        List<ReservationPetSitterService> reservationServices = toReservationPetSitterService(withPetServices,
                petSitter, reservation);

        reservationServiceRepository.saveAll(reservationServices);
        reservation.updateReservationServices(reservationServices);
        reservation.updateTotalPrice(cost);

        /*
        List<UserParty> userParties = userPartyRepository.findAllByParty(dog.getParty());
        sendCreateReservationNotificationByEmail(userParties, user, dog, petSitter);
        */

        return UserReservationCreateResponse.of(reservation);
    }

    private int calculatorReservationCost(ReservationCreateRequest reservationCreateRequest,
                                          PetSitterCriticalService petSitterCriticalService,
                                          List<PetSitterWithPetService> petSitterWithPetServices) {
        int cost = 0;
        cost += petSitterCriticalService.getPrice();

        //시간에 따라 요금 계산 (날짜 * 필수 옵션 가격)
        Period diff = Period.between(reservationCreateRequest.getReservationCheckIn().toLocalDate(),
                reservationCreateRequest.getReservationCheckOut().toLocalDate());
        cost *= diff.getDays();

        for (PetSitterWithPetService petSitterWithPetService : petSitterWithPetServices) {
            cost += petSitterWithPetService.getPrice();
        }
        return cost;
    }

    private List<ReservationPetSitterService> toReservationPetSitterService(
            List<PetSitterWithPetService> petSitterWithPetServices, PetSitter petSitter, Reservation reservation) {
        List<ReservationPetSitterService> reservationServices = new ArrayList<>();

        for (PetSitterWithPetService petSitterWithPetService : petSitterWithPetServices) {
            if (!petSitterWithPetService.getPetSitter().getId().equals(petSitter.getId())) {
                throw new AppException(ErrorCode.PETSITTER_SERVICE_NOT_FOUND,
                        "해당 펫시터가 제공하는 서비스가 아닙니다.");
            }
            ReservationPetSitterService reservationService =
                    ReservationPetSitterService.of(reservation, petSitterWithPetService);
            reservationServices.add(reservationService);
        }

        return reservationServices;
    }

    private void isDuplicatedDogReservation(LocalDateTime checkIn, LocalDateTime checkOut, Dog dog,
                                            List<ReservationStatus> statuses) {
        if (reservationRepository.existsByReservationCheckInBetweenAndDogAndReservationStatusIn(checkIn,
                checkOut, dog, statuses)) {
            throw new AppException(ErrorCode.DUPLICATED_RESERVATION,
                    ErrorCode.DUPLICATED_RESERVATION.getMessage());
        }

        if (reservationRepository.existsByReservationCheckOutBetweenAndDogAndReservationStatusIn(checkIn,
                checkOut, dog, statuses)) {
            throw new AppException(ErrorCode.DUPLICATED_RESERVATION,
                    ErrorCode.DUPLICATED_RESERVATION.getMessage());
        }
    }

    //펫시터 입장에서 validation
    private void isConflictPetSitterReservation(LocalDateTime checkIn, LocalDateTime checkOut,
                                                PetSitter petSitter, List<ReservationStatus> reservationStatuses) {
        if (reservationRepository.existsByReservationCheckInBetweenAndPetSitterAndReservationStatusIn(checkIn,
                checkOut, petSitter, reservationStatuses)) {
            throw new AppException(ErrorCode.RESERVATION_CONFLICT,
                    ErrorCode.RESERVATION_CONFLICT.getMessage());
        }

        if (reservationRepository.existsByReservationCheckOutBetweenAndPetSitterAndReservationStatusIn(checkIn,
                checkOut, petSitter, reservationStatuses)) {
            throw new AppException(ErrorCode.RESERVATION_CONFLICT,
                    ErrorCode.RESERVATION_CONFLICT.getMessage());
        }
    }

    /*
    private void sendCreateReservationNotificationByEmail(List<UserParty> userParties, User user,
                                                          Dog dog,
                                                          PetSitter petSitter) {
        List<Notification> notifications = new ArrayList<>();
        userParties.forEach(u -> {
            Notification notification = notificationService.sendEmail(
                    String.format("%s님이 %s 반려견에 대한 돌봄 서비스 예약을 했습니다. [펫시터] %s", user.getName(),
                            dog.getDogName(), petSitter.getPetSitterName()),
                    "/usagelist",
                    NotificationType.반려인_예약, u.getUser());
            notificationService.saveNotification(notification);
        });

        Notification notification = notificationService.sendEmail(
                String.format("%s님이 %s 반려견에 대한 돌봄 서비스 예약을 했습니다.", user.getName(), dog.getDogName()),
                "/petsitterCalendar",
                NotificationType.펫시터_예약, petSitter.getUser());
        notificationService.saveNotification(notification);
    }
    */
}
