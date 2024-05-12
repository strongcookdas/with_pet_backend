package com.ajou_nice.with_pet.hashtag.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class PetSitterAddHashTagRequest {
    @NotBlank
    private String hashTagName;
}
