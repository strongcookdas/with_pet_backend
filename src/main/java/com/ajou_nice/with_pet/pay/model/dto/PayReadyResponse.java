package com.ajou_nice.with_pet.pay.model.dto;


import java.time.LocalDateTime;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class PayReadyResponse {

	private String tid;	//결제건의 고유번호
	private String next_redirect_pc_url; //pc redirect 할 url
	private LocalDateTime created_at; //언제 결제를 요청했는지
}
