package com.ajou_nice.with_pet.dto.petsitter_critical_service;

import com.ajou_nice.with_pet.domain.entity.PetSitterCriticalService;
import com.ajou_nice.with_pet.repository.PetSitterCriticalServiceRepository;
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

    private Long petSitterCriticalServiceId;
    private Long criticalServiceId;
    private String criticalServiceName;
    private String criticalServiceIntroduction;
    private String criticalServiceImg;
    private Integer criticalServicePrice;

    public static PetSitterCriticalServiceResponse of(PetSitterCriticalService petSitterCriticalService) {
        return PetSitterCriticalServiceResponse.builder()
                .petSitterCriticalServiceId(petSitterCriticalService.getId())
                .criticalServiceId(
                        petSitterCriticalService.getCriticalService().getCriticalServiceId())
                .criticalServiceIntroduction(
                        petSitterCriticalService.getCriticalService().getIntroduction())
                .criticalServiceImg(petSitterCriticalService.getCriticalService().getImage())
                .criticalServicePrice(petSitterCriticalService.getPrice())
                .build();
    }
}
