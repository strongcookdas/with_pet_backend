package com.ajou_nice.with_pet.dto.petsitter_withpet_service;

import com.ajou_nice.with_pet.domain.entity.PetSitterWithPetService;
import com.ajou_nice.with_pet.domain.entity.WithPetService;
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
public class PetSitterWithPetServiceCreateResponse {

    private Boolean isPetSitterService;
    private Long petSitterServiceId;
    private Long serviceId;
    private String serviceName;
    private String serviceIntroduction;
    private String serviceImg;
    private Integer servicePrice;

    public static List<PetSitterWithPetServiceCreateResponse> toList(
            List<PetSitterWithPetService> petSitterServiceList) {
        return petSitterServiceList.stream()
                .map(petSitterService -> PetSitterWithPetServiceCreateResponse.builder()
                        .petSitterServiceId(petSitterService.getId())
                        .serviceId(petSitterService.getWithPetService().getServiceId())
                        .serviceName(petSitterService.getWithPetService().getName())
                        .serviceIntroduction(petSitterService.getWithPetService().getIntroduction())
                        .serviceImg(petSitterService.getWithPetService().getImage())
                        .servicePrice(petSitterService.getPrice())
                        .build()).collect(Collectors.toList());
    }

    public static PetSitterWithPetServiceCreateResponse of(
            PetSitterWithPetService petSitterWithPetService) {
        return PetSitterWithPetServiceCreateResponse.builder()
                .isPetSitterService(true)
                .petSitterServiceId(petSitterWithPetService.getId())
                .serviceId(petSitterWithPetService.getWithPetService().getServiceId())
                .serviceName(petSitterWithPetService.getWithPetService().getName())
                .serviceIntroduction(petSitterWithPetService.getWithPetService().getIntroduction())
                .serviceImg(petSitterWithPetService.getWithPetService().getImage())
                .servicePrice(petSitterWithPetService.getPrice())
                .build();
    }

    public static PetSitterWithPetServiceCreateResponse of(
            WithPetService withPetService) {
        return PetSitterWithPetServiceCreateResponse.builder()
                .isPetSitterService(false)
                .petSitterServiceId(null)
                .serviceId(withPetService.getServiceId())
                .serviceName(withPetService.getName())
                .serviceIntroduction(withPetService.getIntroduction())
                .serviceImg(withPetService.getImage())
                .servicePrice(null)
                .build();
    }
}
