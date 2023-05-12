package com.ajou_nice.with_pet.domain.dto.party;

import com.ajou_nice.with_pet.domain.entity.UserParty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class PartyMemberResponse {

    private Long userId;
    private String userName;
    private String profileImg;

    public static PartyMemberResponse of(UserParty userParty) {
        return PartyMemberResponse.builder()
                .userId(userParty.getUser().getUserId())
                .userName(userParty.getUser().getName())
                .profileImg(userParty.getUser().getProfileImg())
                .build();
    }
}
