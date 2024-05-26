package com.ajou_nice.with_pet.group.model.dto;

import com.ajou_nice.with_pet.dog.model.dto.DogInfoResponse;
import com.ajou_nice.with_pet.dog.model.entity.Dog;
import com.ajou_nice.with_pet.group.model.entity.Party;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
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
    private String partyName;
    private String leaderName;
    private String leaderId;
    private String leaderImg;
    private String partyIsbn;
    private List<PartyMemberResponse> userPartyList;
    private List<DogInfoResponse> dogInfoResponseList;

    public static PartyInfoResponse of(Party party) {
        return PartyInfoResponse.builder()
                .partyId(party.getPartyId())
                .partyName(party.getPartyName())
                .leaderId(party.getPartyLeader().getEmail())
                .leaderImg(party.getPartyLeader().getProfileImg())
                .leaderName(party.getPartyLeader().getName())
                .partyIsbn(party.getPartyIsbn())
                .userPartyList(
                        party.getUserPartyList().stream()
                                .filter(userParty -> userParty.getUser().getId()
                                        != party.getPartyLeader().getId()).map(PartyMemberResponse::of)
                                .collect(
                                        Collectors.toList()))
                .dogInfoResponseList(new ArrayList<>())
                .build();
    }

    public static PartyInfoResponse of(Dog dog) {
        return PartyInfoResponse.builder()
                .partyId(dog.getParty().getPartyId())
                .partyName(dog.getParty().getPartyName())
                .leaderId(dog.getParty().getPartyLeader().getEmail())
                .leaderImg(dog.getParty().getPartyLeader().getProfileImg())
                .leaderName(dog.getParty().getPartyLeader().getName())
                .partyIsbn(dog.getParty().getPartyIsbn())
                .userPartyList(new ArrayList<>())
                .dogInfoResponseList(Arrays.asList(DogInfoResponse.of(dog)))
                .build();
    }

    public void updatePartyInfoResponse(Dog dog) {
        System.out.println("업데이트");
        this.dogInfoResponseList.add(DogInfoResponse.of(dog));
    }

    public void updatePartyInfoResponse(List<Dog> dog) {
        this.dogInfoResponseList = dog.stream().map(DogInfoResponse::of)
                .collect(Collectors.toList());
    }

}
