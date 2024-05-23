package com.ajou_nice.with_pet.critical_service.model.dto.update;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class PetSitterUpdateCriticalServiceRequest {
    private Long criticalServiceId;
    private int petSitterCriticalServicePrice;
}
