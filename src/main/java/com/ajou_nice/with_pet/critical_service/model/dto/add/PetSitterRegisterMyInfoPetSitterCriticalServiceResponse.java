package com.ajou_nice.with_pet.critical_service.model.dto.add;

import com.ajou_nice.with_pet.critical_service.model.entity.PetSitterCriticalService;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@ToString
public class PetSitterRegisterMyInfoPetSitterCriticalServiceResponse {
    private Long petSitterCriticalServiceId;
    private Long criticalServiceId;
    private String criticalServiceName;
    private String criticalServiceIntroduction;
    private String criticalServiceImg;
    private int petSitterCriticalServicePrice;

    public static List<PetSitterRegisterMyInfoPetSitterCriticalServiceResponse> toList(List<PetSitterCriticalService> petSitterCriticalServices) {
        return petSitterCriticalServices.stream().map(petSitterCriticalService -> PetSitterRegisterMyInfoPetSitterCriticalServiceResponse.builder()
                .petSitterCriticalServiceId(petSitterCriticalService.getId())
                .criticalServiceId(petSitterCriticalService.getCriticalService().getId())
                .criticalServiceName(petSitterCriticalService.getCriticalService().getServiceName())
                .criticalServiceIntroduction(petSitterCriticalService.getCriticalService().getIntroduction())
                .criticalServiceImg(petSitterCriticalService.getCriticalService().getServiceImg())
                .petSitterCriticalServicePrice(petSitterCriticalService.getPrice())
                .build()).collect(Collectors.toList());
    }
}
