package com.ajou_nice.with_pet.admin.model.dto.get_applicant;

import com.ajou_nice.with_pet.domain.entity.User;
import com.ajou_nice.with_pet.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class AdminGetApplicantDetailResponse {
    private Long applicantUserId;
    private String applicantName;
    private String applicantEmail;
    private String applicantImg;
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

    public static AdminGetApplicantDetailResponse of(User user) {
        return AdminGetApplicantDetailResponse.builder()
                .applicantUserId(user.getId())
                .applicantName(user.getName())
                .applicantEmail(user.getEmail())
                .applicantImg(user.getProfileImg())
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
