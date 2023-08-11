package com.ajou_nice.with_pet.dto.criticalservice;

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
public class CriticalServiceRequest {
    private Long criticalServiceId;
    private Integer price;
}
