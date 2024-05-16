package com.ajou_nice.with_pet.petsitter.model.dto.house;

import com.ajou_nice.with_pet.house.model.dto.update.PetSitterUpdateHouseRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class PetSitterUpdateHousesRequest {
    private List<PetSitterUpdateHouseRequest> petSitterHousesRequests;
}
