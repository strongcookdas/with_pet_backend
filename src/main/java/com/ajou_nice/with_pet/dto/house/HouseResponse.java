package com.ajou_nice.with_pet.dto.house;

import com.ajou_nice.with_pet.domain.entity.House;
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
public class HouseResponse {
    private Long houseId;
    private String houseImg;
    private Boolean isRepresentative;

    public static HouseResponse of(House house){
        return HouseResponse.builder()
                .houseId(house.getHouseId())
                .houseImg(house.getImage())
                .isRepresentative(house.getRepresentative())
                .build();
    }
}
