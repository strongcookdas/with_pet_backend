package com.ajou_nice.with_pet.withpet_service.model.dto.add;

import com.ajou_nice.with_pet.withpet_service.model.entity.WithPetService;
import lombok.*;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@ToString
public class PetSitterRegisterMyInfoWithPetServiceResponse {
    private Long withPetServiceId;
    private String withPetServiceName;
    private String withPetServiceImg;
    private String withPetServiceIntroduction;

    public static PetSitterRegisterMyInfoWithPetServiceResponse of(WithPetService withPetService){
        return PetSitterRegisterMyInfoWithPetServiceResponse.builder()
                .withPetServiceId(withPetService.getId())
                .withPetServiceName(withPetService.getServiceName())
                .withPetServiceImg(withPetService.getServiceImg())
                .withPetServiceIntroduction(withPetService.getIntroduction())
                .build();
    }

    public static List<PetSitterRegisterMyInfoWithPetServiceResponse> toList(List<WithPetService> withPetServiceList){
        return withPetServiceList.stream().map(withPetService-> PetSitterRegisterMyInfoWithPetServiceResponse.builder()
                .withPetServiceId(withPetService.getId())
                .withPetServiceName(withPetService.getServiceName())
                .withPetServiceImg(withPetService.getServiceImg())
                .withPetServiceIntroduction(withPetService.getIntroduction())
                .build()).collect(Collectors.toList());
    }
}
