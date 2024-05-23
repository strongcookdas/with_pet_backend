package com.ajou_nice.with_pet.house.model.dto.get;

import com.ajou_nice.with_pet.house.model.entity.House;
import lombok.*;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@ToString
public class PetSitterGetDetailInfoHouseResponse {
    private Long petSitterHouseId;
    private String petSitterHouseImg;
    private Boolean petSitterHouseRepresentative;

    public static PetSitterGetDetailInfoHouseResponse of(House house){
        return PetSitterGetDetailInfoHouseResponse.builder()
                .petSitterHouseId(house.getId())
                .petSitterHouseImg(house.getHouse_img())
                .petSitterHouseRepresentative(house.getRepresentative())
                .build();
    }

    public static List<PetSitterGetDetailInfoHouseResponse> toList(List<House> houseList){
        return houseList.stream().map(house -> PetSitterGetDetailInfoHouseResponse.builder()
                .petSitterHouseId(house.getId())
                .petSitterHouseImg(house.getHouse_img())
                .petSitterHouseRepresentative(house.getRepresentative())
                .build()).collect(Collectors.toList());
    }
}
