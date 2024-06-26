package com.ajou_nice.with_pet.admin.model.dto.add_critical;


import javax.persistence.Lob;
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
public class AddWithPetServiceRequest {

	@NotNull
	private String serviceName;
	@NotNull
	@Lob
	private String serviceImg;
	@NotNull
	private String serviceIntro;

	@Data
	public static class WithPetServiceModifyRequest{
		@NotNull
		private Long serviceId;

		private String serviceName;

		@Lob
		private String serviceImg;

		private String serviceIntroduction;
	}
}
