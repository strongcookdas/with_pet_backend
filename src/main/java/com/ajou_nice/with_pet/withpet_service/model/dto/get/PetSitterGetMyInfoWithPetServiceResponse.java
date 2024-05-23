package com.ajou_nice.with_pet.withpet_service.model.dto.get;

import com.ajou_nice.with_pet.withpet_service.model.entity.WithPetService;
import lombok.*;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@ToString
public class PetSitterGetMyInfoWithPetServiceResponse {
    private Long withPetServiceId;
    private String withPetServiceName;
    private String withPetServiceImg;
    private String withPetServiceIntroduction;

    public static PetSitterGetMyInfoWithPetServiceResponse of(WithPetService withPetService){
        return PetSitterGetMyInfoWithPetServiceResponse.builder()
                .withPetServiceId(withPetService.getId())
                .withPetServiceName(withPetService.getServiceName())
                .withPetServiceImg(withPetService.getServiceImg())
                .withPetServiceIntroduction(withPetService.getIntroduction())
                .build();
    }

    public static List<PetSitterGetMyInfoWithPetServiceResponse> toList(List<WithPetService> withPetServiceList){
        return withPetServiceList.stream().map(withPetService-> PetSitterGetMyInfoWithPetServiceResponse.builder()
                .withPetServiceId(withPetService.getId())
                .withPetServiceName(withPetService.getServiceName())
                .withPetServiceImg(withPetService.getServiceImg())
                .withPetServiceIntroduction(withPetService.getIntroduction())
                .build()).collect(Collectors.toList());
    }
}
