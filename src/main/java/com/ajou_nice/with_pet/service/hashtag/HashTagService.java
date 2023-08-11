package com.ajou_nice.with_pet.service.hashtag;

import com.ajou_nice.with_pet.domain.entity.HashTag;
import com.ajou_nice.with_pet.domain.entity.PetSitter;
import com.ajou_nice.with_pet.dto.hashtag.HashTagRequest;
import com.ajou_nice.with_pet.dto.hashtag.HashTagResponse;
import com.ajou_nice.with_pet.repository.PetSitterHashTagRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HashTagService {

    private final PetSitterHashTagRepository hashTagRepository;

    public List<HashTag> registerHashTagAll(PetSitter petSitter,
            List<HashTagRequest> hashTagRequests) {
        List<HashTag> hashTags = hashTagRequests.stream().map(h -> HashTag.of(petSitter, h))
                .collect(
                        Collectors.toList());
        return hashTags;
    }
}
