package com.ajou_nice.with_pet.domain.dto.petsitterapplicant;


import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ApplicantInfoRequest {

	@Pattern(regexp = "^[\\d]{6}-[1-4][\\d]{6}+$", message = "올바른 주민등록번호를 사용하세요")
	@NotEmpty(message = "주민등록번호는 기본사항입니다.")
	private String applicant_identification;

	@NotEmpty(message = "자격증 업로드는 기본사항입니다.")
	private String applicant_license_img;

	@NotEmpty(message = "흡연여부는 기본사항입니다.")
	private Boolean applicant_is_smoking;

	@NotEmpty(message = "강아지 반려경험 여부는 기본사항입니다.")
	private Boolean applicant_having_with_pet;

	private String applicant_care_experience;

	private String applicant_animal_career;

	private String applicant_petsitter_career;

	private String applicant_motivate;


}
