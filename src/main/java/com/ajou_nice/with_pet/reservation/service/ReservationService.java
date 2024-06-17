package com.ajou_nice.with_pet.reservation.service;

import com.ajou_nice.with_pet.dog.model.dto.DogSocializationRequest;
import com.ajou_nice.with_pet.dog.service.DogValidationService;
import com.ajou_nice.with_pet.petsitter.service.PetSitterValidationService;
import com.ajou_nice.with_pet.reservation.model.dto.PaymentResponseForPetSitter;
import com.ajou_nice.with_pet.reservation.model.dto.ReservationDetailResponse;
import com.ajou_nice.with_pet.reservation.model.dto.UserReservationGetInfosResponse;
import com.ajou_nice.with_pet.reservation.model.dto.ReservationResponse;
import com.ajou_nice.with_pet.review.model.dto.ReviewRequest;
import com.ajou_nice.with_pet.dog.model.entity.Dog;
import com.ajou_nice.with_pet.domain.entity.Notification;
import com.ajou_nice.with_pet.petsitter.model.entity.PetSitter;
import com.ajou_nice.with_pet.service.NotificationService;
import com.ajou_nice.with_pet.service.ValidateCollection;
import com.ajou_nice.with_pet.user.service.UserValidationService;
import com.ajou_nice.with_pet.reservation.model.entity.Reservation;
import com.ajou_nice.with_pet.domain.entity.Review;
import com.ajou_nice.with_pet.domain.entity.User;
import com.ajou_nice.with_pet.domain.entity.UserParty;
import com.ajou_nice.with_pet.enums.NotificationType;
import com.ajou_nice.with_pet.enums.ReservationStatus;
import com.ajou_nice.with_pet.exception.AppException;
import com.ajou_nice.with_pet.exception.ErrorCode;
import com.ajou_nice.with_pet.critical_service.repository.PetSitterCriticalServiceRepository;
import com.ajou_nice.with_pet.withpet_service.repository.PetSitterServiceRepository;
import com.ajou_nice.with_pet.repository.ReservationPetSitterServiceRepository;
import com.ajou_nice.with_pet.reservation.repository.ReservationRepository;
import com.ajou_nice.with_pet.repository.ReviewRepository;
import com.ajou_nice.with_pet.repository.UserPartyRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final PetSitterCriticalServiceRepository petSitterCriticalServiceRepository;
    private final PetSitterServiceRepository serviceRepository;
    private final ReservationPetSitterServiceRepository reservationServiceRepository;
    private final ReviewRepository reviewRepository;

    private final ValidateCollection valid;

    private final List<ReservationStatus> reservationStatuses = new ArrayList<>(
            List.of(ReservationStatus.APPROVAL, ReservationStatus.PAYED,
                    ReservationStatus.USE, ReservationStatus.WAIT));

    private final UserPartyRepository userPartyRepository;
    private final NotificationService notificationService;

    private final UserValidationService userValidationService;
    private final PetSitterValidationService petSitterValidationService;
    private final DogValidationService dogValidationService;

    @Transactional
    // 펫시터가 완료된 예약의 반려견의 사회화 온도 평가
    public void modifyDogTemp(String userId, Long reservationId,
                              DogSocializationRequest dogSocializationRequest) {

        //reservation
        Reservation reservation = valid.reservationValidation(reservationId);

        //user가 펫시터인지 검증
        User user = valid.userValidationById(userId);

        //예약의 펫시터가 아니라면
        if (!reservation.getPetSitter().getUser().equals(user)) {
            throw new AppException(ErrorCode.PETSITTER_NOT_FOUND,
                    ErrorCode.PETSITTER_NOT_FOUND.getMessage());
        }

        // 예약의 펫시터가 맞다면
        Dog dog = valid.dogValidation(reservation.getDog().getDogId());

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

    public List<String> getUnavailableDates(Long petsitterId, String month) {

        List<String> unavailableDates = new ArrayList<>();

        PetSitter petSitter = valid.petSitterValidation(petsitterId);

        List<Reservation> reservations = reservationRepository.findAllByPetsitterAndMonthAndStatus(
                petSitter,
                LocalDate.parse(month + "-01"), reservationStatuses);

        for (Reservation reservation : reservations) {
            unavailableDates.addAll(getDateRange(reservation.getReservationCheckIn().toLocalDate(),
                    reservation.getReservationCheckOut().toLocalDate()));
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

    @Transactional
    public ReservationDetailResponse approveReservation(String userId, Long reservationId,
                                                        String status) {

        //상태 변경 값이 잘 들어왔는지 유효 체크
        if (!status.equals(ReservationStatus.APPROVAL.toString())) {
            throw new AppException(ErrorCode.BAD_REQUEST_RESERVATION_STATSUS,
                    ErrorCode.BAD_REQUEST_RESERVATION_STATSUS.getMessage());
        }

        valid.userValidationById(userId);

        Reservation reservation = valid.reservationValidation(reservationId);

        if (!reservation.getPetSitter().getUser().getId().equals(userId)) {
            throw new AppException(ErrorCode.UNAUTHORIZED_RESERVATION,
                    ErrorCode.UNAUTHORIZED_RESERVATION.getMessage());
        }

        reservation.updateStatus(status);
        if (LocalDate.now().equals(reservation.getReservationCheckIn().toLocalDate())) {
            reservation.updateStatus(ReservationStatus.USE.toString());
        }

        List<UserParty> userParties = userPartyRepository.findAllByParty(reservation.getDog()
                .getParty());
        sendApproveReservationNotificationByEmail(userParties, reservation.getDog(),
                reservation.getPetSitter());
        return ReservationDetailResponse.of(reservation);
    }

    private void sendApproveReservationNotificationByEmail(List<UserParty> userParties, Dog dog,
                                                           PetSitter petSitter) {
        userParties.forEach(u -> {
            Notification notification = notificationService.sendEmail(
                    String.format("%s 펫시터님이 %s 반려견에 대한 돌봄 서비스 예약을 승인했습니다.",
                            petSitter.getPetSitterName(), dog.getDogName()),
                    "/usagelist",
                    NotificationType.반려인_예약, u.getUser());
            notificationService.saveNotification(notification);
        });
    }


    public List<ReservationResponse> getMonthlyReservations(String userId, String month) {
        User user = valid.userValidationById(userId);

        PetSitter petSitter = valid.petSitterValidationByUser(user);

        List<Reservation> reservations = reservationRepository.findAllByPetsitterAndMonthAndStatus(
                petSitter,
                LocalDate.parse(month + "-01"), reservationStatuses);

        return reservations.stream().map(ReservationResponse::of).collect(Collectors.toList());
    }

    public PaymentResponseForPetSitter getPaymentView(String userId, Long reservationId) {

        valid.userValidationById(userId);

        Reservation reservation = valid.reservationValidation(reservationId);

        return PaymentResponseForPetSitter.of(reservation);
    }

    // 반려인의 예약 취소
    // 승인 전 예약 건에 대해서
    @Transactional
    public void cancelReservation(String userId, Long reservationId) {
        valid.userValidationById(userId);

        Reservation reservation = valid.reservationValidation(reservationId);

        reservation.updateStatus(ReservationStatus.CANCEL.toString());
    }

    // 반려인의 이용 완료
    @Transactional
    public void doneReservation(String userId, Long reservationId) {
        valid.userValidationById(userId);

        Reservation reservation = valid.reservationValidation(reservationId);
        reservation.updateStatus(ReservationStatus.DONE.toString());
    }

    //반려인의 리뷰 작성
    @Transactional
    public void postReview(String userId, ReviewRequest reviewRequest) {

        valid.userValidationById(userId);

        Reservation reservation = valid.reservationValidation(reviewRequest.getReservationId());

        Review review = Review.of(reservation, reviewRequest.getGrade(),
                reviewRequest.getContent());
        reviewRepository.save(review);

        PetSitter petSitter = valid.petSitterValidation(reservation.getPetSitter().getId());

        petSitter.updateReview(reviewRequest.getGrade());

    }


    //예약 내역 반려인 입장에서
    public UserReservationGetInfosResponse getUserReservations(String userId) {

        User user = valid.userValidationById(userId);

        Optional<List<Reservation>> myReservations = reservationRepository.findAllByUser(user);

        //w
        List<Reservation> waitReservations = myReservations.get().stream().filter(
                        reservation -> reservation.getReservationStatus().equals(ReservationStatus.WAIT))
                .collect(Collectors.toList());

        //p
        List<Reservation> payedReservations = myReservations.get().stream().filter(
                        reservation -> reservation.getReservationStatus().equals(ReservationStatus.PAYED))
                .collect(Collectors.toList());
        //a
        List<Reservation> approveReservations = myReservations.get().stream().filter(
                reservation -> reservation.getReservationStatus()
                        .equals(ReservationStatus.APPROVAL)).collect(Collectors.toList());

        //u
        List<Reservation> useReservations = myReservations.get().stream().filter(
                        reservation -> reservation.getReservationStatus().equals(ReservationStatus.USE))
                .collect(Collectors.toList());

        //d
        List<Reservation> doneReservations = myReservations.get().stream().filter(
                        reservation -> reservation.getReservationStatus().equals(ReservationStatus.DONE))
                .collect(Collectors.toList());

        return UserReservationGetInfosResponse.of(waitReservations, payedReservations, approveReservations,
                useReservations, doneReservations);
    }

}
