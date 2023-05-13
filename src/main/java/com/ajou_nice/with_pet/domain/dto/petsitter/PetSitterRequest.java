package com.ajou_nice.with_pet.domain.dto.petsitter;


import java.util.List;
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
	public static class PetSitterModifyInfoRequest{

		private List<PetSitterHouseRequest> petSitterHouseRequests;
		private List<PetSitterHashTagRequest> petSitterHashTagRequests;
		private String introduction;
		private List<PetSitterServiceRequest> petSitterServiceRequests;

	}

	@AllArgsConstructor
	@NoArgsConstructor
	@Getter
	public static class PetSitterHouseRequest{
		private String houseImg;
		private Boolean representative;
	}

	@AllArgsConstructor
	@NoArgsConstructor
	@Getter
	public static class PetSitterHashTagRequest{
		private String hasTagName;
	}


	@AllArgsConstructor
	@NoArgsConstructor
	@Getter
	public static class PetSitterServiceRequest{
		private Long serviceIds;
		private int price;
	}
}
