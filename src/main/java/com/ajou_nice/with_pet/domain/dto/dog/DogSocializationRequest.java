package com.ajou_nice.with_pet.domain.dto.dog;

import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@ToString
public class DogSocializationRequest {

	@NotBlank
	private int q1;
	@NotBlank
	private int q2;
	@NotBlank
	private int q3;
	@NotBlank
	private int q4;
	@NotBlank
	private int q5;
}
