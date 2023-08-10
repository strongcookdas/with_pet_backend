package com.ajou_nice.with_pet.dto.service;

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

    private Long serviceId;
    private Integer price;
}
