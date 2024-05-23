package com.ajou_nice.with_pet.hashtag.model.dto.get;


import com.ajou_nice.with_pet.hashtag.model.entity.PetSitterHashTag;

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
public class PetSitterRegisterMyInfoHashTagResponse {

    private Long petSitterHashTagId;
    private String petSitterHashTagName;

    public static List<PetSitterRegisterMyInfoHashTagResponse> toList(List<PetSitterHashTag> petSitterHashTags) {
        return petSitterHashTags.stream().map(petSitterHashTag -> PetSitterRegisterMyInfoHashTagResponse.builder()
                .petSitterHashTagId(petSitterHashTag.getId())
                .petSitterHashTagName(petSitterHashTag.getHashTagName())
                .build()).collect(Collectors.toList());
    }
}
