package com.ajou_nice.with_pet.applicant.model.dto;


import com.ajou_nice.with_pet.domain.entity.User;
import com.ajou_nice.with_pet.enums.Gender;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@ToString(callSuper = true)
public class PetSitterApplicationResponse {

    private Long userId;
    private String name;
    private String email;
    private String profileImg;
    private String phone;
    private String status;
    private LocalDate birth;
    private Boolean isSmoking;
    private Gender gender;
    private Boolean havingWithPet;
    private String animalCareer;
    private String motivation;
    private String licenseImg;
    private String streetAdr;

    public static PetSitterApplicationResponse of(User user) {
        return PetSitterApplicationResponse.builder()
                .userId(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .profileImg(user.getProfileImg())
                .phone(user.getPhone())
                .status(user.getApplicantStatus().name())
                .birth(user.getBirth())
                .isSmoking(user.getIsSmoking())
                .gender(user.getGender())
                .havingWithPet(user.getHavingWithPet())
                .animalCareer(user.getAnimalCareer())
                .motivation(user.getMotivation())
                .licenseImg(user.getLicenseImg())
                .streetAdr(user.getAddress().getStreetAdr())
                .build();
    }
}
