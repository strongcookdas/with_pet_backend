package com.ajou_nice.with_pet.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ReservationStatus {
    WAIT, CANCEL, APPROVAL,PAYED, DONE, AUTO_CANCEL, USE
}
