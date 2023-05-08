package com.ajou_nice.with_pet.domain.dto.petsitterapplicant;


import com.ajou_nice.with_pet.domain.entity.PetSitterApplicant;
import com.ajou_nice.with_pet.enums.ApplicantStatus;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@Builder
@ToString
public class ApplicantInfoResponse {
	private String applicant_user_name;
	private Long applicant_user_userId;

	private String applicant_user_profileImg;

	private String applicant_user_phone;

	// 유저 어드레스도 필요
	// 유저 로그인 아이디도 필요 ??

	private Long applicant_id;

	private String applicant_identification;

	private String applicant_license_img;

	private Boolean applicant_is_smoking;

	private Boolean applicant_having_with_pet;

	private String applicant_care_experience;

	private String applicant_animal_career;

	private String applicant_petsitter_career;

	private String applicant_motivate;

	private ApplicantStatus applicant_status;




	public static List<ApplicantInfoResponse> toList(List<PetSitterApplicant> petSitterApplicants){
		return petSitterApplicants.stream().map(applicant -> new ApplicantInfoResponse())
				.collect(Collectors.toList());
	}



	public static ApplicantInfoResponse of(PetSitterApplicant petSitterApplicant){
		return ApplicantInfoResponse.builder()
				.applicant_user_name(petSitterApplicant.getUser().getName())
				.applicant_user_userId(petSitterApplicant.getUser().getUserId())
				.applicant_user_profileImg(petSitterApplicant.getUser().getProfileImg())
				.applicant_user_phone(petSitterApplicant.getUser().getPhone())
				.applicant_id(petSitterApplicant.getId())
				.applicant_identification(petSitterApplicant.getIdentification())
				.applicant_license_img(petSitterApplicant.getLicense_img())
				.applicant_is_smoking(petSitterApplicant.getIs_smoking())
				.applicant_having_with_pet(petSitterApplicant.getHaving_with_pet())
				.applicant_care_experience(petSitterApplicant.getCare_experience())
				.applicant_animal_career(petSitterApplicant.getAnimal_career())
				.applicant_petsitter_career(petSitterApplicant.getPetsitter_career())
				.applicant_motivate(petSitterApplicant.getMotivate())
				.applicant_status(petSitterApplicant.getApplicantStatus())
				.build();
	}
}
