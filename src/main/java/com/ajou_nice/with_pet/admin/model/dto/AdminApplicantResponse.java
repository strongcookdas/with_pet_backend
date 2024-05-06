package com.ajou_nice.with_pet.admin.model.dto;



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
public class AdminApplicantResponse {

	private Long applicantId;
	private String applicantName;
	private ApplicantStatus applicantStatus;

	public static AdminApplicantResponse of(User user){
		return AdminApplicantResponse.builder()
				.applicantId(user.getId())
				.applicantName(user.getName())
				.applicantStatus(user.getApplicantStatus())
				.build();
	}
}
