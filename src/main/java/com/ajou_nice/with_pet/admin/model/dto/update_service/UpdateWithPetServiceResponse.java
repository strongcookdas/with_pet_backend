package com.ajou_nice.with_pet.admin.model.dto.update_service;

import com.ajou_nice.with_pet.withpet_service.model.entity.WithPetService;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@ToString
public class UpdateWithPetServiceResponse {
    private Long serviceId;
    private String serviceName;
    private String serviceImg;
    private String serviceIntroduction;

    public static UpdateWithPetServiceResponse of(WithPetService withPetService){
        return UpdateWithPetServiceResponse.builder()
                .serviceId(withPetService.getId())
                .serviceName(withPetService.getServiceName())
                .serviceImg(withPetService.getServiceImg())
                .serviceIntroduction(withPetService.getIntroduction())
                .build();
    }
}
