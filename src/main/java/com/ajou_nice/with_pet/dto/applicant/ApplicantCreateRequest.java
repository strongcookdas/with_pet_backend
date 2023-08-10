package com.ajou_nice.with_pet.dto.applicant;


import com.ajou_nice.with_pet.enums.Gender;
import java.time.LocalDate;
import javax.persistence.Lob;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@ToString
public class ApplicantCreateRequest {

    @NotNull(message = "올바른 형식의 생년월일을 입력해주세요.")
    private LocalDate applicantBirth;

    @NotNull(message = "흡연여부 문항을 체크해주세요.")
    private Boolean applicantIsSmoking;

    @NotNull(message = "성별을 입력해주세요.")
    private Gender applicantGender;

    @NotNull(message = "반려 경험 여부 문항을 체크해주세요.")
    private Boolean applicantHavingWithPet;

    @Lob
    private String applicantAnimalCareer;

    @Lob
    private String applicantMotivation;

    @NotNull(message = "자격증 업로드를 해주세요.")
    private String applicantLicenseImg;


    @Data
    public static class ApplicantModifyRequest {

        private String applicant_petsitter_career;
        @Lob
        private String applicant_care_experience;
        @Lob
        private String applicant_animal_career;
        @Lob
        private String applicant_motivate;

    }


}
