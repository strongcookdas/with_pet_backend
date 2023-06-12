package com.ajou_nice.with_pet.domain.dto.petsitterapplicant;



import com.ajou_nice.with_pet.domain.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@SuperBuilder
@ToString(callSuper = true)
public class ApplicantInfoResponse extends ApplicantBasicInfoResponse {


	// 유저 어드레스도 필요
	// 유저 로그인 아이디도 필요 ??

	private String applicant_identification;

	private String applicant_license_img;

	private Boolean applicant_is_smoking;

	private Boolean applicant_having_with_pet;

	private String applicant_care_experience;

	private String applicant_animal_career;

	private String applicant_petsitter_career;

	private String applicant_motivate;

	private String applicant_streetAdr;



	// 위 메서드는 처음 지원하고 나서, response를 위한 메서드
	public static ApplicantInfoResponse ofAll(User user){
		return ApplicantInfoResponse.builder()
				.applicant_user_name(user.getName())
				.applicant_user_id(user.getUserId())
				.applicant_user_profileImg(user.getProfileImg())
				.applicant_user_phone(user.getPhone())
				.applicant_identification(user.getIdentification())
				.applicant_license_img(user.getLicenseImg())
				.applicant_is_smoking(user.getIsSmoking())
				.applicant_having_with_pet(user.getHavingWithPet())
				.applicant_care_experience(user.getCareExperience())
				.applicant_animal_career(user.getAnimalCareer())
				.applicant_petsitter_career(user.getPetSitterCareer())
				.applicant_motivate(user.getMotivate())
				.applicant_status(user.getApplicantStatus())
				.applicant_streetAdr(user.getAddress().getStreetAdr())
				.build();
	}
}
