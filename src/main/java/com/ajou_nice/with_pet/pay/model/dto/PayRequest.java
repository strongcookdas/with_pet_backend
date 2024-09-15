package com.ajou_nice.with_pet.pay.model.dto;

import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public class PayRequest {

	@AllArgsConstructor
	@NoArgsConstructor
	@Getter
	@ToString
	public static class PaySimpleRequest{
		@NotNull
		private Long reservationId;
	}
}
