package com.ajou_nice.with_pet.domain.dto.review;

import javax.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class ReviewRequest {

	private Long reservationId;

	@Lob
	private String content;

	private double grade;

	public static ReviewRequest of(Long reservationId, String content, double grade){
		return ReviewRequest.builder()
				.reservationId(reservationId)
				.content(content)
				.grade(grade)
				.build();
	}
}
