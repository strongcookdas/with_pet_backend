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
    private String payTid;
    private Integer payAmount;
    private String payItemName;
    private Integer payRefundAmount;
    private String payApprovedAt;
    private String payCanceledAt;
    private String payStatus;
    private String reservationStatus;
    private String userName;
    private String petSitterName;


    public static RefundResponse of(Pay pay) {
        return RefundResponse.builder()
                .payId(pay.getId())
                .reservationStatus(pay.getReservation().getReservationStatus().toString())
                .payStatus(pay.getPayStatus().toString())
                .userName(pay.getReservation().getUser().getName())
                .petSitterName(pay.getReservation().getPetSitter().getPetSitterName())
                .payTid(pay.getTid())
                .payAmount(pay.getPay_amount())
                .payItemName(pay.getItem_name())
                .payRefundAmount(pay.getRefund_amount())
                .payApprovedAt(pay.getApproved_at())
                .payCanceledAt(pay.getCanceled_at())
                .build();
    }
}
