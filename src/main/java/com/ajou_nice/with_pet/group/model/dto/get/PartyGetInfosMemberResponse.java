package com.ajou_nice.with_pet.group.model.dto.get;

import com.ajou_nice.with_pet.domain.entity.UserParty;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class PartyGetInfosMemberResponse {

    private Long memberId;
    private String memberName;
    private String memberProfileImg;

    public static PartyGetInfosMemberResponse of(UserParty userParty) {
        return PartyGetInfosMemberResponse.builder()
                .memberId(userParty.getUser().getId())
                .memberName(userParty.getUser().getName())
                .memberProfileImg(userParty.getUser().getProfileImg())
                .build();
    }
}
