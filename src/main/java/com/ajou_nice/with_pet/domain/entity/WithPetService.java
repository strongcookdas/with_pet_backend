package com.ajou_nice.with_pet.domain.entity;


import com.ajou_nice.with_pet.domain.dto.withpetservice.WithPetServiceRequest;
import com.ajou_nice.with_pet.domain.dto.withpetservice.WithPetServiceRequest.WithPetServiceModifyRequest;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

//Service는 위드펫에서 기본적으로 제공하는 Service들을 말한다.
@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class WithPetService {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "service_id")
	private Long id;

	private String name;

	private String service_Img;

	private String introduction;

	public void updateServiceInfo(WithPetServiceModifyRequest withPetServiceModifyRequest){
		this.name = withPetServiceModifyRequest.getServiceName();
		this.service_Img = withPetServiceModifyRequest.getServiceImg();
		this.introduction = withPetServiceModifyRequest.getServiceIntroduction();
	}

	public static WithPetService toEntity(WithPetServiceRequest withPetServiceRequest){

		return WithPetService.builder()
				.name(withPetServiceRequest.getServiceName())
				.service_Img(withPetServiceRequest.getServiceImg())
				.introduction(withPetServiceRequest.getServiceIntroduction())
				.build();
	}

}
