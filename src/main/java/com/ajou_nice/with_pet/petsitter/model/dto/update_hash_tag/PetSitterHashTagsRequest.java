package com.ajou_nice.with_pet.petsitter.model.dto.update_hash_tag;

import com.ajou_nice.with_pet.hashtag.model.dto.update.PetSitterUpdateHashTagRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class PetSitterHashTagsRequest {
    private List<PetSitterUpdateHashTagRequest> petSitterHashTagRequests;
}
