package com.ajou_nice.with_pet.service;

import com.ajou_nice.with_pet.domain.dto.dog.DogSocializationRequest;
import com.ajou_nice.with_pet.domain.dto.reservation.PaymentResponseForPetSitter;
import com.ajou_nice.with_pet.domain.dto.reservation.ReservationCreateResponse;
import com.ajou_nice.with_pet.domain.dto.reservation.ReservationDetailResponse;
import com.ajou_nice.with_pet.domain.dto.reservation.ReservationDocsResponse;
import com.ajou_nice.with_pet.domain.dto.reservation.ReservationRequest;
import com.ajou_nice.with_pet.domain.dto.reservation.ReservationResponse;
import com.ajou_nice.with_pet.domain.dto.review.ReviewRequest;
import com.ajou_nice.with_pet.domain.entity.Dog;
import com.ajou_nice.with_pet.domain.entity.PetSitter;
import com.ajou_nice.with_pet.domain.entity.PetSitterCriticalService;
import com.ajou_nice.with_pet.domain.entity.PetSitterWithPetService;
import com.ajou_nice.with_pet.domain.entity.Reservation;
import com.ajou_nice.with_pet.domain.entity.ReservationPetSitterService;
import com.ajou_nice.with_pet.domain.entity.Review;
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
import com.ajou_nice.with_pet.repository.ReviewRepository;
import com.ajou_nice.with_pet.repository.UserRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
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


    @Transactional
    public ReservationCreateResponse createReservation(String userId, ReservationRequest reservationRequest) {
        int cost = 0;
        User user = valid.userValidation(userId);
        Dog dog = valid.dogValidation(reservationRequest.getDogId());


        //반려견 예약 유효 체크
        isDuplicatedReservation(reservationRequest.getCheckIn(), reservationRequest.getCheckOut(),
                dog, reservationStatuses);

        //펫시터 유효 체크
        PetSitter petSitter = valid.petSitterValidation(reservationRequest.getPetsitterId());

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
        Reservation reservation = valid.reservationValidation(reservationId);

        //user가 펫시터인지 검증
        User user = valid.userValidation(userId);

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

    @Transactional
    public ReservationDetailResponse approveReservation(String userId, Long reservationId, String status) {

        //상태 변경 값이 잘 들어왔는지 유효 체크
        if(!status.equals(ReservationStatus.APPROVAL.toString())){
            throw new AppException(ErrorCode.BAD_REQUEST_RESERVATION_STATSUS, ErrorCode.BAD_REQUEST_RESERVATION_STATSUS.getMessage());
        }

        valid.userValidation(userId);

        Reservation reservation = valid.reservationValidation(reservationId);

        if (!reservation.getPetSitter().getUser().getId().equals(userId)) {
            throw new AppException(ErrorCode.UNAUTHORIZED_RESERVATION,
                    ErrorCode.UNAUTHORIZED_RESERVATION.getMessage());
        }

        reservation.updateStatus(status);
        if(LocalDate.now().equals(reservation.getCheckIn().toLocalDate())){
            reservation.updateStatus(ReservationStatus.USE.toString());
        }
        return ReservationDetailResponse.of(reservation);
    }

    public List<ReservationResponse> getMonthlyReservations(String userId, String month) {
        User user = valid.userValidation(userId);

        PetSitter petSitter = valid.petSitterValidationByUser(user);

        List<Reservation> reservations = reservationRepository.findAllByPetsitterAndMonthAndStatus(petSitter,
                LocalDate.parse(month + "-01"), reservationStatuses);

        return reservations.stream().map(ReservationResponse::of).collect(Collectors.toList());
    }

    public PaymentResponseForPetSitter getPaymentView(String userId, Long reservationId){

        valid.userValidation(userId);

        Reservation reservation = valid.reservationValidation(reservationId);

        return PaymentResponseForPetSitter.of(reservation);
    }

    // 반려인의 예약 취소
    // 승인 전 예약 건에 대해서
    @Transactional
    public void cancelReservation(String userId, Long reservationId){
        valid.userValidation(userId);

        Reservation reservation = valid.reservationValidation(reservationId);

        reservation.updateStatus(ReservationStatus.CANCEL.toString());
    }

    // 반려인의 이용 완료
    @Transactional
    public void doneReservation(String userId, Long reservationId){
        valid.userValidation(userId);

        Reservation reservation = valid.reservationValidation(reservationId);
        reservation.updateStatus(ReservationStatus.DONE.toString());
    }

    //반려인의 리뷰 작성
    @Transactional
    public void postReview(String userId, ReviewRequest reviewRequest){

        valid.userValidation(userId);

        Reservation reservation = valid.reservationValidation(reviewRequest.getReservationId());

        Review review = Review.of(reservation, reviewRequest.getGrade(), reviewRequest.getContent());
        reviewRepository.save(review);

        PetSitter petSitter = valid.petSitterValidation(reservation.getPetSitter().getId());

        petSitter.updateReview(reviewRequest.getGrade());

    }


    //예약 내역 반려인 입장에서
    public ReservationDocsResponse getReservationDoc(String userId){

        User user = valid.userValidation(userId);

        Optional<List<Reservation>> myReservations = reservationRepository.findAllByUser(user);

        //w
        List<Reservation> waitReservations = myReservations.get().stream().filter(
                reservation -> reservation.getReservationStatus().equals(ReservationStatus.WAIT)).collect(
                Collectors.toList());

        //p
        List<Reservation> payedReservations = myReservations.get().stream().filter(
                reservation -> reservation.getReservationStatus().equals(ReservationStatus.PAYED)).collect(
                Collectors.toList());
        //a
        List<Reservation> approveReservations = myReservations.get().stream().filter(
                reservation -> reservation.getReservationStatus().equals(ReservationStatus.APPROVAL)).collect(
                Collectors.toList());

        //u
        List<Reservation> useReservations = myReservations.get().stream().filter(
                reservation -> reservation.getReservationStatus().equals(ReservationStatus.USE)).collect(
                Collectors.toList());

        //d
        List<Reservation> doneReservations = myReservations.get().stream().filter(
                reservation -> reservation.getReservationStatus().equals(ReservationStatus.DONE)).collect(
                Collectors.toList());


        return ReservationDocsResponse.of(waitReservations, payedReservations, approveReservations,useReservations,
                doneReservations);
    }

}
