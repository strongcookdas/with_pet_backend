package com.ajou_nice.with_pet.dto.petsitter;

import com.ajou_nice.with_pet.domain.entity.PetSitterWithPetService;
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
public class PetSitterServiceResponse {

    private Boolean isPetSitterService;
    private Long petSitterServiceId;
    private Long serviceId;
    private String serviceName;
    private String serviceIntroduction;
    private String serviceImg;
    private Integer servicePrice;

    public static List<PetSitterServiceResponse> toList(List<PetSitterWithPetService> petSitterServiceList){
        return petSitterServiceList.stream().map(petSitterService -> PetSitterServiceResponse.builder()
                .petSitterServiceId(petSitterService.getId())
                .serviceId(petSitterService.getService().getServiceId())
                .serviceName(petSitterService.getService().getName())
                .serviceIntroduction(petSitterService.getService().getIntroduction())
                .serviceImg(petSitterService.getService().getImage())
                .servicePrice(petSitterService.getPrice())
                .build()).collect(Collectors.toList());
    }
}
