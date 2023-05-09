package com.ajou_nice.with_pet.domain.dto.admin;


import com.ajou_nice.with_pet.domain.entity.PetSitter;
import com.ajou_nice.with_pet.enums.ApplicantStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@SuperBuilder
@ToString(callSuper = true)
public class AdminAcceptApplicantResponse extends AdminApplicantResponse{

	private Long applicant_id;
	private Long applicant_userId;
	private String applicant_userName;

	private ApplicantStatus applicantStatus;

	private Long petSitter_id;

	public static AdminAcceptApplicantResponse ofAccept(PetSitter petSitter){
		return AdminAcceptApplicantResponse.builder()
				.applicant_id(petSitter.getApplicant().getId())
				.applicant_userId(petSitter.getApplicant().getUser().getUserId())
				.applicant_userName(petSitter.getApplicant().getUser().getName())
				.applicantStatus(petSitter.getApplicant().getApplicantStatus())
				.petSitter_id(petSitter.getId())
				.build();
	}

}
