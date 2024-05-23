package com.ajou_nice.with_pet.house.model.dto.get;


import com.ajou_nice.with_pet.house.model.entity.House;
import java.util.List;
import java.util.stream.Collectors;
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
public class PetSitterRegisterMyInfoHouseResponse {

	private Long petSitterHouseId;
	private String petSitterHouseImg;
	private Boolean petSitterHouseRepresentative;

	public static PetSitterRegisterMyInfoHouseResponse of(House house){
		return PetSitterRegisterMyInfoHouseResponse.builder()
				.petSitterHouseId(house.getId())
				.petSitterHouseImg(house.getHouse_img())
				.petSitterHouseRepresentative(house.getRepresentative())
				.build();
	}

	public static List<PetSitterRegisterMyInfoHouseResponse> toList(List<House> houseList){
		return houseList.stream().map(house -> PetSitterRegisterMyInfoHouseResponse.builder()
				.petSitterHouseId(house.getId())
				.petSitterHouseImg(house.getHouse_img())
				.petSitterHouseRepresentative(house.getRepresentative())
				.build()).collect(Collectors.toList());
	}
}
