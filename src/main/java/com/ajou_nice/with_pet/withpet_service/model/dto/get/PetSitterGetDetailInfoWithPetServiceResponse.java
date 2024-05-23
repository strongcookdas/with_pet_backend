package com.ajou_nice.with_pet.withpet_service.model.dto.get;

import com.ajou_nice.with_pet.withpet_service.model.entity.PetSitterWithPetService;
import lombok.*;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@ToString
public class PetSitterGetDetailInfoWithPetServiceResponse {
    private Long petSitterWithPetServiceId;
    private Long withPetServiceId;
    private String withPetServiceName;
    private String withPetServiceIntroduction;
    private String withPetServiceImg;
    private int petSitterWithPetServicePrice;

    public static List<PetSitterGetDetailInfoWithPetServiceResponse> toList(List<PetSitterWithPetService> petSitterServiceList){
        return petSitterServiceList.stream().map(petSitterService -> PetSitterGetDetailInfoWithPetServiceResponse.builder()
                .petSitterWithPetServiceId(petSitterService.getId())
                .withPetServiceId(petSitterService.getWithPetService().getId())
                .withPetServiceName(petSitterService.getWithPetService().getServiceName())
                .withPetServiceIntroduction(petSitterService.getWithPetService().getIntroduction())
                .withPetServiceImg(petSitterService.getWithPetService().getServiceImg())
                .petSitterWithPetServicePrice(petSitterService.getPrice())
                .build()).collect(Collectors.toList());
    }
}
