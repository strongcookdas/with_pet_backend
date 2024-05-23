package com.ajou_nice.with_pet.petsitter.model.dto.get_main;

import com.ajou_nice.with_pet.exception.AppException;
import com.ajou_nice.with_pet.exception.ErrorCode;
import com.ajou_nice.with_pet.hashtag.model.dto.get.PetSitterGetMainHashTagResponse;
import com.ajou_nice.with_pet.petsitter.model.entity.PetSitter;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@ToString
public class PetSitterGetMainResponse {
    private String petSitterName;
    private Long petSitterId;
    private String petSitterRepresentativeHouse;
    private List<PetSitterGetMainHashTagResponse> petSitterHashTags;
    private Integer petSitterReviewCount;
    private Double petSitterStartRate;

    public static PetSitterGetMainResponse of(PetSitter petSitter) {
        return PetSitterGetMainResponse.builder()
                .petSitterId(petSitter.getId())
                .petSitterRepresentativeHouse(petSitter.getPetSitterHouseList()
                        .stream().filter(house -> house.getRepresentative().equals(true)).findAny().orElseThrow(() -> {
                            throw new AppException(ErrorCode.PETSITTER_MAIN_HOUSE_NOT_FOUND,
                                    ErrorCode.PETSITTER_MAIN_HOUSE_NOT_FOUND.getMessage());
                        })
                        .getHouse_img())
                .petSitterName(petSitter.getPetSitterName())
                .petSitterHashTags(PetSitterGetMainHashTagResponse.toList(petSitter.getPetSitterHashTagList()))
                .petSitterReviewCount(petSitter.getReviewCount())
                .petSitterStartRate(petSitter.getStartRate())
                .build();
    }
}
