package com.ajou_nice.with_pet.domain.dto.criticalservice;


import javax.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class CriticalServiceRequest {

	@NotEmpty
	private String serviceName;

	@NotEmpty
	private String serviceImg;

	@NotEmpty
	private String serviceIntro;

	@Data
	public static class CriticalServiceModifyRequest{
		@NotEmpty
		private Long serviceId;
		private String serviceName;
		private String serviceImg;
		private String serviceIntroduction;
	}
}
