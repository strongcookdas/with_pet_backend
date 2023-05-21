package com.ajou_nice.with_pet.service;

import com.ajou_nice.with_pet.domain.dto.kakaopay.PayApproveResponse;
import com.ajou_nice.with_pet.domain.dto.kakaopay.PayCancelResponse;
import com.ajou_nice.with_pet.domain.dto.kakaopay.PayReadyResponse;
import com.ajou_nice.with_pet.domain.entity.Reservation;
import com.ajou_nice.with_pet.domain.entity.User;
import com.ajou_nice.with_pet.exception.AppException;
import com.ajou_nice.with_pet.exception.ErrorCode;
import com.ajou_nice.with_pet.repository.ReservationRepository;
import com.ajou_nice.with_pet.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Slf4j
@RequiredArgsConstructor
@Service
public class KaKaoPayService {

	private final UserRepository userRepository;
	private final ReservationRepository reservationRepository;

	static final String cid = "TC0ONETIME"; //테스트 코드
	static final String admin_Key = "$df8802c35fc381b40d27d3646109831e"; //장승현이 부여받은 admin key

	private PayReadyResponse payReadyResponse;
	private HttpHeaders getHeaders(){
		//카카오 페이 서버로 요청할 header
		HttpHeaders httpHeaders = new HttpHeaders();

		String auth = "KaKaoAK " + admin_Key;
		httpHeaders.set("Authorization", auth);
		httpHeaders.set("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

		return httpHeaders;
	}
	@Transactional
	public PayReadyResponse payReady(String userId, Long reservationId){

		//cost계산은 여기서
		int cost = 0;

		User findUser = userRepository.findById(userId).orElseThrow(()->{
			throw new AppException(ErrorCode.USER_NOT_FOUND, ErrorCode.USER_NOT_FOUND.getMessage());
		});

		Reservation reservation = reservationRepository.findById(reservationId).orElseThrow(()->{
			throw new AppException(ErrorCode.RESERVATION_NOT_FOUND, ErrorCode.RESERVATION_NOT_FOUND.getMessage());
		});
		

		// 카카오페이 요청 양식
		MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
		parameters.add("cid", cid);
		parameters.add("partner_order_id", "가맹점 주문 번호");
		parameters.add("partner_user_id", "가맹점 회원 ID");
		parameters.add("item_name", "펫시터 예약");
		parameters.add("quantity", "1");
		parameters.add("total_amount", "총 금액");
		parameters.add("vat_amount", "0");
		parameters.add("tax_free_amount", "0");
		parameters.add("approval_url", "http://localhost:8080/payment/success"); // 성공 시 redirect url
		parameters.add("cancel_url", "http://localhost:8080/payment/cancel"); // 취소 시 redirect url
		parameters.add("fail_url", "http://localhost:8080/payment/fail"); // 실패 시 redirect url
		//redirect url의 경우 나중에 연동시 프론트에서의 URL을 입력해주고 , 꼭 내가 도메인 변경을 해주어야 한다.

		//파라미터와 header설정
		HttpEntity<MultiValueMap<String, String>> requests = new HttpEntity<>(parameters, this.getHeaders());

		//외부에 보낼 url
		RestTemplate restTemplate = new RestTemplate();

		payReadyResponse = restTemplate.postForObject(
				"https://kapi.kakao.com/v1/payment/ready",
				requests, com.ajou_nice.with_pet.domain.dto.kakaopay.PayReadyResponse.class);

		return payReadyResponse;
	}


	//결제 완료
	public PayApproveResponse approveResponse(String userId, String pgToken){

		User findUser = userRepository.findById(userId).orElseThrow(()->{
			throw new AppException(ErrorCode.USER_NOT_FOUND, ErrorCode.USER_NOT_FOUND.getMessage());
		});

		// 카카오 요청
		MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
		parameters.add("cid", cid);
		parameters.add("tid", payReadyResponse.getTid());
		parameters.add("partner_order_id", "가맹점 주문 번호");
		parameters.add("partner_user_id", "가맹점 회원 ID");
		parameters.add("pg_token", pgToken);

		// 파라미터, 헤더
		HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(parameters, this.getHeaders());

		// 외부에 보낼 url
		RestTemplate restTemplate = new RestTemplate();

		PayApproveResponse approveResponse = restTemplate.postForObject(
				"https://kapi.kakao.com/v1/payment/approve",
				requestEntity,
				PayApproveResponse.class);
		return approveResponse;
	}

	//결제 진행 중 취소
	public void cancelPayment(String userId){
		User findUser = userRepository.findById(userId).orElseThrow(()->{
			throw new AppException(ErrorCode.USER_NOT_FOUND, ErrorCode.USER_NOT_FOUND.getMessage());
		});

		throw new AppException(ErrorCode.PAYMENT_CANCEL, ErrorCode.PAYMENT_CANCEL.getMessage());
	}

	//결제 실패
	public void failPayment(String userId){
		User findUser = userRepository.findById(userId).orElseThrow(()->{
			throw new AppException(ErrorCode.USER_NOT_FOUND, ErrorCode.USER_NOT_FOUND.getMessage());
		});

		throw new AppException(ErrorCode.PAYMENT_FAIL, ErrorCode.PAYMENT_FAIL.getMessage());
	}

	//결제 환불
	public PayCancelResponse refundPayment(String userId){
		User findUser = userRepository.findById(userId).orElseThrow(()->{
			throw new AppException(ErrorCode.USER_NOT_FOUND, ErrorCode.USER_NOT_FOUND.getMessage());
		});
		// 카카오페이 요청
		MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
		parameters.add("cid", cid);
		parameters.add("tid", "환불할 결제 고유 번호");
		parameters.add("cancel_amount", "환불 금액");
		parameters.add("cancel_tax_free_amount", "환불 비과세 금액");
		parameters.add("cancel_vat_amount", "환불 부가세");

		// 파라미터, 헤더
		HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(parameters, this.getHeaders());

		// 외부에 보낼 url
		RestTemplate restTemplate = new RestTemplate();

		PayCancelResponse payCancelResponse = restTemplate.postForObject(
				"https://kapi.kakao.com/v1/payment/cancel",
				requestEntity,
				PayCancelResponse.class);

		return payCancelResponse;
	}
}
