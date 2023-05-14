package com.ajou_nice.with_pet.domain.dto.criticalservice;


import com.ajou_nice.with_pet.domain.entity.CriticalService;
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
public class CriticalServiceResponse {

	private Long serviceId;
	private String serviceName;
	private String serviceImg;
	private String serviceIntroduction;


	public static CriticalServiceResponse of(CriticalService criticalService){
		return CriticalServiceResponse.builder()
				.serviceId(criticalService.getId())
				.serviceName(criticalService.getServiceName())
				.serviceImg(criticalService.getServiceImg())
				.serviceIntroduction(criticalService.getIntroduction())
				.build();
	}

	public static List<CriticalServiceResponse> toList(List<CriticalService> criticalServices){
		return criticalServices.stream().map(criticalService->CriticalServiceResponse.builder()
				.serviceId(criticalService.getId())
				.serviceName(criticalService.getServiceName())
				.serviceImg(criticalService.getServiceImg())
				.serviceIntroduction(criticalService.getIntroduction())
				.build()).collect(Collectors.toList());
	}
}
