package com.ajou_nice.with_pet.hashtag.model.entity;

import com.ajou_nice.with_pet.hashtag.model.dto.PetSitterAddHashTagRequest;
import com.ajou_nice.with_pet.house.model.dto.PetSitterAddHouseRequest;
import com.ajou_nice.with_pet.petsitter.model.dto.PetSitterRequest.PetSitterHashTagRequest;
import com.ajou_nice.with_pet.petsitter.model.entity.PetSitter;
import lombok.*;

import javax.persistence.*;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@ToString
@Table(name = "petsitter_hashtag")
public class PetSitterHashTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "petsitter_id")
    private PetSitter petSitter;

    private String hashTagName;

    public static PetSitterHashTag toEntity(PetSitter petSitter, PetSitterHashTagRequest petSitterHashTagRequest) {
        return PetSitterHashTag.builder()
                .petSitter(petSitter)
                .hashTagName(petSitterHashTagRequest.getHashTagName())
                .build();
    }

    public static List<PetSitterHashTag> toList(PetSitter petSitter, List<PetSitterAddHashTagRequest> hashTags) {
        return hashTags.stream().map(hashTag -> PetSitterHashTag.builder()
                        .hashTagName(hashTag.getHashTagName())
                        .petSitter(petSitter).build())
                .collect(Collectors.toList());
    }

}
