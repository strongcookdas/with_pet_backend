package com.ajou_nice.with_pet.admin.model.dto;

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
