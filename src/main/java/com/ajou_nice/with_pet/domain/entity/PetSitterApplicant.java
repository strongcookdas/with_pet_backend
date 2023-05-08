package com.ajou_nice.with_pet.domain.entity;

import com.ajou_nice.with_pet.domain.dto.petsitterapplicant.ApplicantInfoRequest;
import com.ajou_nice.with_pet.enums.ApplicantStatus;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.springframework.security.core.Authentication;


@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "petsitter_applicant")
@Builder
public class PetSitterApplicant {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "applicant_id")
	private Long id;

	@OneToOne(fetch = FetchType.LAZY)
	@NotNull
	@JoinColumn(name = "userId")
	private User user;
	@NotNull
	private String identification;

	@NotNull
	private String license_img;

	@NotNull
	private Boolean is_smoking;

	@NotNull
	private Boolean having_with_pet;

	@Lob
	private String care_experience;

	@Lob
	private String animal_career;

	private String petsitter_career;

	@Lob
	private String motivate;

	@Enumerated(EnumType.STRING)
	private ApplicantStatus applicantStatus;

	public static PetSitterApplicant of(ApplicantInfoRequest applicantInfoRequest, User user){
		ApplicantStatus defaultApplicantStatus = ApplicantStatus.WAIT;

		return PetSitterApplicant.builder()
				.user(user)
				.identification(applicantInfoRequest.getApplicant_identification())
				.license_img(applicantInfoRequest.getApplicant_license_img())
				.is_smoking(applicantInfoRequest.getApplicant_is_smoking())
				.having_with_pet(applicantInfoRequest.getApplicant_having_with_pet())
				.care_experience(applicantInfoRequest.getApplicant_care_experience())
				.animal_career(applicantInfoRequest.getApplicant_animal_career())
				.petsitter_career(applicantInfoRequest.getApplicant_petsitter_career())
				.motivate(applicantInfoRequest.getApplicant_motivate())
				.applicantStatus(defaultApplicantStatus)
				.build();
	}
}
