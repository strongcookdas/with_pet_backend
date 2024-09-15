package com.ajou_nice.with_pet.pay.model.dto;

import com.ajou_nice.with_pet.pay.model.entity.Pay;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class RefundResponse {

	private Long payId;

	private String reservationStatus;

	private String payStatus;

	private String userName;

	private String petSitterName;

	private String tid;

	private Integer pay_amount;

	private String item_name;

	private Integer refund_amount;

	private String approved_at;

	private String canceled_at;


	public static RefundResponse of(Pay pay){
		return RefundResponse.builder()
				.payId(pay.getId())
				.reservationStatus(pay.getReservation().getReservationStatus().toString())
				.payStatus(pay.getPayStatus().toString())
				.userName(pay.getReservation().getUser().getName())
				.petSitterName(pay.getReservation().getPetSitter().getPetSitterName())
				.tid(pay.getTid())
				.pay_amount(pay.getPay_amount())
				.item_name(pay.getItem_name())
				.refund_amount(pay.getRefund_amount())
				.approved_at(pay.getApproved_at())
				.canceled_at(pay.getCanceled_at())
				.build();
	}
}
