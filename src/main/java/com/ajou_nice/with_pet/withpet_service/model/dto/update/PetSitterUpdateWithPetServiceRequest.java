package com.ajou_nice.with_pet.withpet_service.model.dto.update;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class PetSitterUpdateWithPetServiceRequest {
    private Long serviceId;
    private int servicePrice;
}
