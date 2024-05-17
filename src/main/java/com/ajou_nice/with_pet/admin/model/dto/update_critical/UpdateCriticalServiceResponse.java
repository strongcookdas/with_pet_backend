package com.ajou_nice.with_pet.admin.model.dto.update_critical;

import com.ajou_nice.with_pet.critical_service.model.entity.CriticalService;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@ToString
public class UpdateCriticalServiceResponse {
    private Long serviceId;
    private String serviceName;
    private String serviceImg;
    private String serviceIntroduction;

    public static UpdateCriticalServiceResponse of(CriticalService criticalService){
        return UpdateCriticalServiceResponse.builder()
                .serviceId(criticalService.getId())
                .serviceName(criticalService.getServiceName())
                .serviceImg(criticalService.getServiceImg())
                .serviceIntroduction(criticalService.getIntroduction())
                .build();
    }
}
