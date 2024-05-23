package com.ajou_nice.with_pet.petsitter.model.dto;


import com.ajou_nice.with_pet.critical_service.model.entity.PetSitterCriticalService;
import com.ajou_nice.with_pet.withpet_service.model.entity.PetSitterWithPetService;
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
public class PetSitterServiceResponse {

	private Long petSitterServiceId;

	private Long serviceId;
	private String serviceName;
	private String serviceIntroduction;
	private String serviceImg;

	private int price;

	public static List<PetSitterServiceResponse> toList(List<PetSitterWithPetService> petSitterServiceList){
		return petSitterServiceList.stream().map(petSitterService -> PetSitterServiceResponse.builder()
				.petSitterServiceId(petSitterService.getId())
				.serviceId(petSitterService.getWithPetService().getId())
				.serviceName(petSitterService.getWithPetService().getServiceName())
				.serviceIntroduction(petSitterService.getWithPetService().getIntroduction())
				.serviceImg(petSitterService.getWithPetService().getServiceImg())
				.price(petSitterService.getPrice())
				.build()).collect(Collectors.toList());
	}

	@AllArgsConstructor
	@NoArgsConstructor
	@Getter
	@Builder
	@ToString
	public static class PetSitterCriticalServiceResponse{
		private Long petSitterServiceId;
		private Long serviceId;
		private String serviceName;
		private String serviceIntroduction;
		private String serviceImg;
		private int price;

		public static List<PetSitterCriticalServiceResponse> toList(List<PetSitterCriticalService> petSitterCriticalServices){
			return petSitterCriticalServices.stream().map(petSitterCriticalService-> PetSitterCriticalServiceResponse.builder()
					.petSitterServiceId(petSitterCriticalService.getId())
					.serviceId(petSitterCriticalService.getCriticalService().getId())
					.serviceName(petSitterCriticalService.getCriticalService().getServiceName())
					.serviceIntroduction(petSitterCriticalService.getCriticalService().getIntroduction())
					.serviceImg(petSitterCriticalService.getCriticalService().getServiceImg())
					.price(petSitterCriticalService.getPrice())
					.build()).collect(Collectors.toList());
		}
	}
}
