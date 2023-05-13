package com.ajou_nice.with_pet.domain.dto.party;

import javax.validation.constraints.NotBlank;
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
public class PartyMemberRequest {

    @NotBlank(message = "초대코드를 입력해주세요.")
    private String partyIsbn;
}
