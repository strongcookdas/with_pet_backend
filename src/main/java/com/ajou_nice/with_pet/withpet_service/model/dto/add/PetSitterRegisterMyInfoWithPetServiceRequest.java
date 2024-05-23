package com.ajou_nice.with_pet.withpet_service.model.dto.add;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class PetSitterRegisterMyInfoWithPetServiceRequest {
    @NotNull
    private Long withPetServiceId;
    @NotNull
    private int petSitterWithPetServicePrice;
}
