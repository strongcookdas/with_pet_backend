package com.ajou_nice.with_pet.reservation.model.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class PetSitterReservationPutApprovalRequest {
    @NotNull
    private Long reservationId;
    @NotBlank
    private String reservationStatus;
}
