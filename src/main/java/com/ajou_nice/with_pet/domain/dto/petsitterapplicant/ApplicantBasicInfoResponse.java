package com.ajou_nice.with_pet.domain.dto.petsitterapplicant;


import com.ajou_nice.with_pet.domain.entity.User;
import com.ajou_nice.with_pet.enums.ApplicantStatus;
import java.util.List;
import java.util.stream.Collectors;
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
public class ApplicantBasicInfoResponse {

	// 지원자 id, 유저 프로필이미지, 유저 이름, 유저 아이디, 지원 상태, 유저 폰 번호
	// protected 지시자로 허용해줘야지만 상속받은 클래스에서 접근받아 builder로 활용할 수 있음
	// 기존의 builder대신 @SuperBuilder 어노테이션을 사용해야 한다!!
	protected Long applicant_user_id;
	protected String applicant_user_name;
	protected String applicant_user_auth_id;
	protected String applicant_user_profileImg;

	protected String applicant_user_phone;
	protected ApplicantStatus applicant_status;

	public static ApplicantBasicInfoResponse of(User user){
		return ApplicantBasicInfoResponse.builder()
				.applicant_user_id(user.getId())
				.applicant_user_name(user.getName())
				.applicant_user_auth_id(user.getEmail())
				.applicant_user_profileImg(user.getProfileImg())
				.applicant_user_phone(user.getPhone())
				.applicant_status(user.getApplicantStatus())
				.build();
	}

	public static List<ApplicantBasicInfoResponse> toList(List<User> users){
		return users.stream().map(applicant-> ApplicantBasicInfoResponse.builder()
				.applicant_user_name(applicant.getName())
				.applicant_user_id(applicant.getId())
				.applicant_user_profileImg(applicant.getProfileImg())
				.applicant_user_auth_id(applicant.getEmail())
				.applicant_user_phone(applicant.getPhone())
				.applicant_status(applicant.getApplicantStatus())
				.build()).collect(Collectors.toList());
	}
}
