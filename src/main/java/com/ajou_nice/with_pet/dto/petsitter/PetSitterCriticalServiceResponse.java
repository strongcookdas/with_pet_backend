package com.ajou_nice.with_pet.dto.petsitter;

import com.ajou_nice.with_pet.domain.entity.PetSitterCriticalService;
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
public class PetSitterCriticalServiceResponse {

    private Boolean isPetSitterCriticalServiceResponse;
    private Long petSitterCriticalServiceId;
    private Long criticalServiceId;
    private String criticalServiceName;
    private String criticalServiceIntroduction;
    private String criticalServiceImg;
    private Integer criticalServicePrice;

    public static List<PetSitterCriticalServiceResponse> toList(
            List<PetSitterCriticalService> petSitterCriticalServices) {
        return petSitterCriticalServices.stream()
                .map(petSitterCriticalService -> PetSitterCriticalServiceResponse.builder()
                        .petSitterCriticalServiceId(petSitterCriticalService.getId())
                        .criticalServiceId(petSitterCriticalService.getCriticalService()
                                .getCriticalServiceId())
                        .criticalServiceName(
                                petSitterCriticalService.getCriticalService().getName())
                        .criticalServiceIntroduction(
                                petSitterCriticalService.getCriticalService().getIntroduction())
                        .criticalServiceImg(
                                petSitterCriticalService.getCriticalService().getImage())
                        .criticalServicePrice(petSitterCriticalService.getPrice())
                        .build()).collect(Collectors.toList());
    }
}
