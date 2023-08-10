package com.ajou_nice.with_pet.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class CriticalServiceRequest {
    private Long serviceId;
    private Integer price;
}
