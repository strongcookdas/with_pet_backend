package com.ajou_nice.with_pet.dto.withpet_service;

import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@ToString
public class ServiceRequest {

    @NotNull
    private Long serviceId;
    @NotNull
    private Integer price;
}
