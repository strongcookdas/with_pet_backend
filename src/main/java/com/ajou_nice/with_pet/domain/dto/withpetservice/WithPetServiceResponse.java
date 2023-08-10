package com.ajou_nice.with_pet.domain.dto.withpetservice;

import com.ajou_nice.with_pet.domain.entity.Service;
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


	public static WithPetServiceResponse of(Service service){
		return WithPetServiceResponse.builder()
				.serviceId(service.getServiceId())
				.serviceName(service.getName())
				.serviceImg(service.getImage())
				.serviceIntroduction(service.getIntroduction())
				.build();
	}

	public static List<WithPetServiceResponse> toList(List<Service> serviceList){
		return serviceList.stream().map(withPetService-> WithPetServiceResponse.builder()
				.serviceId(withPetService.getServiceId())
				.serviceName(withPetService.getName())
				.serviceImg(withPetService.getImage())
				.serviceIntroduction(withPetService.getIntroduction())
				.build()).collect(Collectors.toList());
	}
}
