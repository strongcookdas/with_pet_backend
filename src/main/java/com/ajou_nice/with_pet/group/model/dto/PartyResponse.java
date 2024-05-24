package com.ajou_nice.with_pet.group.model.dto;

import com.ajou_nice.with_pet.group.model.entity.Party;
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
public class PartyResponse {

    private Long partyId;
    private String leader;
    private String partyIsbn;

    public static PartyResponse of(Party party) {
        return PartyResponse.builder()
                .partyId(party.getPartyId())
                .leader(party.getPartyName())
                .partyIsbn(party.getPartyIsbn())
                .build();
    }

}
