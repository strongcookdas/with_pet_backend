package com.ajou_nice.with_pet.house.model.dto.update;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class PetSitterUpdateHouseRequest {
    @NotBlank
    private String petSitterHouseImg;
    @NotNull
    private Boolean petSitterHouseRepresentative;
}
