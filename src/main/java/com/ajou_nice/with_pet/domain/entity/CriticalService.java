package com.ajou_nice.with_pet.domain.entity;

import com.ajou_nice.with_pet.domain.dto.criticalservice.CriticalServiceRequest;
import com.ajou_nice.with_pet.domain.dto.criticalservice.CriticalServiceRequest.CriticalServiceModifyRequest;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@ToString
@Table(name="critical_withpetservice")
public class CriticalService {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "critical_serviceId")
	private Long id;

	private String introduction;

	private String serviceName;

	@Lob
	private String serviceImg;

	public void updateServiceInfo(CriticalServiceModifyRequest criticalServiceModifyRequest){
		this.serviceName = criticalServiceModifyRequest.getServiceName();
		this.serviceImg = criticalServiceModifyRequest.getServiceImg();
		this.introduction = criticalServiceModifyRequest.getServiceIntroduction();
	}

	public static CriticalService toEntity(CriticalServiceRequest criticalServiceRequest){
		return CriticalService.builder()
				.serviceName(criticalServiceRequest.getServiceName())
				.serviceImg(criticalServiceRequest.getServiceImg())
				.introduction(criticalServiceRequest.getServiceIntro())
				.build();
	}
}
