package com.ajou_nice.with_pet.domain.dto.petsitterapplicant;


import javax.persistence.Lob;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
@Builder
public class ApplicantInfoRequest {

	@Pattern(regexp = "^[\\d]{6}-[1-4][\\d]{6}+$", message = "올바른 주민등록번호를 사용하세요")
	@NotNull(message = "주민등록번호는 기본사항입니다.")
	private String applicant_identification;

	@NotNull(message = "자격증 업로드는 기본사항입니다.")
	@Lob
	private String applicant_license_img;

	private Boolean applicant_is_smoking;

	private Boolean applicant_having_with_pet;

	@Lob
	private String applicant_care_experience;

	@Lob
	private String applicant_animal_career;

	@Lob
	private String applicant_petsitter_career;

	@Lob
	private String applicant_motivate;

	@Data
	public static class ApplicantModifyRequest{
		private String applicant_petsitter_career;
		@Lob
		private String applicant_care_experience;
		@Lob
		private String applicant_animal_career;
		@Lob
		private String applicant_motivate;

	}


}
