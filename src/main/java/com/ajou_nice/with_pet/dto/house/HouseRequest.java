package com.ajou_nice.with_pet.dto.house;

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
public class HouseRequest {
    private String houseImg;
    private Boolean isRepresentative;
}
