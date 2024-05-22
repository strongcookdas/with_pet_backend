package com.ajou_nice.with_pet.hashtag.model.dto.get;

import com.ajou_nice.with_pet.hashtag.model.entity.PetSitterHashTag;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@ToString
public class PetSitterGetMainHashTagResponse {
    private Long petSitterHashTagId;
    private String petSitterHashTagName;

    public static List<PetSitterGetMainHashTagResponse> toList(List<PetSitterHashTag> petSitterHashTags) {
        return petSitterHashTags.stream().map(petSitterHashTag -> PetSitterGetMainHashTagResponse.builder()
                        .petSitterHashTagId(petSitterHashTag.getId())
                        .petSitterHashTagName(petSitterHashTag.getHashTagName())
                        .build())
                .collect(Collectors.toList());
    }
}
