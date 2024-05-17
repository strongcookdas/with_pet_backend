package com.ajou_nice.with_pet.petsitter.model.dto;


import java.util.List;
import javax.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public class PetSitterRequest {	//펫시터가 보내는 요청 DTO

	@AllArgsConstructor
	@NoArgsConstructor
	@Getter
	@ToString
	public static class PetSitterHouseRequest{

		private Long houseId;
		@Lob
		private String houseImg;
		private Boolean representative;
	}

	@AllArgsConstructor
	@NoArgsConstructor
	@Getter
	@ToString
	public static class PetSitterHashTagRequest{ // 사용하지 않으면 삭제 예정

		private Long petSitterhashTagId;
		private String hashTagName;
	}

	@AllArgsConstructor
	@NoArgsConstructor
	@Getter
	@ToString
	public static class PetSitterCriticalServiceRequest{//사용하지 않으면 삭제 예정
		private Long serviceId;
		private int price;
		private String serviceName;
	}
}
