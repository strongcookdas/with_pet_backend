package com.ajou_nice.with_pet.domain.dto.petsitter;


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
	public static class PetSitterModifyInfoRequest{

		private List<PetSitterHouseRequest> petSitterHouseRequests;
		private List<PetSitterHashTagRequest> petSitterHashTagRequests;
		private String introduction;
		private List<PetSitterServiceRequest> petSitterServiceRequests;

		private List<PetSitterCriticalServiceRequest> petSitterCriticalServiceRequests;

	}

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
	public static class PetSitterHashTagRequest{

		private Long hashTagId;
		private String hashTagName;
	}


	@AllArgsConstructor
	@NoArgsConstructor
	@Getter
	@ToString
	public static class PetSitterServiceRequest{
		private Long serviceId;
		private int price;
	}

	@AllArgsConstructor
	@NoArgsConstructor
	@Getter
	@ToString
	public static class PetSitterCriticalServiceRequest{
		private Long serviceId;
		private int price;
	}
}
