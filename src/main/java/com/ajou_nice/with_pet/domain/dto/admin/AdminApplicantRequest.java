package com.ajou_nice.with_pet.domain.dto.admin;

import com.ajou_nice.with_pet.enums.ApplicantStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class AdminApplicantRequest {
	private Long userId;
}
