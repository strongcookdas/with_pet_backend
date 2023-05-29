package com.ajou_nice.with_pet.domain.dto.party;

import com.ajou_nice.with_pet.domain.dto.dog.DogInfoResponse;
import com.ajou_nice.with_pet.domain.entity.Party;
import com.ajou_nice.with_pet.domain.entity.UserParty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class PartyInfoResponse {

    private Long partyId;
    private String leader;
    private String partyIsbn;
    private List<UserParty> userPartyList;
    private List<DogInfoResponse> dogInfoResponseList;

    public static PartyInfoResponse of(Party party) {
        return PartyInfoResponse.builder()
                .partyId(party.getPartyId())
                .leader(party.getUser().getName())
                .partyIsbn(party.getPartyIsbn())
                .build();
    }

}
