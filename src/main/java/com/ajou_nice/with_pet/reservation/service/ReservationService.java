package com.ajou_nice.with_pet.reservation.service;

import com.ajou_nice.with_pet.critical_service.repository.PetSitterCriticalServiceRepository;
import com.ajou_nice.with_pet.dog.model.entity.Dog;
import com.ajou_nice.with_pet.dog.service.DogValidationService;
import com.ajou_nice.with_pet.domain.entity.Notification;
import com.ajou_nice.with_pet.domain.entity.Review;
import com.ajou_nice.with_pet.domain.entity.User;
import com.ajou_nice.with_pet.domain.entity.UserParty;
import com.ajou_nice.with_pet.enums.NotificationType;
import com.ajou_nice.with_pet.enums.ReservationStatus;
import com.ajou_nice.with_pet.exception.AppException;
import com.ajou_nice.with_pet.exception.ErrorCode;
import com.ajou_nice.with_pet.petsitter.model.entity.PetSitter;
import com.ajou_nice.with_pet.petsitter.service.PetSitterValidationService;
import com.ajou_nice.with_pet.repository.ReservationPetSitterServiceRepository;
import com.ajou_nice.with_pet.repository.ReviewRepository;
import com.ajou_nice.with_pet.repository.UserPartyRepository;
import com.ajou_nice.with_pet.reservation.model.dto.PaymentResponseForPetSitter;
import com.ajou_nice.with_pet.reservation.model.dto.PetSitterReservationGetMonthlyResponse;
import com.ajou_nice.with_pet.reservation.model.dto.PetSitterReservationGetSideInfoResponse;
import com.ajou_nice.with_pet.reservation.model.dto.PetSitterReservationPatchApprovalResponse;
import com.ajou_nice.with_pet.reservation.model.dto.PetSitterReservationPutEvaluateDogRequest;
import com.ajou_nice.with_pet.reservation.model.dto.PetSitterReservationPutEvaluateDogResponse;
import com.ajou_nice.with_pet.reservation.model.dto.UserReservationGetInfosResponse;
import com.ajou_nice.with_pet.reservation.model.entity.Reservation;
import com.ajou_nice.with_pet.reservation.repository.ReservationRepository;
import com.ajou_nice.with_pet.review.model.dto.ReviewRequest;
import com.ajou_nice.with_pet.service.NotificationService;
import com.ajou_nice.with_pet.service.ValidateCollection;
import com.ajou_nice.with_pet.user.service.UserValidationService;
import com.ajou_nice.with_pet.withpet_service.repository.PetSitterServiceRepository;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
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
    private final ReviewRepository reviewRepository;

    private final ValidateCollection valid;

    private final NotificationService notificationService;
    private final UserValidationService userValidationService;
    private final PetSitterValidationService petSitterValidationService;
    private final ReservationValidationService reservationValidationService;
    private final DogValidationService dogValidationService;


    public List<PetSitterReservationGetMonthlyResponse> getMonthlyReservations(String email, String month) {
        List<ReservationStatus> getReservationStatuses = new ArrayList<>(
                List.of(ReservationStatus.APPROVAL, ReservationStatus.PAYED, ReservationStatus.USE));

        User user = userValidationService.userValidationByEmail(email);
        PetSitter petSitter = petSitterValidationService.petSitterValidationByUser(user);

        LocalDate startDate = validatedStartDate(month);
        List<Reservation> reservations = reservationRepository.findAllByPetSitterAndMonthAndStatus(petSitter,
                startDate, getReservationStatuses);

        return reservations.stream().map(PetSitterReservationGetMonthlyResponse::of).collect(Collectors.toList());
    }

    private LocalDate validatedStartDate(String month) {
        try {
            return LocalDate.parse(month + "-01");
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid month format: " + month, e);
        }
    }

    public List<String> getUnavailableDates(Long petSitterId, String month) {
        List<ReservationStatus> reservationStatuses = new ArrayList<>(
                List.of(ReservationStatus.APPROVAL, ReservationStatus.PAYED,
                        ReservationStatus.USE, ReservationStatus.WAIT));

        PetSitter petSitter = petSitterValidationService.petSitterValidationByPetSitterId(petSitterId);

        LocalDate startDate = validatedStartDate(month);
        List<Reservation> reservations = reservationRepository.findAllByPetSitterAndMonthAndStatus(petSitter, startDate,
                reservationStatuses);

        return getUnavailableDateList(reservations);
    }

    private ArrayList<String> getUnavailableDateList(List<Reservation> reservations) {
        ArrayList<String> unavailableDateList = new ArrayList<>();
        for (Reservation reservation : reservations) {
            unavailableDateList.addAll(getDateRange(reservation.getReservationCheckIn().toLocalDate(),
                    reservation.getReservationCheckOut().toLocalDate()));
        }
        return unavailableDateList;
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
    // 펫시터가 완료된 예약의 반려견의 사회화 온도 평가
    public PetSitterReservationPutEvaluateDogResponse evaluateDogSocialTemperature(String email, Long reservationId,
                                                                                   PetSitterReservationPutEvaluateDogRequest dogSocializationRequest) {

        Reservation reservation = reservationValidationService.reservationValidationById(reservationId);
        PetSitter petSitter = petSitterValidationService.petSitterValidationByPetSitterEmail(email);
        Dog dog = dogValidationService.dogValidationById(reservation.getDog().getDogId());

        float score = (float) (dogSocializationRequest.getDogSocialTemperatureQ1()
                + dogSocializationRequest.getDogSocialTemperatureQ2()
                + dogSocializationRequest.getDogSocialTemperatureQ3() +
                dogSocializationRequest.getDogSocialTemperatureQ4()
                + dogSocializationRequest.getDogSocialTemperatureQ5()) / 5;

        if (score < 2.5) {
            score = -score;
            dog.updateSocializationTemperature(score);
        } else {
            dog.updateSocializationTemperature(score);
        }

        return PetSitterReservationPutEvaluateDogResponse.of("반려견의 사회화 온도 평가가 완료되었습니다.");
    }

    @Transactional
    public PetSitterReservationPatchApprovalResponse approveReservation(String email, Long reservationId) {
        PetSitter petSitter = petSitterValidationService.petSitterValidationByPetSitterEmail(email);
        Reservation reservation = valid.reservationValidation(reservationId);

        validationPetSitterReservation(reservation, petSitter);
        reservation.approveReservation();

        // 필요한 로직인지 의문
        if (LocalDate.now().equals(reservation.getReservationCheckIn().toLocalDate())) {
            reservation.updateStatus(ReservationStatus.USE.toString());
        }

        // 이메일 알림 주석

//        List<UserParty> userParties = userPartyRepository.findAllByParty(reservation.getDog()
//                .getParty());
//        sendApproveReservationNotificationByEmail(userParties, reservation.getDog(),
//                reservation.getPetSitter());

        return PetSitterReservationPatchApprovalResponse.of(reservation);
    }

    private void validationPetSitterReservation(Reservation reservation, PetSitter petSitter) {
        if (!reservation.getPetSitter().getId().equals(petSitter.getId())) {
            throw new AppException(ErrorCode.UNAUTHORIZED_RESERVATION, ErrorCode.UNAUTHORIZED_RESERVATION.getMessage());
        }
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

    public PetSitterReservationGetSideInfoResponse getPetSitterReservationsByType(String email, String month) {
        PetSitter petSitter = petSitterValidationService.petSitterValidationByPetSitterEmail(email);

        List<Reservation> useReservation = reservationRepository.getPetsitterSideBarInfo(petSitter,
                LocalDate.parse(month + "-01"), ReservationStatus.USE);
        List<Reservation> payedReservation = reservationRepository.findAllByPetSitterAndReservationStatus(
                petSitter, ReservationStatus.PAYED);
        List<Reservation> approvalReservation = reservationRepository.findAllByPetSitterAndReservationStatus(
                petSitter, ReservationStatus.APPROVAL);
        List<Reservation> doneReservation = reservationRepository.getPetsitterSideBarInfo(petSitter,
                LocalDate.parse(month + "-01"), ReservationStatus.DONE);

        int cost = 0;
        for (Reservation reservation : doneReservation) {
            cost += reservation.getReservationTotalPrice();
        }

        return PetSitterReservationGetSideInfoResponse.of(useReservation, payedReservation, approvalReservation, doneReservation, cost);
    }

}
