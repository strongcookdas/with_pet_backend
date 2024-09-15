package com.ajou_nice.with_pet.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
/*
WAIT : 예약했지만 펫시터 승인 X 결제 0
APPROVAL : 펫시터 승인 O
CANCEL : 펫시터 취소
 */

@AllArgsConstructor
@Getter
public enum ReservationStatus {
    WAIT, CANCEL, APPROVAL,PAYED, DONE, AUTO_CANCEL, USE
}
