package com.ajou_nice.with_pet.admin.model.dto.refuse_applicant;



import com.ajou_nice.with_pet.domain.entity.User;
import com.ajou_nice.with_pet.enums.ApplicantStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@ToString
public class AdminRefuseApplicantResponse {

	private Long applicantId;
	private String applicantName;
	private ApplicantStatus applicantStatus;

	public static AdminRefuseApplicantResponse of(User user){
		return AdminRefuseApplicantResponse.builder()
				.applicantId(user.getId())
				.applicantName(user.getName())
				.applicantStatus(user.getApplicantStatus())
				.build();
	}
}
