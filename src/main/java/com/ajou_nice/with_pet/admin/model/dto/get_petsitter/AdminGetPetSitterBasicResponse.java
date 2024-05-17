package com.ajou_nice.with_pet.admin.model.dto.get_petsitter;

import com.ajou_nice.with_pet.admin.model.dto.accept_applicant.AdminAcceptApplicantResponse;
import com.ajou_nice.with_pet.petsitter.model.entity.PetSitter;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@ToString
public class AdminGetPetSitterBasicResponse {
    private Long petSitterId;
    private String petSitterName;
    private String petSitterEmail;
    private String petSitterImg;
    private String petSitterPhone;
    private int petSitterReportCount;

    public static AdminGetPetSitterBasicResponse of(PetSitter petSitter) {
        return AdminGetPetSitterBasicResponse.builder()
                .petSitterId(petSitter.getId())
                .petSitterName(petSitter.getPetSitterName())
                .petSitterEmail(petSitter.getUser().getEmail())
                .petSitterImg(petSitter.getProfileImg())
                .petSitterPhone(petSitter.getPetSitterPhone())
                .petSitterReportCount(petSitter.getReportCount())
                .build();
    }

    public static List<AdminGetPetSitterBasicResponse> toList(List<PetSitter> petSitters) {
        return petSitters.stream().map(petSitter -> AdminGetPetSitterBasicResponse.builder()
                .petSitterId(petSitter.getId())
                .petSitterName(petSitter.getUser().getName())
                .petSitterEmail(petSitter.getUser().getEmail())
                .petSitterImg(petSitter.getUser().getProfileImg())
                .petSitterPhone(petSitter.getUser().getPhone())
                .petSitterReportCount(petSitter.getReportCount())
                .build()).collect(Collectors.toList());
    }
}
