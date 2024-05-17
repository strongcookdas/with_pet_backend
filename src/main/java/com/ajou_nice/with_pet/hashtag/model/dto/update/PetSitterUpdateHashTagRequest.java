package com.ajou_nice.with_pet.hashtag.model.dto.update;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class PetSitterUpdateHashTagRequest {
    private String hashTagName;
}
