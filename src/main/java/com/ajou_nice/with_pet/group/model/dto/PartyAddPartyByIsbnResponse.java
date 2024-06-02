package com.ajou_nice.with_pet.group.model.dto;

import com.ajou_nice.with_pet.dog.model.dto.DogInfoResponse;
import com.ajou_nice.with_pet.dog.model.entity.Dog;
import com.ajou_nice.with_pet.group.model.entity.Party;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class PartyAddPartyByIsbnResponse {

    private Long partyId;
    private String partyName;
    private String partyLeaderName;
    private String partyLeaderId;
    private String partyLeaderImg;
    private String partyIsbn;
    private List<PartyMemberResponse> partyMemberList;
    private List<DogInfoResponse> partyDogList;

    public static PartyAddPartyByIsbnResponse of(Party party) {
        return PartyAddPartyByIsbnResponse.builder()
                .partyId(party.getPartyId())
                .partyName(party.getPartyName())
                .partyLeaderId(party.getPartyLeader().getEmail())
                .partyLeaderImg(party.getPartyLeader().getProfileImg())
                .partyLeaderName(party.getPartyLeader().getName())
                .partyIsbn(party.getPartyIsbn())
                .partyMemberList(
                        party.getUserPartyList().stream()
                                .filter(userParty -> !Objects.equals(userParty.getUser().getId(), party.getPartyLeader().getId())).map(PartyMemberResponse::of)
                                .collect(Collectors.toList()))
                .partyDogList(new ArrayList<>())
                .build();
    }

    public static PartyAddPartyByIsbnResponse of(Dog dog) {
        return PartyAddPartyByIsbnResponse.builder()
                .partyId(dog.getParty().getPartyId())
                .partyName(dog.getParty().getPartyName())
                .partyLeaderId(dog.getParty().getPartyLeader().getEmail())
                .partyLeaderImg(dog.getParty().getPartyLeader().getProfileImg())
                .partyLeaderName(dog.getParty().getPartyLeader().getName())
                .partyIsbn(dog.getParty().getPartyIsbn())
                .partyMemberList(new ArrayList<>())
                .partyDogList(Arrays.asList(DogInfoResponse.of(dog)))
                .build();
    }

    public void updatePartyInfoResponse(Dog dog) {
        System.out.println("업데이트");
        this.partyDogList.add(DogInfoResponse.of(dog));
    }

    public void updatePartyInfoResponse(List<Dog> dog) {
        this.partyDogList = dog.stream().map(DogInfoResponse::of)
                .collect(Collectors.toList());
    }

}
