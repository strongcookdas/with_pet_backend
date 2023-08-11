package com.ajou_nice.with_pet.dto.hashtag;

import com.ajou_nice.with_pet.domain.entity.HashTag;
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
public class HashTagResponse {
    private Long hashTagId;

    private String hashTagName;


    public static List<HashTagResponse> toList(List<HashTag> hashTags){
        return hashTags.stream().map(petSitterHashTag -> HashTagResponse.builder()
                .hashTagId(petSitterHashTag.getHashTagId())
                .hashTagName(petSitterHashTag.getName())
                .build()).collect(Collectors.toList());
    }
}
