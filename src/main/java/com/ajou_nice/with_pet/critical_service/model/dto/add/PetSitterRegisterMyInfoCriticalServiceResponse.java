package com.ajou_nice.with_pet.critical_service.model.dto.add;

import com.ajou_nice.with_pet.critical_service.model.entity.CriticalService;
import lombok.*;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@ToString
public class PetSitterRegisterMyInfoCriticalServiceResponse {
    private Long criticalServiceId;
    private String criticalServiceName;
    private String criticalServiceImg;
    private String criticalServiceIntroduction;


    public static PetSitterRegisterMyInfoCriticalServiceResponse of(CriticalService criticalService){
        return PetSitterRegisterMyInfoCriticalServiceResponse.builder()
                .criticalServiceId(criticalService.getId())
                .criticalServiceName(criticalService.getServiceName())
                .criticalServiceImg(criticalService.getServiceImg())
                .criticalServiceIntroduction(criticalService.getIntroduction())
                .build();
    }

    public static List<PetSitterRegisterMyInfoCriticalServiceResponse> toList(List<CriticalService> criticalServices){
        return criticalServices.stream().map(criticalService->PetSitterRegisterMyInfoCriticalServiceResponse.builder()
                .criticalServiceId(criticalService.getId())
                .criticalServiceName(criticalService.getServiceName())
                .criticalServiceImg(criticalService.getServiceImg())
                .criticalServiceIntroduction(criticalService.getIntroduction())
                .build()).collect(Collectors.toList());
    }
}
