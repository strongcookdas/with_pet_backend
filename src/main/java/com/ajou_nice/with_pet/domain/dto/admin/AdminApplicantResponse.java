package com.ajou_nice.with_pet.domain.dto.admin;


import com.ajou_nice.with_pet.domain.entity.PetSitterApplicant;
import com.ajou_nice.with_pet.enums.ApplicantStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@SuperBuilder
@ToString
public class AdminApplicantResponse {

	protected Long applicant_id;

	protected Long applicant_userId;

	protected String applicant_userName;
	protected ApplicantStatus applicantStatus;

	public static AdminApplicantResponse of(PetSitterApplicant petSitterApplicant){
		return AdminApplicantResponse.builder()
				.applicant_id(petSitterApplicant.getId())
				.applicant_userId(petSitterApplicant.getUser().getUserId())
				.applicant_userName(petSitterApplicant.getUser().getName())
				.applicantStatus(petSitterApplicant.getApplicantStatus())
				.build();
	}
}
