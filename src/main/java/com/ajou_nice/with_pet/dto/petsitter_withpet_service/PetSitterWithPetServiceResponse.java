package com.ajou_nice.with_pet.dto.petsitter_withpet_service;

import com.ajou_nice.with_pet.domain.entity.PetSitterWithPetService;
import com.ajou_nice.with_pet.domain.entity.WithPetService;
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
public class PetSitterWithPetServiceResponse {

    private Long petSitterServiceId;
    private Long serviceId;
    private String serviceName;
    private String serviceIntroduction;
    private String serviceImg;
    private Integer servicePrice;

    public static PetSitterWithPetServiceResponse of(PetSitterWithPetService petSitterWithPetService){
        return PetSitterWithPetServiceResponse.builder()
                .petSitterServiceId(petSitterWithPetService.getId())
                .serviceId(petSitterWithPetService.getWithPetService().getServiceId())
                .serviceName(petSitterWithPetService.getWithPetService().getName())
                .serviceIntroduction(petSitterWithPetService.getWithPetService().getIntroduction())
                .serviceImg(petSitterWithPetService.getWithPetService().getImage())
                .servicePrice(petSitterWithPetService.getPrice())
                .build();
    }
}
