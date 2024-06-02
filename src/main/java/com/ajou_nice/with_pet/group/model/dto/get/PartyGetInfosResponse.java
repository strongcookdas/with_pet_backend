package com.ajou_nice.with_pet.group.model.dto.get;

import com.ajou_nice.with_pet.dog.model.dto.get.PartyGetInfosDogResponse;
import com.ajou_nice.with_pet.dog.model.entity.Dog;
import com.ajou_nice.with_pet.group.model.entity.Party;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class PartyGetInfosResponse {

    private Long partyId;
    private String partyName;
    private String partyLeaderName;
    private String partyLeaderId;
    private String partyLeaderImg;
    private String partyIsbn;
    private List<PartyGetInfosMemberResponse> partyMemberList;
    private List<PartyGetInfosDogResponse> partyDogList;

    public static PartyGetInfosResponse of(Party party) {
        return PartyGetInfosResponse.builder()
                .partyId(party.getPartyId())
                .partyName(party.getPartyName())
                .partyLeaderId(party.getPartyLeader().getEmail())
                .partyLeaderImg(party.getPartyLeader().getProfileImg())
                .partyLeaderName(party.getPartyLeader().getName())
                .partyIsbn(party.getPartyIsbn())
                .partyMemberList(
                        party.getUserPartyList().stream()
                                .filter(userParty -> !Objects.equals(userParty.getUser().getId(), party.getPartyLeader().getId())).map(PartyGetInfosMemberResponse::of)
                                .collect(Collectors.toList()))
                .partyDogList(new ArrayList<>())
                .build();
    }

    public void toPartyGetInfosDogResponseAndAddDogList(Dog dog) {
        this.partyDogList.add(PartyGetInfosDogResponse.of(dog));
    }
}
