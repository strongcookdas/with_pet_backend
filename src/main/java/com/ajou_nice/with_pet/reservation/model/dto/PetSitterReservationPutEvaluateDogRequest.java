package com.ajou_nice.with_pet.reservation.model.dto;

import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@ToString
public class PetSitterReservationPutEvaluateDogRequest {

    @NotBlank
    private int dogSocialTemperatureQ1;
    @NotBlank
    private int dogSocialTemperatureQ2;
    @NotBlank
    private int dogSocialTemperatureQ3;
    @NotBlank
    private int dogSocialTemperatureQ4;
    @NotBlank
    private int dogSocialTemperatureQ5;
}
