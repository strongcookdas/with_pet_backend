package com.ajou_nice.with_pet.withpet_service.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class PetSitterAddServiceRequest {
    @NotNull
    private Long serviceId;
    @NotNull
    private int price;
}
