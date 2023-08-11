package com.ajou_nice.with_pet.dto.petsitter_critical_service;

import com.ajou_nice.with_pet.domain.entity.CriticalService;
import com.ajou_nice.with_pet.domain.entity.PetSitterCriticalService;
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
public class PetSitterCriticalServiceCreateResponse {

    private Boolean isPetSitterCriticalService;
    private Long petSitterCriticalServiceId;
    private Long criticalServiceId;
    private String criticalServiceName;
    private String criticalServiceIntroduction;
    private String criticalServiceImg;
    private Integer criticalServicePrice;

    public static PetSitterCriticalServiceCreateResponse of(
            PetSitterCriticalService petSitterCriticalService) {
        return PetSitterCriticalServiceCreateResponse.builder()
                .isPetSitterCriticalService(true)
                .petSitterCriticalServiceId(
                        petSitterCriticalService.getId())
                .criticalServiceId(petSitterCriticalService.getCriticalService().getCriticalServiceId())
                .criticalServiceIntroduction(
                        petSitterCriticalService.getCriticalService().getIntroduction())
                .criticalServiceImg(
                        petSitterCriticalService.getCriticalService().getImage())
                .criticalServicePrice(petSitterCriticalService.getPrice())
                .build();
    }

    public static PetSitterCriticalServiceCreateResponse of(
            CriticalService criticalService) {
        return PetSitterCriticalServiceCreateResponse.builder()
                .isPetSitterCriticalService(false)
                .petSitterCriticalServiceId(null)
                .criticalServiceId(
                        criticalService.getCriticalServiceId())
                .criticalServiceIntroduction(
                        criticalService.getIntroduction())
                .criticalServiceImg(criticalService.getImage())
                .criticalServicePrice(null)
                .build();
    }
}
