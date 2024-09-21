package com.ajou_nice.with_pet.pay.service;

import com.ajou_nice.with_pet.domain.entity.User;
import com.ajou_nice.with_pet.enums.PayStatus;
import com.ajou_nice.with_pet.enums.ReservationStatus;
import com.ajou_nice.with_pet.exception.AppException;
import com.ajou_nice.with_pet.exception.ErrorCode;
import com.ajou_nice.with_pet.pay.model.dto.PayApproveResponse;
import com.ajou_nice.with_pet.pay.model.dto.PayCancelResponse;
import com.ajou_nice.with_pet.pay.model.dto.PayReadyResponse;
import com.ajou_nice.with_pet.pay.model.dto.RefundResponse;
import com.ajou_nice.with_pet.pay.model.entity.Pay;
import com.ajou_nice.with_pet.pay.repository.PayRepository;
import com.ajou_nice.with_pet.reservation.model.entity.Reservation;
import com.ajou_nice.with_pet.reservation.repository.ReservationRepository;
import com.ajou_nice.with_pet.reservation.service.ReservationValidationService;
import com.ajou_nice.with_pet.user.service.UserValidationService;
import com.ajou_nice.with_pet.utils.HttpUtil;
import java.time.LocalDate;
import java.time.Period;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

@Slf4j
@RequiredArgsConstructor
@Service
public class KaKaoPayService {
    private final String KAKAOPAY_URI = "https://open-api.kakaopay.com/online/v1";

    private final ReservationRepository reservationRepository;
    private final PayRepository payRepository;
    //private final NotificationService notificationService;
    //private final UserPartyRepository userPartyRepository;

    private final UserValidationService userValidationService;
    private final ReservationValidationService reservationValidationService;

    private final HttpUtil httpUtil;
    private final KakaoPayUtilService kakaoPayUtilService;


    //pay ready를 요청하면 pay entity 생성 x
    @Transactional
    public PayReadyResponse payReady(String email, Long reservationId) {

        User user = userValidationService.userValidationByEmail(email);
        Reservation reservation = reservationValidationService.reservationValidationById(reservationId);

        Map<String, String> parameters = kakaoPayUtilService.getReadyPayParameters(user, reservation);

        //파라미터와 header설정
        HttpEntity<Map<String, String>> requests = httpUtil.getHttpEntity(kakaoPayUtilService.getHeaders(), parameters);

        // RestTemplate 객체 생성
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());

        //restTemplate를 이용한 api 호출 카카오 페이
        PayReadyResponse payReadyResponse = restTemplate.postForObject(KAKAOPAY_URI + "/payment/ready",
                requests, PayReadyResponse.class);

        assert payReadyResponse != null;
        reservation.updateTid(payReadyResponse.getTid());

        return payReadyResponse;
    }


    //결제 완료 -> 예약상태 payed , payed 생성 후 success update
    @Transactional
    public PayApproveResponse approvePay(String email, String pgToken, String tid) {

        userValidationService.userValidationByEmail(email);

        Reservation reservation = reservationRepository.findByTid(tid).orElseThrow(() -> new AppException(
                ErrorCode.RESERVATION_NOT_FOUND,
                ErrorCode.RESERVATION_NOT_FOUND.getMessage()));

        Map<String, String> parameters = kakaoPayUtilService.getApprovePayParameters(tid, pgToken, reservation);
        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(parameters, kakaoPayUtilService.getHeaders());

        RestTemplate restTemplate = new RestTemplate();
        PayApproveResponse approveResponse = restTemplate.postForObject(
                KAKAOPAY_URI + "/payment/approve", requestEntity, PayApproveResponse.class);

        assert approveResponse != null;
        Pay pay = Pay.of(reservation, approveResponse);
        payRepository.save(pay);
        reservation.approvePay(ReservationStatus.PAYED);

        return approveResponse;
    }

    // 결제 자동 환불
    // 결제는 되었으나, 펫시터가 예약 승락을 안해줌
    @Transactional
    public void autoRefund(Reservation reservation) {

        reservation.updateStatus(ReservationStatus.AUTO_CANCEL.toString());
        Pay pay = payRepository.findByReservation(reservation).orElseThrow(() -> new AppException(
                ErrorCode.PAY_NOT_FOUND, ErrorCode.PAY_NOT_FOUND.getMessage()));

        Map<String, String> parameters = kakaoPayUtilService.getAutoRefund(pay);
        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(parameters, kakaoPayUtilService.getHeaders());

        RestTemplate restTemplate = new RestTemplate();
        PayCancelResponse payCancelResponse = restTemplate.postForObject(
                KAKAOPAY_URI + "/payment/cancel", requestEntity, PayCancelResponse.class);

        //pay 상태 refund update -> soft delete 기법 사용 (삭제는 하지 않는다) -> 추후 환불에 대한 내역을 볼 수 있도록
        assert payCancelResponse != null;
        pay.refund(PayStatus.REFUND, payCancelResponse.getCanceled_amount().getTotal(),
                payCancelResponse.getCanceled_at());

        /* 알림 주석
        List<UserParty> userParties = userPartyRepository.findAllByParty(reservation.getDog()
                .getParty());
        sendAutoCancelReservationNotificationByEmail(userParties, reservation.getDog(),
                reservation.getPetSitter());
         */
    }

    //결제 환불
    //예약 상태 -> cancel, pay 상태 -> Refund , pay entity 환불금액 update
    @Transactional
    public RefundResponse refundPayment(String email, Long reservationId) {

        User user = userValidationService.userValidationByEmail(email);
        Reservation reservation = reservationValidationService.reservationValidationById(reservationId);

        Pay pay = payRepository.findByReservation(reservation).orElseThrow(() -> new AppException(
                ErrorCode.PAY_NOT_FOUND, ErrorCode.PAY_NOT_FOUND.getMessage()));

        int cancelAmount = getCancelAmount(reservation, user, pay);

        Map<String, String> parameters = kakaoPayUtilService.getRefundParameters(pay, cancelAmount);

        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(parameters, kakaoPayUtilService.getHeaders());
        RestTemplate restTemplate = new RestTemplate();
        PayCancelResponse payCancelResponse = restTemplate.postForObject(KAKAOPAY_URI + "/payment/cancel",
                requestEntity, PayCancelResponse.class);

        //예약 상태 cancel update
        reservation.updateStatus(ReservationStatus.CANCEL.toString());

        //pay 상태 refund update -> soft delete 기법 사용 (삭제는 하지 않는다) -> 추후 환불에 대한 내역을 볼 수 있도록
        assert payCancelResponse != null;
        pay.refund(PayStatus.REFUND, payCancelResponse.getCanceled_amount().getTotal(),
                payCancelResponse.getCanceled_at());

        return RefundResponse.of("예약을 거절했습니다.");
    }

    private static int getCancelAmount(Reservation reservation, User user, Pay pay) {
        Period period = Period.between(LocalDate.now(), reservation.getReservationCheckIn().toLocalDate());
        int cancelAmount; //환불 금액

        //List<UserParty> userParties = userPartyRepository.findAllByParty(reservation.getDog().getParty());

        //만약 결제 환불을 요청한 사람이 petsitter라면 100% 환불
        //결제 환불을 요청한 사람이 user라면
        //아직 승인받지 못한 예약의 경우 100% 환불 (PAYED)
        //
        if (reservation.getPetSitter().getUser().equals(user)) {
            cancelAmount = pay.getPay_amount();
            //sendCancelReservationNotificationByEmail(userParties, reservation.getDog(), reservation.getPetSitter());
        } else {
            //환불 금액 판정 -> 예약날짜가 7일 이내로 남았을 경우 50%환불, 예약날짜가 7일 초과라면 100% 환불
            if (period.getDays() > 7) {
                cancelAmount = pay.getPay_amount();
            } else {
                cancelAmount = (int) (pay.getPay_amount() * 0.5);
            }
            //sendCancelReservationNotificationByEmailByPetSitter(userParties, reservation.getDog(), reservation.getPetSitter());
        }
        return cancelAmount;
    }

    /*
    private void sendAutoCancelReservationNotificationByEmail(List<UserParty> userParties,
                                                              Dog dog, PetSitter petSitter) {
        userParties.forEach(u -> {
            Notification notification = notificationService.sendEmail(
                    String.format("%s 반려견에 대한 돌봄 서비스 예약이 자동 취소되었습니다. [펫시터] %s", dog.getDogName(),
                            petSitter.getPetSitterName()),
                    "/usagelist",
                    NotificationType.반려인_예약취소, u.getUser());
            notificationService.saveNotification(notification);
        });
    }

    private void sendCancelReservationNotificationByEmail(List<UserParty> userParties,
                                                          Dog dog, PetSitter petSitter) {
        userParties.forEach(u -> {
            Notification notification = notificationService.sendEmail(
                    String.format("%s 반려견에 대한 돌봄 서비스 예약이 취소되었습니다. [펫시터] %s", dog.getDogName(),
                            petSitter.getPetSitterName()),
                    "/usagelist",
                    NotificationType.반려인_예약취소, u.getUser());
            notificationService.saveNotification(notification);
        });
    }

    private void sendCancelReservationNotificationByEmailByPetSitter(List<UserParty> userParties,
                                                                     Dog dog, PetSitter petSitter) {
        userParties.forEach(u -> {
            Notification notification = notificationService.sendEmail(
                    String.format("%s 반려견에 대한 돌봄 서비스 예약을 거절되었습니다. [펫시터] %s", dog.getDogName(),
                            petSitter.getPetSitterName()),
                    "/usagelist",
                    NotificationType.반려인_예약취소, u.getUser());
            notificationService.saveNotification(notification);
        });
    }
     */
}
