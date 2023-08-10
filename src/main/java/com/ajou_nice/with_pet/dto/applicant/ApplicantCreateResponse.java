package com.ajou_nice.with_pet.dto.applicant;


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
public class ApplicantCreateResponse {

    private Long applicantUserId;
    private String applicantName;
    private String applicantEmail;
    private String applicantProfileImg;
    private String applicantPhone;
    private String applicantStatus;
    private LocalDate applicantBirth;
    private Boolean applicantIsSmoking;
    private Gender applicantGender;
    private Boolean applicantHavingWithPet;
    private String applicantAnimalCareer;
    private String applicantMotivation;
    private String applicantLicenseImg;
    private String applicantStreetAdr;

    public static ApplicantCreateResponse of(User user) {
        return ApplicantCreateResponse.builder()
                .applicantUserId(user.getUserId())
                .applicantName(user.getName())
                .applicantEmail(user.getEmail())
                .applicantProfileImg(user.getProfileImg())
                .applicantPhone(user.getPhone())
                .applicantStatus(user.getApplicantStatus().name())
                .applicantBirth(user.getBirth())
                .applicantIsSmoking(user.getIsSmoking())
                .applicantGender(user.getGender())
                .applicantHavingWithPet(user.getHavingWithPet())
                .applicantAnimalCareer(user.getAnimalCareer())
                .applicantMotivation(user.getMotivation())
                .applicantLicenseImg(user.getLicenseImg())
                .applicantStreetAdr(user.getAddress().getStreetAdr())
                .build();
    }
}
