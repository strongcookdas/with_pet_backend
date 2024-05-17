package com.ajou_nice.with_pet.critical_service.model.dto.add;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class PetSitterAddCriticalServiceRequest {
    @NotNull
    private Long serviceId;
    @NotNull
    private int price;
}
