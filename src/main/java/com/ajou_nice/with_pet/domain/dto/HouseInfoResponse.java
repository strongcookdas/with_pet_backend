package com.ajou_nice.with_pet.domain.dto;


import com.ajou_nice.with_pet.domain.entity.House;
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
public class HouseInfoResponse {

	private Long houseId;

	private String houseImg;

	public static HouseInfoResponse of(House house){
		return HouseInfoResponse.builder()
				.houseId(house.getId())
				.houseImg(house.getHouse_img())
				.build();
	}

	public static List<HouseInfoResponse> toList(List<House> houseList){
		return houseList.stream().map(house -> HouseInfoResponse.builder()
				.houseId(house.getId())
				.houseImg(house.getHouse_img())
				.build()).collect(Collectors.toList());
	}
}
