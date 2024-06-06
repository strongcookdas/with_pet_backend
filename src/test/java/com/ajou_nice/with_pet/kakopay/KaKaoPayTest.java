package com.ajou_nice.with_pet.kakopay;


import com.ajou_nice.with_pet.controller.KaKaoPayController;
import com.ajou_nice.with_pet.domain.dto.kakaopay.PayReadyResponse;
import com.ajou_nice.with_pet.petsitter.model.entity.PetSitter;
import com.ajou_nice.with_pet.reservation.model.entity.Reservation;
import com.ajou_nice.with_pet.domain.entity.User;
import com.ajou_nice.with_pet.domain.entity.embedded.Address;
import com.ajou_nice.with_pet.enums.UserRole;
import com.ajou_nice.with_pet.petsitter.repository.PetSitterRepository;
import com.ajou_nice.with_pet.reservation.repository.ReservationRepository;
import com.ajou_nice.with_pet.repository.UserRepository;
import com.ajou_nice.with_pet.service.KaKaoPayService;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@SpringBootTest
public class KaKaoPayTest {

	@Autowired
	ReservationRepository reservationRepository;

	@Autowired
	KaKaoPayService kaKaoPayService;

	@Autowired
	UserRepository userRepository;

	@Autowired
	PetSitterRepository petSitterRepository;

	@Autowired
	KaKaoPayController kaKaoPayController;

	static final String cid = "TC0ONETIME"; //테스트 코드
	static final String admin_Key = "059c166f6891b7508def2a190d83955f";
	private PayReadyResponse payReadyResponse;
	private String next_url;

	static{
		System.setProperty("com.amazonaws.sdk.disableEc2Metadata", "true");
	}

	public HttpHeaders getHeaders(){
		//카카오 페이 서버로 요청할 header
		HttpHeaders httpHeaders = new HttpHeaders();

		String auth = "KakaoAK " + admin_Key;
		httpHeaders.set("Authorization", auth);
		httpHeaders.set("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
		return httpHeaders;
	}

	public void simplePayReady(Reservation reservation){
		// 카카오페이 요청 양식
		MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
		parameters.add("cid", cid);
		parameters.add("partner_order_id", reservation.getPetSitter().getId().toString());
		parameters.add("partner_user_id", reservation.getUser().getId().toString());
		parameters.add("item_name", "펫시터 예약");
		parameters.add("quantity", "1");
		parameters.add("total_amount", reservation.getReservationTotalPrice().toString());
		parameters.add("vat_amount", "0");
		parameters.add("tax_free_amount", "0");
		parameters.add("approval_url", "http://localhost:8080/payment/test/"+reservation.getPetSitter().getId().toString()); // 성공 시 redirect url -> 이 부분을 프론트엔드 url로 바꿔주어야 함
		parameters.add("cancel_url", "http://ec2-13-209-73-128.ap-northeast-2.compute.amazonaws.com:8080/payment-cancel"); // 취소 시 redirect url -> 서버의 주소
		parameters.add("fail_url", "http://ec2-13-209-73-128.ap-northeast-2.compute.amazonaws.com:8080/payment-fail"); // 실패 시 redirect url -> 서버의 주소
		//redirect url의 경우 나중에 연동시 프론트에서의 URL을 입력해주고 , 꼭 내가 도메인 변경을 해주어야 한다.

		//파라미터와 header설정
		HttpEntity<MultiValueMap<String, String>> requests = new HttpEntity<>(parameters, this.getHeaders());

		// RestTemplate 객체 생성
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());

		//restTemplate를 이용한 api 호출 카카오 페이
		payReadyResponse = restTemplate.postForObject(
				"https://kapi.kakao.com/v1/payment/ready",
				requests, PayReadyResponse.class);

		reservation.updateTid(payReadyResponse.getTid());
		next_url = payReadyResponse.getNext_redirect_pc_url();
	}

	@Transactional
	@Test
	public void kaKaoPayTest() throws Exception {
		//given
		//예약 initialize
		Address address = Address.simpleAddressGenerator("213","adasd", "244");
		User user1 = User.simpleUserForTest("장승현", "simpleuser", "1234", "jason5102@ajou.ac.kr",
				UserRole.ROLE_USER, "010-3931-5102", address);
		User user2 = User.simpleUserForTest("장승현2", "simplepetsitter", "1234", "jason5102@ajou.ac.kr",
				UserRole.ROLE_PETSITTER, "010-3931-5102", address);

		userRepository.save(user1); userRepository.save(user2);

		PetSitter petSitter1 = PetSitter.simplePetSitterForTest(user2.getName(), user2.getPhone(), "www.google.com", "213", "sdfs",
				"dsfds", user2);
		petSitterRepository.save(petSitter1);

		LocalDateTime checkIn = LocalDateTime.of(2023, 6, 5, 5, 13);
		LocalDateTime checkOut = LocalDateTime.of(2023, 6, 6, 6, 13);

		Reservation reservation = Reservation.forSimpleTest(checkIn, checkOut, user1, petSitter1,
				30000);
		reservationRepository.save(reservation);
		//when (kakao pay simplePayReady method 호출했을때)
		simplePayReady(reservation);
		//then (url 잘 받아오는 지 test)
		System.out.println(next_url);
	}
}
