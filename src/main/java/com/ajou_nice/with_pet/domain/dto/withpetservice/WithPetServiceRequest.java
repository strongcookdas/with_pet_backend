package com.ajou_nice.with_pet.domain.dto.withpetservice;


import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class WithPetServiceRequest {

	@NotEmpty
	private String serviceName;
	@NotEmpty
	private String serviceImg;
	@NotEmpty
	private String serviceIntroduction;

	@Data
	public static class WithPetServiceModifyRequest{
		@NotNull
		private Long serviceId;

		private String serviceName;

		private String serviceImg;

		private String serviceIntroduction;
	}
}
