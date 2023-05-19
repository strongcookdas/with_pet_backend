package com.ajou_nice.with_pet.controller;


import com.ajou_nice.with_pet.domain.dto.kakaopay.PayReadyResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping("/payment")
@RequiredArgsConstructor
public class KaKaoPayController {

	//결제 요청 -> 클라이언트에서는
	@PostMapping("/ready")
	public PayReadyResponse readyToKakaoPay(@ApiIgnore Authentication authentication){


	}

	//결제 성공
	@GetMapping("/success")


	//결제 진행 중 취소
	@GetMapping("/cancel")

	//결제 실패
	@GetMapping("/fail")
}
