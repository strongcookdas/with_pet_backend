package com.ajou_nice.with_pet.domain.dto.admin;

import com.ajou_nice.with_pet.enums.ApplicantStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class AdminApplicantRequest {

	private Long applicantId;
	private Long applicant_userId;
	private ApplicantStatus applicantStatus;

}
