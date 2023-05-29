package com.ajou_nice.with_pet.domain.dto.kakaopay;


import java.time.LocalDateTime;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class PayReadyResponse {

	private String tid;	//결제건의 고유번호
	private String next_redirect_pc_url;
	private String partner_order_id; //결제건에 대한 가맹점의 주문번호
	private LocalDateTime created_at; //언제 결제를 요청했는지
}
