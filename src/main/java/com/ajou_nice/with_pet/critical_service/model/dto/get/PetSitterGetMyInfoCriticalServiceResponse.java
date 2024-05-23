package com.ajou_nice.with_pet.critical_service.model.dto.get;

import com.ajou_nice.with_pet.critical_service.model.entity.CriticalService;
import lombok.*;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@ToString
public class PetSitterGetMyInfoCriticalServiceResponse {
    private Long criticalServiceId;
    private String criticalServiceName;
    private String criticalServiceImg;
    private String criticalServiceIntroduction;


    public static PetSitterGetMyInfoCriticalServiceResponse of(CriticalService criticalService){
        return PetSitterGetMyInfoCriticalServiceResponse.builder()
                .criticalServiceId(criticalService.getId())
                .criticalServiceName(criticalService.getServiceName())
                .criticalServiceImg(criticalService.getServiceImg())
                .criticalServiceIntroduction(criticalService.getIntroduction())
                .build();
    }

    public static List<PetSitterGetMyInfoCriticalServiceResponse> toList(List<CriticalService> criticalServices){
        return criticalServices.stream().map(criticalService->PetSitterGetMyInfoCriticalServiceResponse.builder()
                .criticalServiceId(criticalService.getId())
                .criticalServiceName(criticalService.getServiceName())
                .criticalServiceImg(criticalService.getServiceImg())
                .criticalServiceIntroduction(criticalService.getIntroduction())
                .build()).collect(Collectors.toList());
    }
}
