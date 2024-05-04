package com.ajou_nice.with_pet.service;

import com.ajou_nice.with_pet.domain.dto.kakaopay.PayApproveResponse;
import com.ajou_nice.with_pet.domain.dto.kakaopay.PayCancelResponse;
import com.ajou_nice.with_pet.domain.dto.kakaopay.PayReadyResponse;
import com.ajou_nice.with_pet.domain.dto.kakaopay.RefundResponse;
import com.ajou_nice.with_pet.domain.entity.Dog;
import com.ajou_nice.with_pet.domain.entity.Notification;
import com.ajou_nice.with_pet.domain.entity.Pay;
import com.ajou_nice.with_pet.domain.entity.PetSitter;
import com.ajou_nice.with_pet.domain.entity.Reservation;
import com.ajou_nice.with_pet.domain.entity.User;
import com.ajou_nice.with_pet.domain.entity.UserParty;
import com.ajou_nice.with_pet.enums.NotificationType;
import com.ajou_nice.with_pet.enums.PayStatus;
import com.ajou_nice.with_pet.enums.ReservationStatus;
import com.ajou_nice.with_pet.exception.AppException;
import com.ajou_nice.with_pet.exception.ErrorCode;
import com.ajou_nice.with_pet.repository.PayRepository;
import com.ajou_nice.with_pet.repository.ReservationRepository;
import com.ajou_nice.with_pet.repository.UserPartyRepository;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Slf4j
@RequiredArgsConstructor
@Service
public class KaKaoPayService {

	private final ReservationRepository reservationRepository;
	private final PayRepository payRepository;
	private final NotificationService notificationService;
	private final UserPartyRepository userPartyRepository;

	private final ValidateCollection valid;
	static final String cid = "TC0ONETIME"; //테스트 코드
	static final String admin_Key = "059c166f6891b7508def2a190d83955f"; //장승현이 부여받은 admin key

	private PayReadyResponse payReadyResponse;

	private HttpHeaders getHeaders() {
		//카카오 페이 서버로 요청할 header
		HttpHeaders httpHeaders = new HttpHeaders();

		String auth = "KakaoAK " + admin_Key;
		httpHeaders.set("Authorization", auth);
		httpHeaders.set("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
		return httpHeaders;
	}

	//pay ready를 요청하면 pay entity 생성 x
	@Transactional
	public PayReadyResponse payReady(String userId, Long reservationId) {

		User findUser = valid.userValidationById(userId);

		Reservation reservation = valid.reservationValidation(reservationId);

		// 카카오페이 요청 양식
		MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
		parameters.add("cid", cid);
		parameters.add("partner_order_id", reservation.getPetSitter().getId().toString());
		parameters.add("partner_user_id", findUser.getId().toString());
		parameters.add("item_name", "펫시터 예약");
		parameters.add("quantity", "1");
		parameters.add("total_amount", reservation.getTotalPrice().toString());
		parameters.add("vat_amount", "0");
		parameters.add("tax_free_amount", "0");
		parameters.add("approval_url",
				"http://ec2-13-125-242-183.ap-northeast-2.compute.amazonaws.com/petsitterdetail/"
						+ reservation.getPetSitter().getId()
						.toString()); // 성공 시 redirect url -> 이 부분을 프론트엔드 url로 바꿔주어야 함
		parameters.add("cancel_url",
				"http://ec2-13-209-73-128.ap-northeast-2.compute.amazonaws.com:8080/payment-cancel"); // 취소 시 redirect url -> 서버의 주소
		parameters.add("fail_url",
				"http://ec2-13-209-73-128.ap-northeast-2.compute.amazonaws.com:8080/payment-fail"); // 실패 시 redirect url -> 서버의 주소
		//redirect url의 경우 나중에 연동시 프론트에서의 URL을 입력해주고 , 꼭 내가 도메인 변경을 해주어야 한다.

		//파라미터와 header설정
		HttpEntity<MultiValueMap<String, String>> requests = new HttpEntity<>(parameters,
				this.getHeaders());
		System.out.println(requests);
		System.out.println(requests.getHeaders());
		System.out.println(requests.getBody());

		// RestTemplate 객체 생성
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());

		//restTemplate를 이용한 api 호출 카카오 페이
		payReadyResponse = restTemplate.postForObject(
				"https://kapi.kakao.com/v1/payment/ready",
				requests, PayReadyResponse.class);

		reservation.updateTid(payReadyResponse.getTid());

		return payReadyResponse;
	}


	//결제 완료 -> 예약상태 payed , payed 생성 후 success update
	@Transactional
	public PayApproveResponse approvePay(String userId, String pgToken, String tid) {

		//사용자 인증 체크
		valid.userValidationById(userId);

		Reservation reservation = reservationRepository.findByTid(tid).orElseThrow(() -> {
			throw new AppException(ErrorCode.RESERVATION_NOT_FOUND,
					ErrorCode.RESERVATION_NOT_FOUND.getMessage());
		});

		// 카카오 요청
		MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
		parameters.add("cid", cid);
		parameters.add("tid", tid);
		parameters.add("partner_order_id", reservation.getPetSitter().getId().toString());
		parameters.add("partner_user_id", reservation.getUser().getId().toString());
		parameters.add("pg_token", pgToken);

		// 파라미터, 헤더
		HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(parameters,
				this.getHeaders());

		// RestTemplate 객체 생성
		RestTemplate restTemplate = new RestTemplate();

		//restTemplate를 이용한 api 호출 카카오 페이
		PayApproveResponse approveResponse = restTemplate.postForObject(
				"https://kapi.kakao.com/v1/payment/approve",
				requestEntity,
				PayApproveResponse.class);

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
		Pay pay = payRepository.findByReservation(reservation).orElseThrow(() -> {
			throw new AppException(ErrorCode.PAY_NOT_FOUND, ErrorCode.PAY_NOT_FOUND.getMessage());
		});

		// 카카오페이 요청
		MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
		parameters.add("cid", cid);
		parameters.add("tid", pay.getTid());
		parameters.add("cancel_amount", pay.getPay_amount().toString());
		parameters.add("cancel_tax_free_amount", Integer.toString(0));

		// 파라미터, 헤더
		HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(parameters,
				this.getHeaders());

		// 외부에 보낼 url
		RestTemplate restTemplate = new RestTemplate();

		PayCancelResponse payCancelResponse = restTemplate.postForObject(
				"https://kapi.kakao.com/v1/payment/cancel",
				requestEntity,
				PayCancelResponse.class);

		//pay 상태 refund update -> soft delete 기법 사용 (삭제는 하지 않는다) -> 추후 환불에 대한 내역을 볼 수 있도록
		pay.refund(PayStatus.REFUND, payCancelResponse.getCanceled_amount().getTotal(),
				payCancelResponse.getCanceled_at());

		List<UserParty> userParties = userPartyRepository.findAllByParty(reservation.getDog()
				.getParty());
		sendAutoCancelReservationNotificationByEmail(userParties, reservation.getDog(),
				reservation.getPetSitter());
	}

	private void sendAutoCancelReservationNotificationByEmail(List<UserParty> userParties,
			Dog dog, PetSitter petSitter) {
		userParties.forEach(u -> {
			Notification notification = notificationService.sendEmail(
					String.format("%s 반려견에 대한 돌봄 서비스 예약이 자동 취소되었습니다. [펫시터] %s", dog.getName(),
							petSitter.getPetSitterName()),
					"/usagelist",
					NotificationType.반려인_예약취소, u.getUser());
			notificationService.saveNotification(notification);
		});
	}

	//결제 환불
	//예약 상태 -> cancel, pay 상태 -> Refund , pay entity 환불금액 update
	@Transactional
	public RefundResponse refundPayment(String userId, Long reservationId) {

		//사용자 인증
		User findUser = valid.userValidationById(userId);

		//예약 유효 인증
		Reservation reservation = valid.reservationValidation(reservationId);

		//결제 정보 확인 인증
		Pay pay = payRepository.findByReservation(reservation).orElseThrow(() -> {
			throw new AppException(ErrorCode.PAY_NOT_FOUND, ErrorCode.PAY_NOT_FOUND.getMessage());
		});
		Period period = Period.between(LocalDate.now(), reservation.getCheckIn().toLocalDate());
		int cancel_amount = 0; //환불 금액
		List<UserParty> userParties = userPartyRepository.findAllByParty(reservation.getDog()
				.getParty());

		//만약 결제 환불을 요청한 사람이 petsitter라면 100% 환불
		//결제 환불을 요청한 사람이 user라면
		//아직 승인받지 못한 예약의 경우 100% 환불 (PAYED)
		//
		if (reservation.getPetSitter().getUser().equals(findUser)) {
			cancel_amount = pay.getPay_amount();
			sendCancelReservationNotificationByEmail(userParties, reservation.getDog(),
					reservation.getPetSitter());
		} else {
			if (reservation.getReservationStatus().equals(ReservationStatus.PAYED.toString())) {

			}
			//환불 금액 판정 -> 예약날짜가 7일 이내로 남았을 경우 50%환불, 예약날짜가 7일 초과라면 100% 환불
			if (period.getDays() > 7) {
				cancel_amount = pay.getPay_amount();
			} else if (period.getDays() <= 7) {
				cancel_amount = (int) (pay.getPay_amount() * 0.5);
			}
			sendCancelReservationNotificationByEmailByPetSitter(userParties, reservation.getDog(),
					reservation.getPetSitter());
		}

		// 카카오페이 요청
		MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
		parameters.add("cid", cid);
		parameters.add("tid", pay.getTid());
		parameters.add("cancel_amount", Integer.toString(cancel_amount));
		parameters.add("cancel_tax_free_amount", Integer.toString(0));

		// 파라미터, 헤더
		HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(parameters,
				this.getHeaders());

		// 외부에 보낼 url
		RestTemplate restTemplate = new RestTemplate();

		PayCancelResponse payCancelResponse = restTemplate.postForObject(
				"https://kapi.kakao.com/v1/payment/cancel",
				requestEntity,
				PayCancelResponse.class);

		//예약 상태 cancel update
		reservation.updateStatus(ReservationStatus.CANCEL.toString());

		//pay 상태 refund update -> soft delete 기법 사용 (삭제는 하지 않는다) -> 추후 환불에 대한 내역을 볼 수 있도록
		pay.refund(PayStatus.REFUND, payCancelResponse.getCanceled_amount().getTotal(),
				payCancelResponse.getCanceled_at());

		return RefundResponse.of(pay);
	}

	private void sendCancelReservationNotificationByEmail(List<UserParty> userParties,
			Dog dog, PetSitter petSitter) {
		userParties.forEach(u -> {
			Notification notification = notificationService.sendEmail(
					String.format("%s 반려견에 대한 돌봄 서비스 예약이 취소되었습니다. [펫시터] %s", dog.getName(),
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
					String.format("%s 반려견에 대한 돌봄 서비스 예약을 거절되었습니다. [펫시터] %s", dog.getName(),
							petSitter.getPetSitterName()),
					"/usagelist",
					NotificationType.반려인_예약취소, u.getUser());
			notificationService.saveNotification(notification);
		});
	}
}
