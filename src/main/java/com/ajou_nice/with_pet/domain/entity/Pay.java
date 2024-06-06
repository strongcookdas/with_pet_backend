package com.ajou_nice.with_pet.domain.entity;

import com.ajou_nice.with_pet.domain.dto.kakaopay.PayApproveResponse;
import com.ajou_nice.with_pet.enums.PayStatus;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import com.ajou_nice.with_pet.reservation.model.entity.Reservation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Entity
public class Pay {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;

    //결제 상태
    @Enumerated(EnumType.STRING)
    private PayStatus payStatus;

    //결제 건의 고유번호
    private String tid;

    //결제한 금액
    private Integer pay_amount;

    //결제 승인 시간
    private String approved_at;

    //결제 상품명
    private String item_name;

    //결제 상품 수량
    private Integer quantity;

    //환불받은 금액
    private Integer refund_amount;

    private String canceled_at; // 결제가 취소된 시간



    public void refund(PayStatus payStatus, int refund_amount, String canceled_at){
        this.payStatus = payStatus;
        this.refund_amount = refund_amount;
        this.canceled_at = canceled_at;
    }

    public void approve(PayStatus payStatus, String item_name, int quantity, String approved_at){
        this.payStatus = payStatus;
        this.item_name = item_name;
        this.quantity = quantity;
        this.approved_at = approved_at;
    }

    public static Pay simplePayForTest(Reservation reservation){
        return Pay.builder()
                .reservation(reservation)
                .build();
    }

    public static Pay of(Reservation reservation, PayApproveResponse approveResponse) {
        return Pay.builder()
                .reservation(reservation)
                .payStatus(PayStatus.SUCCESS)
                .item_name(approveResponse.getItem_name())
                .tid(approveResponse.getTid())
                .quantity(approveResponse.getQuantity())
                .approved_at(approveResponse.getApproved_at())
                .pay_amount(approveResponse.getAmount().getTotal())
                .build();
    }
}
