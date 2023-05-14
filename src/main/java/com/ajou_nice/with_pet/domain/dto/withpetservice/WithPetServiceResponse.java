package com.ajou_nice.with_pet.domain.dto.withpetservice;

import com.ajou_nice.with_pet.domain.entity.WithPetService;
import com.ajou_nice.with_pet.service.ApplicantService;
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
public class WithPetServiceResponse {

	private Long serviceId;
	private String serviceName;
	private String serviceImg;
	private String serviceIntroduction;


	public static WithPetServiceResponse of(WithPetService withPetService){
		return WithPetServiceResponse.builder()
				.serviceId(withPetService.getId())
				.serviceName(withPetService.getName())
				.serviceImg(withPetService.getService_Img())
				.serviceIntroduction(withPetService.getIntroduction())
				.build();
	}

	public static List<WithPetServiceResponse> toList(List<WithPetService> withPetServiceList){
		return withPetServiceList.stream().map(withPetService-> WithPetServiceResponse.builder()
				.serviceId(withPetService.getId())
				.serviceName(withPetService.getName())
				.serviceImg(withPetService.getService_Img())
				.serviceIntroduction(withPetService.getIntroduction())
				.build()).collect(Collectors.toList());
	}
}
