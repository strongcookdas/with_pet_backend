package com.ajou_nice.with_pet.controller;


import com.ajou_nice.with_pet.domain.dto.Response;
import com.ajou_nice.with_pet.domain.dto.kakaopay.PayApproveResponse;
import com.ajou_nice.with_pet.domain.dto.kakaopay.PayCancelResponse;
import com.ajou_nice.with_pet.domain.dto.kakaopay.PayReadyResponse;
import com.ajou_nice.with_pet.service.KaKaoPayService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping("/payment")
@RequiredArgsConstructor
public class KaKaoPayController {

	private final KaKaoPayService kaKaoPayService;

	//결제 요청 -> 클라이언트에서는
	@PostMapping("/ready")
	public Response<PayReadyResponse> readyToKakaoPay(@ApiIgnore Authentication authentication){

		//reservation과 동기화 필요
		PayReadyResponse payReadyResponse = kaKaoPayService.payReady(authentication.getName(), (long)1);
		return Response.success(payReadyResponse);
	}

	//결제 성공
	@GetMapping("/success")
	public Response<PayApproveResponse> afterPay(@ApiIgnore Authentication authentication,@RequestParam("pg_token") String pgToken){
		PayApproveResponse payApproveResponse = kaKaoPayService.approveResponse(
				authentication.getName(), pgToken);
		return Response.success(payApproveResponse);
	}


	//결제 진행 중 취소
	@GetMapping("/cancel")
	public void cancel(@ApiIgnore Authentication authentication){
		kaKaoPayService.cancelPayment(authentication.getName());
	}

	//결제 실패
	@GetMapping("/fail")
	public void failPayment(@ApiIgnore Authentication authentication){
		kaKaoPayService.failPayment(authentication.getName());
	}

	//결제 환불
	@PostMapping("/refund")
	public Response<PayCancelResponse> refundPay(@ApiIgnore Authentication authentication){

		PayCancelResponse payCancelResponse = kaKaoPayService.refundPayment(authentication.getName());

		return Response.success(payCancelResponse);
	}



}
