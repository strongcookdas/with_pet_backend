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
public class AddCriticalServiceRequest {

	@NotNull
	private String serviceName;

	@NotNull
	private String serviceImg;

	@NotNull
	private String serviceIntro;
}
