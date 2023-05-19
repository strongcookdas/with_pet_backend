package com.ajou_nice.with_pet.domain.dto.kakaopay;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class PaymentDetail {

	private int total;
	private int tax_free;
	private int vat;
	private int point;
	private int discount;
}
