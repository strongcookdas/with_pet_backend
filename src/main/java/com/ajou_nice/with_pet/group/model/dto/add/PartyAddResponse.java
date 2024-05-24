package com.ajou_nice.with_pet.group.model.dto.add;

import com.ajou_nice.with_pet.domain.dto.dog.PartyAddDogResponse;
import com.ajou_nice.with_pet.domain.entity.Dog;
import com.ajou_nice.with_pet.group.model.dto.PartyMemberResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class PartyAddResponse {

    private Long partyId;
    private String partyName;
    private String partyLeaderName;
    private String partyLeaderEmail;
    private String partyLeaderImg;
    private String partyIsbn;
    private List<PartyMemberResponse> partyUserList;
    private List<PartyAddDogResponse> partyDogInfoList;

    public static PartyAddResponse of(Dog dog) {
        return PartyAddResponse.builder()
                .partyId(dog.getParty().getPartyId())
                .partyName(dog.getParty().getPartyName())
                .partyLeaderEmail(dog.getParty().getPartyLeader().getEmail())
                .partyLeaderImg(dog.getParty().getPartyLeader().getProfileImg())
                .partyLeaderName(dog.getParty().getPartyLeader().getName())
                .partyIsbn(dog.getParty().getPartyIsbn())
                .partyUserList(new ArrayList<>())
                .partyDogInfoList(Arrays.asList(PartyAddDogResponse.of(dog)))
                .build();
    }

}
