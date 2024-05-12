package com.ajou_nice.with_pet.house.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class PetSitterAddHouseRequest {
    @NotBlank
    private String houseImg;
    @NotBlank
    private Boolean representative;
}
