package com.ajou_nice.with_pet.domain.dto.kakaopay;

import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public class PayRequest {

	@AllArgsConstructor
	@Getter
	@ToString
	public static class PaySimpleRequest{
		@NotNull
		private Long reservationId;
	}


}
