package com.ajou_nice.with_pet.dto.applicant;


import com.ajou_nice.with_pet.enums.Gender;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Lob;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@ToString
public class PetsitterApplicationRequest {

    @NotNull(message = "올바른 형식의 생년월일을 입력해주세요.")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birth;

    @NotNull(message = "흡연여부 문항을 체크해주세요.")
    private Boolean isSmoking;

    @NotNull(message = "성별을 입력해주세요.")
    private Gender gender;

    @NotNull(message = "반려 경험 여부 문항을 체크해주세요.")
    private Boolean havingWithPet;

    private String animalCareer;

    private String motivation;

    @NotBlank(message = "자격증 업로드를 해주세요.")
    private String licenseImg;


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
