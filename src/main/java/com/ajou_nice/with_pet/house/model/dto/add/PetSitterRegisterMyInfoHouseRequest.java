package com.ajou_nice.with_pet.house.model.dto.add;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class PetSitterRegisterMyInfoHouseRequest {
    @NotBlank
    private String petSitterHouseImg;
    @NotBlank
    private Boolean petSitterHouseRepresentative;
}
