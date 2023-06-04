package com.ajou_nice.with_pet.controller;


import com.ajou_nice.with_pet.service.KaKaoPayService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ViewPaymentController {

	private final KaKaoPayService kaKaoPayService;


	//결제 진행 중 취소
	@RequestMapping("/payment-cancel")
	public String cancel() {
		kaKaoPayService.deletePayment();

		return "payment-cancel";
	}

	//결제 실패
	@RequestMapping("/payment-fail")
	public String failPayment() {
		kaKaoPayService.deletePayment();

		return "payment-fail";
	}
}
